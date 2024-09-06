package ar.edu.utn.frbb.tup.model.exception;
import ar.edu.utn.frbb.tup.model.Cliente;
public class ClienteAlreadyExistsException extends Throwable {
    public ClienteAlreadyExistsException(Cliente cliente) {
        throw new IllegalArgumentException("Ya existe un cliente con DNI " + cliente.getDni());
        //throw new IllegalArgumentException("La persona con este dni ya existe");
        //modifique la excepcion para que en postman arroje el error

    }
}
