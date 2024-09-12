package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.validator.DepositoRetiroValidator;
import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaNulaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositoRetiroValidatorTest {

    private DepositoRetiroValidator depositoRetiroValidator;

    @BeforeEach
    public void setUp() {
        depositoRetiroValidator = new DepositoRetiroValidator();
    }

    @Test
    public void testCuentaNula() {
        DepositoRetiroDto depositoRetiroDto = new DepositoRetiroDto();
        depositoRetiroDto.setCuenta(0);

        assertThrows(CuentaNulaExcepcion.class, () -> {
            depositoRetiroValidator.validate(depositoRetiroDto);
        });
    }

    @Test
    public void testMontoMenorOIgualQueCero() {
        DepositoRetiroDto depositoRetiroDto = new DepositoRetiroDto();
        depositoRetiroDto.setCuenta(44882713);
        depositoRetiroDto.setMoneda("P");
        depositoRetiroDto.setMonto(0);

        assertThrows(MontoMenorIgualQueCero.class, () -> {
            depositoRetiroValidator.validate(depositoRetiroDto);
        });
    }

    @Test
    public void testMonedaVacia() {
        DepositoRetiroDto depositoRetiroDto = new DepositoRetiroDto();
        depositoRetiroDto.setCuenta(44882713);
        depositoRetiroDto.setMonto(1000.00);
        depositoRetiroDto.setMoneda("");  

        assertThrows(MonedaVaciaExcepcion.class, () -> {
            depositoRetiroValidator.validate(depositoRetiroDto);
        });
    }

    @Test
    public void testValidacionExitosa() throws CuentaNulaExcepcion, MontoMenorIgualQueCero, MonedaVaciaExcepcion{
        DepositoRetiroDto depositoRetiroDto = new DepositoRetiroDto();
        depositoRetiroDto.setCuenta(44882713);
        depositoRetiroDto.setMonto(1000.00);
        depositoRetiroDto.setMoneda("P");

        depositoRetiroValidator.validate(depositoRetiroDto);
    }
}

