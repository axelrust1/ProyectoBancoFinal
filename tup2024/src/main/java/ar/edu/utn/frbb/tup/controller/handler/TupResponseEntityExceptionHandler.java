package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.FormatoFechaIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExisteException;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDePersonaIncorrectoExcepcion;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TupResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {ClienteNoExisteException.class,TipoDePersonaIncorrectoExcepcion.class,FormatoFechaIncorrectoException.class,  TipoDeMonedaIncorrectoExcepcion.class, ClienteAlreadyExistsException.class, CuentaAlreadyExistsException.class, CuentaNoSoportadaException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleMateriaNotFound(
            Exception ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String exceptionMessage = ex.getMessage();
        CustomApiError error = new CustomApiError();
        error.setErrorCode(1234);
        error.setErrorMessage(exceptionMessage);
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }



    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }

        return new ResponseEntity(body, headers, status);
    }

}