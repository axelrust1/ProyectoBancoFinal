package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNoExisteException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento("2010-05-30");
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setFechaNacimiento("2003-05-30");
        clienteDto.setDni(44882713);
        clienteDto.setTipoPersona("F");
        Cliente cliente = clienteService.darDeAltaCliente(clienteDto);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(44882713);
        clienteDto.setNombre("Axel");
        clienteDto.setApellido("Rust");
        clienteDto.setFechaNacimiento("2003-05-30");
        clienteDto.setTipoPersona("F");

        when(clienteDao.find(44882713, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws ClienteNoExisteException, CuentaAlreadyExistsException {
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
        assertEquals(pepeRino.getDni(), cuenta.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero

    }

    @Test
    public void testAgregarCuentaAhorroYDolaresAClienteSucess()throws ClienteNoExisteException, CuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino.getDni(), cuenta.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero

    }

    @Test
    public void testAgregarCuentaCorrientePesosAClienteSucess()throws ClienteNoExisteException, CuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino.getDni(), cuenta.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero

    }
    @Test
    public void testAgregarCuentaAClienteDuplicada() throws ClienteNoExisteException, CuentaAlreadyExistsException {
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

        assertThrows(CuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano.getDni(), cuenta.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero

    }

    @Test
public void testAgregarCajaAhorroYCuentaCorriente() throws ClienteNoExisteException, CuentaAlreadyExistsException {
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
    assertEquals(peperino.getDni(), cajaAhorro.getTitular()); //comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero
    assertEquals(peperino.getDni(), cuentaCorriente.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero
}

@Test
public void testAgregarPesosYDolares() throws ClienteNoExisteException, CuentaAlreadyExistsException {
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
    assertEquals(peperino.getDni(), cajaAhorro.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero
    assertEquals(peperino.getDni(), cajaAhorroDolares.getTitular());//comparo el dni ya que cambiamos el titular solo por el numero del dni y no el objeto entero
}

@Test
public void testBuscarDniExitoso()  throws ClienteNoExisteException{
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
    public void testBuscarPorDniFallo() throws ClienteNoExisteException{
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        assertThrows(ClienteNoExisteException.class, () -> clienteService.buscarClientePorDni(123456789));
    }
}