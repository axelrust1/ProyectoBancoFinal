package ar.edu.utn.frbb.tup.model.exception;

public class TranferenciaBanelcoFalladaExcepcion extends Exception{
    public TranferenciaBanelcoFalladaExcepcion(){
        super("Transferencia a banco externo erronea. Vuelva a intentarlo");
    }
}
