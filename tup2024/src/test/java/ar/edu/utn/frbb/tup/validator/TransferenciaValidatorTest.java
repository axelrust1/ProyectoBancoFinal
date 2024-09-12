package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenyDestinoIguales;
import ar.edu.utn.frbb.tup.model.exception.CuentasOrigenDestinoNulas;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TransferenciaValidatorTest {

    @Mock
    private TransferenciaValidator transferenciaValidator;

    @BeforeEach
    public void setUp() {
        transferenciaValidator = new TransferenciaValidator();
    }

    @Test
    public void testCuentasOrigenDestinoNulas() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(0);  // Cuenta origen nula
        transferenciaDto.setCuentaDestino(0); // Cuenta destino nula

        assertThrows(CuentasOrigenDestinoNulas.class, () -> {
            transferenciaValidator.validate(transferenciaDto);
        });
    }

    @Test
    public void testMontoMenorOIgualQueCero() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(44882713);
        transferenciaDto.setCuentaDestino(30674123);
        transferenciaDto.setMoneda("P");
        transferenciaDto.setMonto(0);  // Monto inválido

        assertThrows(MontoMenorIgualQueCero.class, () -> {
            transferenciaValidator.validate(transferenciaDto);
        });
    }

    @Test
    public void testCuentasOrigenyDestinoIguales() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(44882713);
        transferenciaDto.setCuentaDestino(44882713); // Cuentas iguales
        transferenciaDto.setMonto(1000.00);
        transferenciaDto.setMoneda("P");

        assertThrows(CuentaOrigenyDestinoIguales.class, () -> {
            transferenciaValidator.validate(transferenciaDto);
        });
    }

    @Test
    public void testMonedaVaciaException() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(44882713);
        transferenciaDto.setCuentaDestino(44887432);
        transferenciaDto.setMonto(1000.00);
        transferenciaDto.setMoneda("");  // Moneda vacía

        assertThrows(MonedaVaciaExcepcion.class, () -> {
            transferenciaValidator.validate(transferenciaDto);
        });
    }

    @Test
    public void testValidacionExitosa() throws CuentasOrigenDestinoNulas, MontoMenorIgualQueCero, CuentaOrigenyDestinoIguales, MonedaVaciaExcepcion {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(44882713);
        transferenciaDto.setCuentaDestino(44678552);
        transferenciaDto.setMonto(1000.00);
        transferenciaDto.setMoneda("P");

        assertDoesNotThrow(() -> transferenciaValidator.validate(transferenciaDto));
    }
}

