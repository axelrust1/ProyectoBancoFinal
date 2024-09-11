package ar.edu.utn.frbb.tup.model.exception;

public class MonedaVaciaExcepcion extends Exception {
    public MonedaVaciaExcepcion(){
        super("La moneda no puede ser vacia");
    }
}
