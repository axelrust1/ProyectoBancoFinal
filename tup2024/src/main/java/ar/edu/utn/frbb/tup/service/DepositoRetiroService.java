package ar.edu.utn.frbb.tup.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;
import ar.edu.utn.frbb.tup.controller.MovimientoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.DepositoRetiro;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TranferenciaBanelcoFalladaExcepcion;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

@Component
public class DepositoRetiroService {
    @Autowired
    CuentaDao cuentaDao;
    @Autowired
    CuentaService cuentaService;
    @Autowired
    ClienteDao clienteDao;

    public DepositoRetiro realizarDeposito(DepositoRetiroDto DepositoRetirodto) throws CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
        Cuenta cuenta = cuentaDao.find(DepositoRetirodto.getCuenta()); //clono la cuenta en un tipo cuenta nuevo
        DepositoRetiro deposito = new DepositoRetiro(DepositoRetirodto); //creo un nuevo deposito que sera el que retorna
        if (cuenta==null){
            throw new CuentaOrigenNoExisteExcepcion(); //excepcion por si no exixste la cuenta
        }
        if (!(TipoMoneda.valueOf(deposito.getTipoMoneda()).equals(cuenta.getMoneda()))){ //CONVIERTO EL STRING EN UN ENUM PARA LA COMPARACION CON LA CUENTA
                throw new MonedaErroneaTransferenciaExcepcion(); //excepcion por si es distinta moneda
        }
        
        cuenta.setBalance(cuenta.getBalance()+deposito.getMonto()); //si todo sale bien seteamos el balance de la cuenta creada aca
        MovimientoDto movimientoDto = new MovimientoDto(LocalDate.now(), "Credito", "Deposito", deposito.getMonto()); //creamos el movimiento para guardarlo
        cuentaDao.updateBalance(cuenta.getNumeroCuenta(), cuenta.getBalance()); //actualizamos el balance en la cuenta original
        cuentaDao.guardarMovimiento(cuenta.getNumeroCuenta(), movimientoDto); //guardamos el movimiento
        return deposito;
    }

    public DepositoRetiro realizarRetiro(DepositoRetiroDto DepositoRetirodto) throws NoAlcanzaException, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
        Cuenta cuenta = cuentaDao.find(DepositoRetirodto.getCuenta());
        DepositoRetiro retiro = new DepositoRetiro(DepositoRetirodto);
        if (cuenta==null){
            throw new CuentaOrigenNoExisteExcepcion(); 
        }
        if (!(TipoMoneda.valueOf(retiro.getTipoMoneda()).equals(cuenta.getMoneda()))){ //CONVIERTO EL STRING EN UN ENUM PARA LA COMPARACION CON LA CUENTA
                throw new MonedaErroneaTransferenciaExcepcion();
        }
        if (cuenta.getBalance()<retiro.getMonto()){
            throw new NoAlcanzaException(); //verifico si el monto alcanza
        }

        cuenta.setBalance(cuenta.getBalance()-retiro.getMonto());
        MovimientoDto movimientoDto = new MovimientoDto(LocalDate.now(), "DEBITO", "Retiro", retiro.getMonto());
        cuentaDao.updateBalance(cuenta.getNumeroCuenta(), cuenta.getBalance());
        cuentaDao.guardarMovimiento(cuenta.getNumeroCuenta(), movimientoDto);
        return retiro;
    }
}
