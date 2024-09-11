package ar.edu.utn.frbb.tup.model.exception;
import ar.edu.utn.frbb.tup.model.Cliente;
public class ClienteAlreadyExistsException extends Exception {
    public ClienteAlreadyExistsException(Cliente cliente) {
        super("Ya existe un cliente con DNI " + cliente.getDni());
        //throw new IllegalArgumentException("La persona con este dni ya existe");
        //modifique la excepcion para que en postman arroje el error

    }
}
