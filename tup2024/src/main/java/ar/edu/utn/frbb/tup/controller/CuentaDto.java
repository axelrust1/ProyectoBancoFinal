package ar.edu.utn.frbb.tup.controller;
import java.util.List;
public class CuentaDto {

    private String tipoCuenta;
    private long dniTitular;
    private String moneda;
    private List<MovimientoDto> movimientos; //agrego una lista con los movimientos de cada cuenta

    public long getDniTitular() {
        return dniTitular;
    }

    public String getMoneda() {
        return moneda;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public List<MovimientoDto> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoDto> movimientos) {
        this.movimientos = movimientos;
    }
}
