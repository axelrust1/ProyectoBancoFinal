package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;

@Component
public class TransferenciaValidator {
    public void validate(TransferenciaDto transferenciaDto) {
        if (transferenciaDto.getCuentaOrigen() == 0 || transferenciaDto.getCuentaDestino() == 0) {
            throw new IllegalArgumentException("Las cuentas de origen y destino no pueden ser nulas.");
        }

        if (transferenciaDto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto de la transferencia debe ser mayor que cero.");
        }

        if (transferenciaDto.getCuentaOrigen()==(transferenciaDto.getCuentaDestino())) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser las mismas.");
        }

        if (transferenciaDto.getMoneda() == null || transferenciaDto.getMoneda().isEmpty()) {
            throw new IllegalArgumentException("La moneda no puede estar vacía.");
        }
        
    }
}
