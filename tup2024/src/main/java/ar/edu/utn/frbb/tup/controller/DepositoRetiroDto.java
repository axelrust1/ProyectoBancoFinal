package ar.edu.utn.frbb.tup.controller;

public class DepositoRetiroDto {
    private long cuenta;
    double monto;
    String moneda;

    public long getCuenta(){
        return cuenta;
    }

    public void setCuenta(long cuenta){
        this.cuenta=cuenta;
    }
    public double getMonto(){
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    
}