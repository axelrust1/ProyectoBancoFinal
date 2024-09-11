package ar.edu.utn.frbb.tup.model.exception;

public class ClienteNoExisteException extends Exception {
    public ClienteNoExisteException(){
        super("El cliente no existe");
    }
}
