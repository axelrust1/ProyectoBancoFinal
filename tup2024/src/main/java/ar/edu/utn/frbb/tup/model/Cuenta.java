package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.Random;

import ar.edu.utn.frbb.tup.controller.CuentaDto;

public class Cuenta {
    private long numeroCuenta;
    LocalDateTime fechaCreacion;
    int balance;
    TipoCuenta tipoCuenta;
    long titular;
    TipoMoneda moneda;

    public Cuenta() {
        long aux = new Random().nextLong();
        if (aux<0){
            numeroCuenta = aux * -1;        //nos aseguramos de que el numero de cuenta random siempre sea positivo
        } else {
            numeroCuenta=aux;
        }
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Cuenta(CuentaDto cuentaDto){
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 0;
        this.titular=cuentaDto.getDniTitular(); //aplico al titular el dni
        this.numeroCuenta = new Random().nextLong();
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


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public int getBalance() {
        return balance;
    }

    public Cuenta setBalance(int balance) {
        this.balance = balance;
        return this;
    }

    public void debitarDeCuenta(int cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new CantidadNegativaException();
        }

        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException();
        }
        this.balance = this.balance - cantidadADebitar;
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


}
