package ar.edu.utn.frbb.tup.model.exception;

public class NoAlcanzaException extends Exception {
    public NoAlcanzaException() {
        super("No hay saldo suficiente para realizar el retiro.");
    }
}
