package ar.edu.utn.frbb.tup.model.exception;

public class CuentasOrigenDestinoNulas extends Exception {
    public CuentasOrigenDestinoNulas() {
        super("Las cuentas de origen y destino no pueden ser nulas");
    }
}
