package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1978,3,25));
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }



    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }

    @Test
public void testAgregarCajaAhorroYCuentaCorriente() throws TipoCuentaAlreadyExistsException {
    Cliente peperino = new Cliente();
    peperino.setDni(26456439);
    peperino.setNombre("Pepe");
    peperino.setApellido("Rino");
    peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
    peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

    Cuenta cajaAhorro = new Cuenta();
    cajaAhorro.setMoneda(TipoMoneda.PESOS);
    cajaAhorro.setBalance(500000);
    cajaAhorro.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

    Cuenta cuentaCorriente = new Cuenta();
    cuentaCorriente.setMoneda(TipoMoneda.PESOS);
    cuentaCorriente.setBalance(1000000);
    cuentaCorriente.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

    when(clienteDao.find(26456439, true)).thenReturn(peperino);

    clienteService.agregarCuenta(cajaAhorro, peperino.getDni());
    clienteService.agregarCuenta(cuentaCorriente, peperino.getDni());

    verify(clienteDao, times(2)).save(peperino);

    assertEquals(2, peperino.getCuentas().size());
    assertTrue(peperino.getCuentas().contains(cajaAhorro));
    assertTrue(peperino.getCuentas().contains(cuentaCorriente));
    assertEquals(peperino, cajaAhorro.getTitular());
    assertEquals(peperino, cuentaCorriente.getTitular());
}

@Test
public void testAgregarPesosYDolares() throws TipoCuentaAlreadyExistsException {
    Cliente peperino = new Cliente();
    peperino.setDni(26456439);
    peperino.setNombre("Pepe");
    peperino.setApellido("Rino");
    peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
    peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

    Cuenta cajaAhorro = new Cuenta();
    cajaAhorro.setMoneda(TipoMoneda.PESOS);
    cajaAhorro.setBalance(500000);
    cajaAhorro.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

    Cuenta cajaAhorroDolares = new Cuenta();
    cajaAhorroDolares.setMoneda(TipoMoneda.DOLARES);
    cajaAhorroDolares.setBalance(10000);
    cajaAhorroDolares.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

    when(clienteDao.find(26456439, true)).thenReturn(peperino);

    clienteService.agregarCuenta(cajaAhorro, peperino.getDni());
    clienteService.agregarCuenta(cajaAhorroDolares, peperino.getDni());

    verify(clienteDao, times(2)).save(peperino);

    assertEquals(2, peperino.getCuentas().size());
    assertTrue(peperino.getCuentas().contains(cajaAhorro));
    assertTrue(peperino.getCuentas().contains(cajaAhorroDolares));
    assertEquals(peperino, cajaAhorro.getTitular());
    assertEquals(peperino, cajaAhorroDolares.getTitular());
}

@Test
public void testBuscarDniExitoso() {
    Cliente peperino = new Cliente();
    peperino.setDni(26456439);
    peperino.setNombre("Pepe");
    peperino.setApellido("Rino");
    peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
    peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

    when(clienteDao.find(26456439, true)).thenReturn(peperino);

    Cliente clienteEncontrado = clienteService.buscarClientePorDni(26456439);

    assertEquals(peperino, clienteEncontrado);
    verify(clienteDao, times(1)).find(26456439, true);
}

@Test
public void testBuscarDniFallido() {
    when(clienteDao.find(30673123, true)).thenReturn(null);

    assertNull(clienteService.buscarClientePorDni(30673123));
    verify(clienteDao, times(1)).find(30673123, true);
}
}