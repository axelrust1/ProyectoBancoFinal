package ar.edu.utn.frbb.tup.model.exception;

public class CuentaAlreadyExistsException extends Throwable {
    public CuentaAlreadyExistsException() {
        throw new IllegalArgumentException("El cliente ya tiene una cuenta de este tipo");
    }
}
