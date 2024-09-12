package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.TipoDeCuentaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CuentaValidatorTest {

    @Mock
    private CuentaValidator cuentaValidator;

    @BeforeEach
    void setUp() {
        cuentaValidator = new CuentaValidator();
    }

    @Test
    void testValidacionCompleta() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    void testTipoCuentaErroneayMonedaCorrecta() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("L");
        cuentaDto.setMoneda("P");

        assertThrows(TipoDeCuentaIncorrectoExcepcion.class, () -> {
            cuentaValidator.validate(cuentaDto);
        });

    }

    @Test
    void testTipoCuentaCorrectayMonedaErronea() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("A"); 

        assertThrows(TipoDeMonedaIncorrectoExcepcion.class, () -> {
            cuentaValidator.validate(cuentaDto);
        });

    }

    @Test
    void testValidacionCompletaConCuentaCorrienteEnPesos() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    void testValidacionCompletaConCuentaAhorroEnDolares() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("D");

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    void testValidacionCompletaConCuentaAhorroEnPesos() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

}
