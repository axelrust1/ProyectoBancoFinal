package ar.edu.utn.frbb.tup.model.exception;

public class SaldoInsuficienteExcepcion extends Exception {
    public SaldoInsuficienteExcepcion(){
        super("Saldo insuficiente para realizar la transferencia.");
    }
}
