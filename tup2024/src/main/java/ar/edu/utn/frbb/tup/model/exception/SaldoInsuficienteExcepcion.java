package ar.edu.utn.frbb.tup.model.exception;

public class SaldoInsuficienteExcepcion extends Throwable {
    public SaldoInsuficienteExcepcion(){
        throw new IllegalArgumentException ("Saldo insuficiente para realizar la transferencia.");
    }
}
