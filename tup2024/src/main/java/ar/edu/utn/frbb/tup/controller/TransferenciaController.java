package ar.edu.utn.frbb.tup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaDestinoNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedasDistintasTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.TransferenciaService;

@RestController
@RequestMapping("/api/transfer")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService; 

    @Autowired
    private TransferenciaValidator transferenciaValidator;

    @PostMapping
    public Transferencia crearTransferencia(@RequestBody TransferenciaDto transferenciaDto) throws CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion//agregar excepciones
    {
        transferenciaValidator.validate(transferenciaDto);
        return transferenciaService.realizarTransferencia(transferenciaDto);
    }
}
