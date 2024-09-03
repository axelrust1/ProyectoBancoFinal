package ar.edu.utn.frbb.tup.model.exception;

public class ClienteAlreadyExistsException extends Throwable {
    public ClienteAlreadyExistsException(String message) {
        super(message);
        //throw new IllegalArgumentException("La persona con este dni ya existe");
        //modifique la excepcion para que en postman arroje el error

    }
}
