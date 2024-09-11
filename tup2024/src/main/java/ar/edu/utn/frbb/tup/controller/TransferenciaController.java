package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.validator.DepositoRetiroValidator;
import ar.edu.utn.frbb.tup.controller.handler.TransferMensaje;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaNulaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenyDestinoIguales;
import ar.edu.utn.frbb.tup.model.exception.CuentasOrigenDestinoNulas;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TranferenciaBanelcoFalladaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.service.DepositoRetiroService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;

@RestController
@RequestMapping("/api")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService; 

    @Autowired
    private DepositoRetiroService depositoRetiroService;

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    @Autowired
    private DepositoRetiroValidator depositoRetiroValidator;

    @PostMapping ("/transfer")
    public TransferMensaje crearTransferencia(@RequestBody TransferenciaDto transferenciaDto)
    {
        try {
            transferenciaValidator.validate(transferenciaDto);
            transferenciaService.realizarTransferencia(transferenciaDto);
            return new TransferMensaje("EXITOSA", "Transferencia exitosa");
     } catch (TipoDeMonedaIncorrectoExcepcion | CuentaOrigenNoExisteExcepcion | MonedasDistintasTransferenciaExcepcion | MonedaErroneaTransferenciaExcepcion | SaldoInsuficienteExcepcion | TranferenciaBanelcoFalladaExcepcion | CuentaDestinoNoExisteExcepcion | CuentasOrigenDestinoNulas | MontoMenorIgualQueCero | CuentaOrigenyDestinoIguales | MonedaVaciaExcepcion excepcion) { //multicatch
            return new TransferMensaje("FALLIDA", excepcion.getMessage());
        }
    }

    @PostMapping("/deposito")
    public TransferMensaje hacerDeposito(@RequestBody DepositoRetiroDto depositoRetiroDto){
        try{
            depositoRetiroValidator.validate(depositoRetiroDto);
            depositoRetiroService.realizarDeposito(depositoRetiroDto);
            return new TransferMensaje("EXITOSO", "Deposito Exitoso");
        } catch (CuentaOrigenNoExisteExcepcion | MonedaErroneaTransferenciaExcepcion | CuentaNulaExcepcion | MonedaVaciaExcepcion | MontoMenorIgualQueCero excepcion ){
            return new TransferMensaje("FALLIDA", excepcion.getMessage());
        }
    }

    @PostMapping("/retiro")
    public TransferMensaje hacerRetiro(@RequestBody DepositoRetiroDto depositoRetiroDto){
        try{
            depositoRetiroValidator.validate(depositoRetiroDto);
            depositoRetiroService.realizarRetiro(depositoRetiroDto);
            return new TransferMensaje("EXITOSO", "Retiro Exitoso");
        } catch (NoAlcanzaException | CuentaOrigenNoExisteExcepcion | MonedaErroneaTransferenciaExcepcion | CuentaNulaExcepcion | MonedaVaciaExcepcion | MontoMenorIgualQueCero excepcion ){
            return new TransferMensaje("FALLIDA", excepcion.getMessage());
        }
    }
    }
