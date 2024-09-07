package ar.edu.utn.frbb.tup.controller.handler;

public class TransferMensaje {
    private String estado;
    private String mensaje;

    public TransferMensaje(String estado, String mensaje){
        this.estado=estado;
        this.mensaje=mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
