package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaService {
    @Autowired
    CuentaDao cuentaDao;
    @Autowired
    TransferenciaValidator transValidator;
    @Autowired
    CuentaService cuentaService;

    public Transferencia realizarTransferencia(TransferenciaDto transferenciaDto) throws CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion {
        Cuenta cuenta = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuenta2 = cuentaDao.find(transferenciaDto.getCuentaDestino());
        Transferencia trans = new Transferencia(transferenciaDto);

        if (cuenta==null){
            throw new CuentaOrigenNoExisteExcepcion(); 
        }
        
        if (cuenta2==null){
            throw new IllegalArgumentException("esto hay que sacarlo es para que no tire error");
                //aca se invoca al servicio banelco
        }

        if(cuenta.getBalance()<trans.getMonto()){
            throw new SaldoInsuficienteExcepcion();
        }

        if(!cuenta.getMoneda().equals(cuenta2.getMoneda())){
            throw new MonedasDistintasTransferenciaExcepcion();
        }
            if (!(TipoMoneda.valueOf(trans.getTipoMoneda()).equals(cuenta.getMoneda()))){ //CONVIERTO EL STRING EN UN ENUM PARA LA COMPARACION CON LA CUENTA
                throw new MonedaErroneaTransferenciaExcepcion();
            } 
    
                    cuenta.setBalance(cuenta.getBalance()-trans.getMonto());
                    cuenta2.setBalance(cuenta2.getBalance()+(trans.getMonto()-recargo(transferenciaDto))); //calculo recargo
                    //actualizo los saldos de las cuentas en las bases de datos con el actualizar balance creado
                    cuentaDao.updateBalance(cuenta.getNumeroCuenta(), cuenta.getBalance());
                    cuentaDao.updateBalance(cuenta2.getNumeroCuenta(), cuenta2.getBalance());
                    return trans;

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
