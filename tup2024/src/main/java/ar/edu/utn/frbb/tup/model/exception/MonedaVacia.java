package ar.edu.utn.frbb.tup.model.exception;

public class MonedaVacia extends Exception {
    public MonedaVacia(){
        super ("La moneda no puede ser vacia");
    }
}
