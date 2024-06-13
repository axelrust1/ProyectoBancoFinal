package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCuentaExistente() throws CuentaAlreadyExistsException{
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(29857643);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        when(cuentaDao.find(29857643)).thenReturn(cuenta);

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 29857643));
    }

    @Test
    void testCuentaNoSoportada() throws CuentaNoSoportadaException{
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(29857643);
        cuenta.setMoneda(TipoMoneda.DOLARES);
        cuenta.setBalance(2000);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        assertThrows(CuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 29857643));
    }

    @Test
    void testClienteYaTieneCuenta() throws TipoCuentaAlreadyExistsException{
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(29857643);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(2000);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        
        when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(null);
        doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(cuenta, peperino.getDni());
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta, peperino.getDni()));
    }

    @Test
    void testCuentaCreadaExitosamente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, CuentaNoSoportadaException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(29857643);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(2000);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        cuentaService.darDeAltaCuenta(cuenta, 29857643);

        verify (clienteService, times(1)).agregarCuenta(cuenta, 29857643);
        verify (cuentaDao, times(1)).save(cuenta);
    }
}