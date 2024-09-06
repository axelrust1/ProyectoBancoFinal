package ar.edu.utn.frbb.tup.model.exception;

public class MonedaErroneaTransferenciaExcepcion extends Throwable {
    public MonedaErroneaTransferenciaExcepcion(){
        throw new IllegalArgumentException ("Error en la moneda seleccionada para la transferencia.");
    }
}
