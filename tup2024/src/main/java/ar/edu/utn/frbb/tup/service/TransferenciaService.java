package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.MovimientoDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenyDestinoIguales;
import ar.edu.utn.frbb.tup.model.exception.CuentasOrigenDestinoNulas;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TranferenciaBanelcoFalladaExcepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class TransferenciaService {
    @Autowired
    CuentaDao cuentaDao;
    @Autowired
    TransferenciaValidator transValidator;
    @Autowired
    CuentaService cuentaService;
    @Autowired
    BanelcoService banelcoService;
    @Autowired
    ClienteDao clienteDao;

    public Transferencia realizarTransferencia(TransferenciaDto transferenciaDto) throws TipoDeMonedaIncorrectoExcepcion, MonedaVaciaExcepcion, CuentaOrigenyDestinoIguales, MontoMenorIgualQueCero, CuentasOrigenDestinoNulas, CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion, TranferenciaBanelcoFalladaExcepcion{
        Cuenta cuenta = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuenta2 = cuentaDao.find(transferenciaDto.getCuentaDestino());
        Transferencia trans = new Transferencia(transferenciaDto);
        if (trans.getTipoMoneda() == null || trans.getTipoMoneda().isEmpty()) {
            throw new MonedaVaciaExcepcion("La moneda no puede ser vacia");
        }
        if (transferenciaDto.getCuentaOrigen() == 0 || transferenciaDto.getCuentaDestino() == 0) {
            throw new CuentasOrigenDestinoNulas("Las cuentas de origen y destino no pueden ser nulas");
        }
        if (cuenta==null){
            throw new CuentaOrigenNoExisteExcepcion("La cuenta origen no existe."); 
        }

         if (transferenciaDto.getMonto() <= 0) {
            throw new MontoMenorIgualQueCero("El monto debe ser mayor a 0");
        }
        if(cuenta.getBalance()<trans.getMonto()){
            throw new SaldoInsuficienteExcepcion("Saldo insuficiente para realizar la transferencia.");
        }

        if (cuenta2==null){
            throw new CuentaDestinoNoExisteExcepcion("La cuenta de destino no existe.");
        }
        if (transferenciaDto.getCuentaOrigen()==(transferenciaDto.getCuentaDestino())) {
            throw new CuentaOrigenyDestinoIguales("Las cuentas de origen y destino no pueden ser la misma");
        }
        if (!"PESOS".equals(trans.getTipoMoneda()) && !"DOLARES".equals(trans.getTipoMoneda())) {
           throw new TipoDeMonedaIncorrectoExcepcion("Tipo de moneda "+ trans.getTipoMoneda() + "  es incorrecto");
        }

        if(!cuenta.getMoneda().equals(cuenta2.getMoneda())){
            throw new MonedasDistintasTransferenciaExcepcion("Las monedas de las cuentas son distintas.");
        }

        if (!TipoMoneda.valueOf(trans.getTipoMoneda()).equals(cuenta.getMoneda()) || !TipoMoneda.valueOf(trans.getTipoMoneda()).equals(cuenta2.getMoneda())){
            throw new MonedaErroneaTransferenciaExcepcion("Error en la moneda seleccionada para la transferencia.");
        }
    
        //aca ya verificamos que las dos cuentas existen, solamente hay que chequear los bancos
        Cliente cliente = clienteDao.find(cuenta.getTitular(), true); //creamos los clientes para evaluar si los bancos son los mismos
        Cliente cliente2 = clienteDao.find(cuenta2.getTitular(), true);
        if (cliente.getBanco().toLowerCase().equals((cliente2.getBanco().toLowerCase()))){ //lo paso todo a minuscula asi lo compara bien
                    cuenta.setBalance(cuenta.getBalance()-trans.getMonto());
                    MovimientoDto movimientoDto = new MovimientoDto(LocalDate.now(), "DEBITO", "Transferencia Saliente", trans.getMonto());
                    cuenta2.setBalance(cuenta2.getBalance()+(trans.getMonto()-recargo(transferenciaDto))); //calculo recargo
                    MovimientoDto movimientoDto2 = new MovimientoDto(LocalDate.now(), "CREDITO", "Transferencia Entrante", (trans.getMonto()-recargo(transferenciaDto)));
                    //actualizo los saldos de las cuentas en las bases de datos con el actualizar balance creado
                    cuentaDao.updateBalance(cuenta.getNumeroCuenta(), cuenta.getBalance());
                    cuentaDao.guardarMovimiento(cuenta.getNumeroCuenta(), movimientoDto);
                    cuentaDao.updateBalance(cuenta2.getNumeroCuenta(), cuenta2.getBalance());
                    cuentaDao.guardarMovimiento(cuenta2.getNumeroCuenta(), movimientoDto2);
                    return trans;
        } else {
            //invocamos el servicio banelco ya que son de distinto banco pero las dos cuentas existen
            boolean aux = banelcoService.realizarTransferenciaDistintoBanco();
            if (aux==false){
                throw new TranferenciaBanelcoFalladaExcepcion("Transferencia a banco externo erronea. Vuelva a intentarlo");
            } else {
                cuenta.setBalance(cuenta.getBalance()-trans.getMonto());
                MovimientoDto movimientoDto = new MovimientoDto(LocalDate.now(), "DEBITO", "Transferencia Saliente", trans.getMonto());
                cuenta2.setBalance(cuenta2.getBalance()+(trans.getMonto()-recargo(transferenciaDto)));
                MovimientoDto movimientoDto2 = new MovimientoDto(LocalDate.now(), "CREDITO", "Transferencia Entrante", (trans.getMonto()-recargo(transferenciaDto)));
                cuentaDao.updateBalance(cuenta.getNumeroCuenta(), cuenta.getBalance());
                cuentaDao.guardarMovimiento(cuenta.getNumeroCuenta(), movimientoDto);
                cuentaDao.updateBalance(cuenta2.getNumeroCuenta(), cuenta2.getBalance());
                cuentaDao.guardarMovimiento(cuenta2.getNumeroCuenta(), movimientoDto2);
                
                return trans;
            }
        }
    
                    

    }

    public double recargo(TransferenciaDto trans){
        double aux = 0;
        if (trans.getMoneda().equals("PESOS")&&(trans.getMonto()>1000000)){
            aux = trans.getMonto()*0.02;
        } else {
            if (trans.getMoneda().equals("DOLARES")&&(trans.getMonto()>5000)){
                aux = trans.getMonto()*0.005;
            }
        }
        return aux;
    }
}
