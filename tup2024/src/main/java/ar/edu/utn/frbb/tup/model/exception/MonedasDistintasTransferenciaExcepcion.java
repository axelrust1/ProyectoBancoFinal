package ar.edu.utn.frbb.tup.model.exception;

public class MonedasDistintasTransferenciaExcepcion extends Exception{
    public MonedasDistintasTransferenciaExcepcion(String message){
        super (message);
    }
} //modificamos las excepciones con exception en ves de throwle ya que utilizamos el mensaje para mostrarlo luego de la trasnferencia