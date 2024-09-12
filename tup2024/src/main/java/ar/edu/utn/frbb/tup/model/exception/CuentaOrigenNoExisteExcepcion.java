package ar.edu.utn.frbb.tup.model.exception;

public class CuentaOrigenNoExisteExcepcion extends Exception {
    
    public CuentaOrigenNoExisteExcepcion(String message) {
        super(message);
    }
}//modificamos las excepciones con exception en ves de throwle ya que utilizamos el mensaje para mostrarlo luego de la trasnferencia