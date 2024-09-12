package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.exception.FormatoFechaIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.TipoDePersonaIncorrectoExcepcion;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteValidator {


    public void validate(ClienteDto clienteDto) throws TipoDePersonaIncorrectoExcepcion, FormatoFechaIncorrectoException{
        validateTipoPersona(clienteDto);
        validateFormato(clienteDto);
    }
    private void validateTipoPersona(ClienteDto clienteDto) throws TipoDePersonaIncorrectoExcepcion{
        if (!"F".equals(clienteDto.getTipoPersona()) && !"J".equals(clienteDto.getTipoPersona())) {
            throw new TipoDePersonaIncorrectoExcepcion("El tipo de persona no es correcto");
        }
    }
        private void validateFormato(ClienteDto clienteDto) throws FormatoFechaIncorrectoException{
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new FormatoFechaIncorrectoException("Error en el formato de fecha");
        }
    }
}