package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.controller.TransferenciaDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteDao clienteDao;
    @Mock
    private BanelcoService banelcoService;
    
    @InjectMocks
    private TransferenciaService transferenciaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaOrigenNoExisteExcepcion() {
        when(cuentaDao.find(anyLong())).thenReturn(null); //simulo q la cuenta de origen no existe
        TransferenciaDto transferenciaDto = new TransferenciaDto(); //creo la transferencia
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(200);
        
        assertThrows(CuentaOrigenNoExisteExcepcion.class, () -> { //verifico q la excepcion se lanze
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testCuentaDestinoNoExisteExcepcion() {
        Cuenta cuentaOrigen = new Cuenta(); //configuro las cuentas de origen y simulo q la de destino no existe
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(null);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(200);

        assertThrows(CuentaDestinoNoExisteExcepcion.class, () -> { //verifico q la excepcion se lanze
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testMonedasDistintasTransferenciaExcepcion() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setMoneda(TipoMoneda.fromString("D"));
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setBalance(6000);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(200);

        assertThrows(MonedasDistintasTransferenciaExcepcion.class, () -> { //verifico q la excepcion se lanze
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testMonedaErroneaTransferenciaExcepcion() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));

        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("DOLARES");
        transferenciaDto.setMonto(200);
        assertThrows(MonedaErroneaTransferenciaExcepcion.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testSaldoInsuficienteExcepcion() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);

        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(7000);
        assertThrows(SaldoInsuficienteExcepcion.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testTranferenciaBanelcoFalladaExcepcion() {
        Cliente cliente1 = new Cliente();
        cliente1.setBanco("BancoNacion");
        cliente1.setDni(44882713);
        Cliente cliente2 = new Cliente();
        cliente2.setDni(44882712);
        cliente2.setBanco("BancoProvincia");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setTitular(cliente1.getDni());
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        cliente1.addCuenta(cuentaOrigen);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setTitular(cliente2.getDni());
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);
        cliente2.addCuenta(cuentaDestino);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(2000);

        when(clienteDao.find(eq(44882713L), eq(true))).thenReturn(cliente1);
        when(clienteDao.find(eq(44882712L), eq(true))).thenReturn(cliente2);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        when(banelcoService.realizarTransferenciaDistintoBanco()).thenReturn(false);

        assertThrows(TranferenciaBanelcoFalladaExcepcion.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testTransfenciaBanelcoExitosa() throws TipoDeMonedaIncorrectoExcepcion, MonedaVaciaExcepcion, CuentaOrigenyDestinoIguales, MontoMenorIgualQueCero, CuentasOrigenDestinoNulas, CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion, TranferenciaBanelcoFalladaExcepcion {
        Cliente cliente1 = new Cliente();
        cliente1.setBanco("BancoNacion");
        cliente1.setDni(44882713);
        Cliente cliente2 = new Cliente();
        cliente2.setDni(44882712);
        cliente2.setBanco("BancoProvincia");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setTitular(cliente1.getDni());
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        cliente1.addCuenta(cuentaOrigen);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setTitular(cliente2.getDni());
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);
        cliente2.addCuenta(cuentaDestino);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(2000);

        when(clienteDao.find(eq(44882713L), eq(true))).thenReturn(cliente1);
        when(clienteDao.find(eq(44882712L), eq(true))).thenReturn(cliente2);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        when(banelcoService.realizarTransferenciaDistintoBanco()).thenReturn(true);

        transferenciaService.realizarTransferencia(transferenciaDto);
        assertEquals(3000, cuentaOrigen.getBalance()); 
        assertEquals(12000, cuentaDestino.getBalance()); 
    }

    @Test
    public void testCuentasOrigenDestinoNulas() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(0);
        transferenciaDto.setCuentaDestino(0);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(2000);
        assertThrows(CuentasOrigenDestinoNulas.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testMontoMenorIgualQueCero() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(-2);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);

        assertThrows(MontoMenorIgualQueCero.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testCuentaOrigenyDestinoIguales() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(1L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(200);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);

        assertThrows(CuentaOrigenyDestinoIguales.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testMonedaVaciaExcepcion() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda(null);
        transferenciaDto.setMonto(500);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);

        assertThrows(MonedaVaciaExcepcion.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testTransferenciaExitosaPesos() throws TipoDeMonedaIncorrectoExcepcion, MonedaVaciaExcepcion, CuentaOrigenyDestinoIguales, MontoMenorIgualQueCero, CuentasOrigenDestinoNulas, CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion, TranferenciaBanelcoFalladaExcepcion{
        Cliente cliente1 = new Cliente();
        cliente1.setBanco("BancoNacion");
        cliente1.setDni(44882713);
        Cliente cliente2 = new Cliente();
        cliente2.setDni(44882712);
        cliente2.setBanco("BancoNacion");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        cliente1.addCuenta(cuentaOrigen);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setBalance(10000);
        cliente2.addCuenta(cuentaDestino);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("PESOS");
        transferenciaDto.setMonto(500);
        when(clienteDao.find(eq(44882713L), eq(true))).thenReturn(cliente1); //utilizadno mockito utilizamos el metodo find con el valor pasado, y usamos eq true para simular que es el titular de la cuenta
        when(clienteDao.find(eq(44882712L), eq(true))).thenReturn(cliente2);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        
        Transferencia transferencia = transferenciaService.realizarTransferencia(transferenciaDto);
        assertEquals(4500, cuentaOrigen.getBalance());
        assertEquals(10500, cuentaDestino.getBalance()); //verifico si se actualizo el balance de las cuentas
        assertNotNull(transferencia);
    }

    @Test
    public void testTransferenciaExitosaDolares() throws TipoDeMonedaIncorrectoExcepcion, MonedaVaciaExcepcion, CuentaOrigenyDestinoIguales, MontoMenorIgualQueCero, CuentasOrigenDestinoNulas, CuentaOrigenNoExisteExcepcion, CuentaDestinoNoExisteExcepcion, MonedasDistintasTransferenciaExcepcion, MonedaErroneaTransferenciaExcepcion,  SaldoInsuficienteExcepcion, TranferenciaBanelcoFalladaExcepcion{
        Cliente cliente1 = new Cliente();
        cliente1.setBanco("BancoNacion");
        cliente1.setDni(44882713);
        Cliente cliente2 = new Cliente();
        cliente2.setDni(44882712);
        cliente2.setBanco("BancoNacion");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("D"));
        cuentaOrigen.setBalance(5000);
        cliente1.addCuenta(cuentaOrigen);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setMoneda(TipoMoneda.fromString("D"));
        cuentaDestino.setBalance(10000);
        cliente2.addCuenta(cuentaDestino);

        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("DOLARES");
        transferenciaDto.setMonto(500);
        when(clienteDao.find(eq(44882713L), eq(true))).thenReturn(cliente1); //utilizadno mockito utilizamos el metodo find con el valor pasado, y usamos eq true para simular que es el titular de la cuenta
        when(clienteDao.find(eq(44882712L), eq(true))).thenReturn(cliente2);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        
        Transferencia transferencia = transferenciaService.realizarTransferencia(transferenciaDto);
        assertEquals(4500, cuentaOrigen.getBalance());
        assertEquals(10500, cuentaDestino.getBalance()); //verifico si se actualizo el balance de las cuentas
        assertNotNull(transferencia);
    }

    @Test
    public void testTipoMonedaTransferenciaIncorrecto() {
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setMoneda(TipoMoneda.fromString("P"));
        cuentaOrigen.setBalance(5000);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setMoneda(TipoMoneda.fromString("P"));
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setBalance(6000);
        when(cuentaDao.find(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(2L)).thenReturn(cuentaDestino);
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(1L);
        transferenciaDto.setCuentaDestino(2L);
        transferenciaDto.setMoneda("p");
        transferenciaDto.setMonto(200);

        assertThrows(TipoDeMonedaIncorrectoExcepcion.class, () -> { //verifico q la excepcion se lanze
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }
}
