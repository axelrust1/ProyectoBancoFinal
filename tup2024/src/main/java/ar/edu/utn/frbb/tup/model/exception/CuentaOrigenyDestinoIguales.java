package ar.edu.utn.frbb.tup.model.exception;

public class CuentaOrigenyDestinoIguales extends Exception {
    public CuentaOrigenyDestinoIguales() {
        super("Las cuentas de origen y destino no pueden ser la misma");
    }
}
