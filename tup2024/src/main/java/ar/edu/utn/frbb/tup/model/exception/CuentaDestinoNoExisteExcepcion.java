package ar.edu.utn.frbb.tup.model.exception;

public class CuentaDestinoNoExisteExcepcion extends Exception {
    public CuentaDestinoNoExisteExcepcion(){
        super("La cuenta de destino no existe.");
    }
}
