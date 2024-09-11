package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

import ar.edu.utn.frbb.tup.controller.CuentaDto;

public class Cuenta {
    private long numeroCuenta;
    LocalDate fechaCreacion;
    double balance;
    TipoCuenta tipoCuenta;
    long titular;
    TipoMoneda moneda;
    private Set<Movimiento> movimientos = new HashSet<>();
   

    public Cuenta() {
        long aux = new Random().nextLong(); 
        if (aux<0){
            numeroCuenta = aux * -1;        //nos aseguramos de que el numero de cuenta random siempre sea positivo
        } else {
            numeroCuenta=aux;
        }
        this.balance = 0;
        this.fechaCreacion = LocalDate.now();
    }

    public Cuenta(CuentaDto cuentaDto){
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDate.now();
        this.balance = 50000;
        this.titular=cuentaDto.getDniTitular(); //aplico al titular el dni
        long aux = new Random().nextLong(); 
        if (aux<0){
            this.numeroCuenta = aux * -1;        //nos aseguramos de que el numero de cuenta random siempre sea positivo
        } else {
            this.numeroCuenta=aux;
        }
    }

    public long getTitular() {
        return titular;
    }

    public void setTitular(long titular) {
        this.titular = titular;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public Set<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Set<Movimiento> movimientos){
        this.movimientos=movimientos;
    }

    public void addMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }

}
