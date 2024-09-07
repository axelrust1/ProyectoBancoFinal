package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.controller.MovimientoDto;
import java.time.LocalDate;
public class Movimiento {
    private LocalDate fecha;
    private String tipo;
    private String descripcionBreve;
    private double monto;

    public Movimiento(MovimientoDto movimientoDto){
        this.fecha=movimientoDto.getFecha();
        this.tipo=movimientoDto.getTipo();
        this.descripcionBreve=movimientoDto.getDescripcionBreve();
        this.monto = movimientoDto.getMonto();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    
}
