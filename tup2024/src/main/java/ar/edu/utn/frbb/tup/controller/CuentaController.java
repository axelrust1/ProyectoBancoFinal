package ar.edu.utn.frbb.tup.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExisteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoEncontradaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.service.CuentaService;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService; 

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) throws TipoDeCuentaIncorrectoExcepcion, TipoDeMonedaIncorrectoExcepcion, ClienteNoExisteException, CuentaAlreadyExistsException, CuentaAlreadyExistsException, CuentaNoSoportadaException{
        cuentaValidator.validate(cuentaDto);
        return cuentaService.darDeAltaCuenta(cuentaDto);
    }

    @GetMapping ("/{numeroCuenta}")
    public Cuenta buscarCuenta(@PathVariable long numeroCuenta) throws CuentaNoEncontradaExcepcion {
        return cuentaService.find(numeroCuenta);
    }

    @GetMapping ("/{numeroCuenta}/transacciones")
    public MovimientoMensajeDto listaMovimientos(@PathVariable long numeroCuenta) throws CuentaNoEncontradaExcepcion{
        MovimientoMensajeDto movimiento = new MovimientoMensajeDto();
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimiento.setTransacciones(cuenta.getMovimientos());
        return movimiento;
    }
}
