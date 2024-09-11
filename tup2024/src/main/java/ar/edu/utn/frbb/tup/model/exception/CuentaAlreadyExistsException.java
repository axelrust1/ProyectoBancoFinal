package ar.edu.utn.frbb.tup.model.exception;

public class CuentaAlreadyExistsException extends Exception {
    public CuentaAlreadyExistsException() {
        super("El cliente ya tiene una cuenta de este tipo");
    }
}
