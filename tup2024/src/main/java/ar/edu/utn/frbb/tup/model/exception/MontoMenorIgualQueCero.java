package ar.edu.utn.frbb.tup.model.exception;
public class MontoMenorIgualQueCero extends Exception {
    public MontoMenorIgualQueCero(){
        super ("El monto debe ser mayor a 0");
    }
}
