package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;

public class DepositoRetiro {

    private long cuenta;
    private double monto;
    private String tipoMoneda;

    public DepositoRetiro(DepositoRetiroDto DepositoRetirodto){
        this.cuenta = DepositoRetirodto.getCuenta();
        this.monto = DepositoRetirodto.getMonto();
        this.tipoMoneda = DepositoRetirodto.getMoneda();
    }

    public long getCuenta() {
        return cuenta;
    }

    public void setCuenta(long cuenta) {
        this.cuenta = cuenta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

}

