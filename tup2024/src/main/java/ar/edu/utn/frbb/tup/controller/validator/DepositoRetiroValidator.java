package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaNulaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;

@Component
public class DepositoRetiroValidator {
    public void validate(DepositoRetiroDto depositoRetiroDto) throws CuentaNulaExcepcion, MontoMenorIgualQueCero, MonedaVaciaExcepcion {
        validateCuentaNula(depositoRetiroDto);
        validateMoneda(depositoRetiroDto);
        validateMonto(depositoRetiroDto);
        
    }
    private void validateCuentaNula(DepositoRetiroDto depositoRetiroDto) throws CuentaNulaExcepcion{
        if (depositoRetiroDto.getCuenta() == 0 || depositoRetiroDto.getCuenta() == 0) {
            throw new CuentaNulaExcepcion("La cuenta no puede ser nula.");
        }
    }
    private void validateMonto(DepositoRetiroDto depositoRetiroDto) throws MontoMenorIgualQueCero{
        if (depositoRetiroDto.getMonto() <= 0) {
            throw new MontoMenorIgualQueCero("El monto debe ser mayor a 0");
        }
    }
    private void validateMoneda(DepositoRetiroDto depositoRetiroDto) throws MonedaVaciaExcepcion{
        if (depositoRetiroDto.getMoneda() == null || depositoRetiroDto.getMoneda().isEmpty()) {
            throw new MonedaVaciaExcepcion("La moneda no puede ser vacia");
        }
    }
}

