package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenyDestinoIguales;
import ar.edu.utn.frbb.tup.model.exception.CuentasOrigenDestinoNulas;
import ar.edu.utn.frbb.tup.model.exception.MonedaVacia;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;

@Component
public class TransferenciaValidator {
    public void validate(TransferenciaDto transferenciaDto) throws CuentasOrigenDestinoNulas, MontoMenorIgualQueCero, CuentaOrigenyDestinoIguales, MonedaVacia {
        if (transferenciaDto.getCuentaOrigen() == 0 || transferenciaDto.getCuentaDestino() == 0) {
            throw new CuentasOrigenDestinoNulas();
        }
        
        if (transferenciaDto.getMonto() <= 0) {
            throw new MontoMenorIgualQueCero();
        }

        if (transferenciaDto.getCuentaOrigen()==(transferenciaDto.getCuentaDestino())) {
            throw new CuentaOrigenyDestinoIguales();
        }

        if (transferenciaDto.getMoneda() == null || transferenciaDto.getMoneda().isEmpty()) {
            throw new MonedaVacia();
        }
        
    }
}
