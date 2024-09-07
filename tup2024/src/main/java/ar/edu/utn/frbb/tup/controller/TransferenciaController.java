package ar.edu.utn.frbb.tup.controller;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.handler.TransferMensaje;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TranferenciaBanelcoFalladaExcepcion;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;

@RestController
@RequestMapping("/api/transfer")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService; 

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public TransferMensaje crearTransferencia(@RequestBody TransferenciaDto transferenciaDto)
    {
        try {
            transferenciaValidator.validate(transferenciaDto);
            transferenciaService.realizarTransferencia(transferenciaDto);
            return new TransferMensaje("EXITOSA", "Transferencia exitosa");
     } catch (CuentaOrigenNoExisteExcepcion | MonedasDistintasTransferenciaExcepcion | MonedaErroneaTransferenciaExcepcion | SaldoInsuficienteExcepcion | TranferenciaBanelcoFalladaExcepcion | CuentaDestinoNoExisteExcepcion excepcion) { //multicatch
            return new TransferMensaje("FALLIDA", excepcion.getMessage());
        }
    }

    @GetMapping("/{numeroCuenta}")
    public Set<Movimiento> listaMovimientos(@PathVariable long numeroCuenta){
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        return cuenta.getMovimientos();
    }
    }
