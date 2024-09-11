package ar.edu.utn.frbb.tup.controller;

import java.util.Set;
import ar.edu.utn.frbb.tup.model.Movimiento;

public class MovimientoMensajeDto {
    private Long numeroCuenta;
    private Set<Movimiento> transacciones;

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    public Set<Movimiento> getTransacciones() {
        return transacciones;
    }
    public void setTransacciones(Set<Movimiento> transacciones) {
        this.transacciones = transacciones;
    }
}
