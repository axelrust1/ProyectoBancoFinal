package ar.edu.utn.frbb.tup.model.exception;

public class CuentaDestinoNoExisteExcepcion extends Throwable{
    public CuentaDestinoNoExisteExcepcion(){
        throw new IllegalArgumentException ("La cuenta destino no existe.");
    }
}
