package ar.edu.utn.frbb.tup.model.exception;

public class CuentaNulaExcepcion extends Exception {
    public CuentaNulaExcepcion(){
        super("La cuenta no puede ser nula.");
    }
}
