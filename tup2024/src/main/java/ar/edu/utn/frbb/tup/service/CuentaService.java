package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExisteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();
    ClienteDao clienteDao;

    @Autowired
    ClienteService clienteService;

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws ClienteNoExisteException, CuentaAlreadyExistsException, CuentaAlreadyExistsException, CuentaNoSoportadaException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        if(cuentaDao.find(cuenta.getTitular()) != null) {
            throw new CuentaAlreadyExistsException();
        }

       if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new CuentaNoSoportadaException(cuenta);
       }
        clienteService.agregarCuenta(cuenta, cuentaDto.getDniTitular());
        cuentaDao.save(cuenta);
        return cuenta;
    }

   
    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS) || (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES));
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}

