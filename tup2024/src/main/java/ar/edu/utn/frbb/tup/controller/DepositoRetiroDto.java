package ar.edu.utn.frbb.tup.controller;

public class DepositoRetiroDto {
    private long cuenta;
    int monto;
    String moneda;

    public long getCuenta(){
        return cuenta;
    }

    public void setCuenta(){
        this.cuenta=cuenta;
    }
    public int getMonto(){
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    
}