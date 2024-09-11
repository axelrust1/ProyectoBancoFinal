package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaNulaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;

@Component
public class DepositoRetiroValidator {
    public void validate(DepositoRetiroDto depositoRetiroDto) throws CuentaNulaExcepcion, MontoMenorIgualQueCero, MonedaVaciaExcepcion {
        if (depositoRetiroDto.getCuenta() == 0 || depositoRetiroDto.getCuenta() == 0) {
            throw new CuentaNulaExcepcion();
        }
        
        if (depositoRetiroDto.getMonto() <= 0) {
            throw new MontoMenorIgualQueCero();
        }

        if (depositoRetiroDto.getMoneda() == null || depositoRetiroDto.getMoneda().isEmpty()) {
            throw new MonedaVaciaExcepcion("La moneda no puede ser vacia");
        }
        
    }
}

