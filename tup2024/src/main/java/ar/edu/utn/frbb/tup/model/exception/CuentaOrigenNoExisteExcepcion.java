package ar.edu.utn.frbb.tup.model.exception;

public class CuentaOrigenNoExisteExcepcion extends Throwable{
    public CuentaOrigenNoExisteExcepcion(){
        throw new IllegalArgumentException ("La cuenta origen no existe.");
    }
}
