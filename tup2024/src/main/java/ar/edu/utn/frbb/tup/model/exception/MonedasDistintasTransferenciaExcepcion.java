package ar.edu.utn.frbb.tup.model.exception;

public class MonedasDistintasTransferenciaExcepcion extends Throwable{
    public MonedasDistintasTransferenciaExcepcion(){
        throw new IllegalArgumentException ("Las monedas de las cuentas son distintas.");
    }
}
