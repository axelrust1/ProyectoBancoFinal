package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.FormatoFechaIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.TipoDePersonaIncorrectoExcepcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ClienteValidatorTest {

    private ClienteValidator clienteValidator;

    @BeforeEach
    void setUp() {
        clienteValidator = new ClienteValidator();
    }

    @Test
    void testValidacionCompleta() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("F");
        clienteDto.setFechaNacimiento("2003-05-30");

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void testPersonaInvalida() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("D"); //tipo persoan invalido

        TipoDePersonaIncorrectoExcepcion exception = assertThrows(TipoDePersonaIncorrectoExcepcion.class, () -> {
            clienteValidator.validate(clienteDto);
        });
        assertEquals("El tipo de persona no es correcto", exception.getMessage());//verifico mensaje 
    }

    @Test
    void testPersonaValidayFechaInvalida() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("F");
        clienteDto.setFechaNacimiento("30-05-2003"); //formato incorrecto

        FormatoFechaIncorrectoException exception = assertThrows(FormatoFechaIncorrectoException.class, () -> {
            clienteValidator.validate(clienteDto);
        });

        assertEquals("Error en el formato de fecha", exception.getMessage());//verifico mensaje 
    }

    @Test
    void ttestValidacionCompletaJuridica() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("J");
        clienteDto.setFechaNacimiento("2003-05-30");

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }
}
