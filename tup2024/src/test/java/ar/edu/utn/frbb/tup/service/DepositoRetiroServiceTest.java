package ar.edu.utn.frbb.tup.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.controller.DepositoRetiroDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaNulaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.CuentaOrigenNoExisteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaErroneaTransferenciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MonedaVaciaExcepcion;
import ar.edu.utn.frbb.tup.model.exception.MontoMenorIgualQueCero;
import ar.edu.utn.frbb.tup.model.exception.SaldoInsuficienteExcepcion;
import ar.edu.utn.frbb.tup.model.exception.TipoDeMonedaIncorrectoExcepcion;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepositoRetiroServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private DepositoRetiroService depositoRetiroService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCuentaNoExisteDeposito() {
        when(cuentaDao.find(1L)).thenReturn(null);//la cuenta no existe, por lo tanto devuelve null

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(1000);
        depositoDto.setMoneda("PESOS");

        assertThrows(CuentaOrigenNoExisteExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testRealizarDepositoExitoso() throws MontoMenorIgualQueCero, MonedaVaciaExcepcion, TipoDeMonedaIncorrectoExcepcion, CuentaNulaExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.PESOS);
        when(cuentaDao.find(1L)).thenReturn(cuenta);

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("PESOS");

        depositoRetiroService.realizarDeposito(depositoDto);

        assertEquals(7000, cuenta.getBalance());
        verify(cuentaDao).updateBalance(1L, 7000);
        verify(cuentaDao).guardarMovimiento(eq(1L), any());
    }

    @Test
    public void testRealizarDepositoExitosoDolares() throws MontoMenorIgualQueCero, MonedaVaciaExcepcion, TipoDeMonedaIncorrectoExcepcion, CuentaNulaExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.DOLARES);
        when(cuentaDao.find(1L)).thenReturn(cuenta);

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("DOLARES");

        depositoRetiroService.realizarDeposito(depositoDto);

        assertEquals(7000, cuenta.getBalance());
        verify(cuentaDao).updateBalance(1L, 7000);
        verify(cuentaDao).guardarMovimiento(eq(1L), any());
    }

    @Test
    public void testDepositoConMontoNegativo() throws MontoMenorIgualQueCero, TipoDeMonedaIncorrectoExcepcion, CuentaNulaExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.PESOS);
    when(cuentaDao.find(1L)).thenReturn(cuenta);
    DepositoRetiroDto depositoDto = new DepositoRetiroDto();
    depositoDto.setCuenta(1L);
    depositoDto.setMonto(-1000); // Monto negativo
    depositoDto.setMoneda("PESOS");

    assertThrows(MontoMenorIgualQueCero.class, () -> {
        depositoRetiroService.realizarDeposito(depositoDto);
    });
}

    @Test
    public void testRealizarDepositoConCuentaNula() {
        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(0); // Cuenta nula
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("PESOS");

        assertThrows(CuentaNulaExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testDepositoConMonedaVacia() throws MontoMenorIgualQueCero, TipoDeMonedaIncorrectoExcepcion, CuentaNulaExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion{
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.PESOS);
    when(cuentaDao.find(1L)).thenReturn(cuenta);
    DepositoRetiroDto depositoDto = new DepositoRetiroDto();
    depositoDto.setCuenta(1L);
    depositoDto.setMonto(1000);
    depositoDto.setMoneda(""); // Moneda vacía
    assertThrows(MonedaVaciaExcepcion.class, () -> {
        depositoRetiroService.realizarDeposito(depositoDto);
    });
}

    @Test
    public void testRealizarDepositoCuentaNoExiste() {
        when(cuentaDao.find(1L)).thenReturn(null);

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("PESOS");

        assertThrows(CuentaOrigenNoExisteExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testRealizarDepositoMonedaErronea() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(1L)).thenReturn(cuenta);

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("PESOS");

        assertThrows(MonedaErroneaTransferenciaExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testRealizarDepositoMonedaDistintaAPesosODolares() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(1L)).thenReturn(cuenta);

        DepositoRetiroDto depositoDto = new DepositoRetiroDto();
        depositoDto.setCuenta(1L);
        depositoDto.setMonto(2000);
        depositoDto.setMoneda("euros");

        assertThrows(TipoDeMonedaIncorrectoExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testRealizarRetiroPesosExitoso() throws MontoMenorIgualQueCero, MonedaVaciaExcepcion, TipoDeMonedaIncorrectoExcepcion, SaldoInsuficienteExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.PESOS);

    when(cuentaDao.find(1L)).thenReturn(cuenta);

    DepositoRetiroDto retiroDto = new DepositoRetiroDto();
    retiroDto.setCuenta(1L);
    retiroDto.setMonto(2000);
    retiroDto.setMoneda("PESOS");

    depositoRetiroService.realizarRetiro(retiroDto);

    assertEquals(3000, cuenta.getBalance());
    verify(cuentaDao).updateBalance(1L, 3000);
    verify(cuentaDao).guardarMovimiento(eq(1L), any());
}

@Test
    public void testRealizarRetiroDolaresExitoso() throws MontoMenorIgualQueCero, MonedaVaciaExcepcion, TipoDeMonedaIncorrectoExcepcion, SaldoInsuficienteExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.DOLARES);

    when(cuentaDao.find(1L)).thenReturn(cuenta);

    DepositoRetiroDto retiroDto = new DepositoRetiroDto();
    retiroDto.setCuenta(1L);
    retiroDto.setMonto(2000);
    retiroDto.setMoneda("DOLARES");

    depositoRetiroService.realizarRetiro(retiroDto);

    assertEquals(3000, cuenta.getBalance());
    verify(cuentaDao).updateBalance(1L, 3000);
    verify(cuentaDao).guardarMovimiento(eq(1L), any());
}

@Test
    public void testRetiroConMontoNegativo() throws MontoMenorIgualQueCero, TipoDeMonedaIncorrectoExcepcion, CuentaNulaExcepcion, CuentaOrigenNoExisteExcepcion, MonedaErroneaTransferenciaExcepcion{
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.PESOS);
    when(cuentaDao.find(1L)).thenReturn(cuenta);
    DepositoRetiroDto retiroDto = new DepositoRetiroDto();
    retiroDto.setCuenta(1L);
    retiroDto.setMonto(-500); // Monto negativo
    retiroDto.setMoneda("PESOS");

    assertThrows(MontoMenorIgualQueCero.class, () -> {
        depositoRetiroService.realizarRetiro(retiroDto);
    });
}
@Test
    public void testCuentaNoExisteRetiro() {
        when(cuentaDao.find(1L)).thenReturn(null);//la cuenta no existe, por lo tanto devuelve null

        DepositoRetiroDto retiroDto = new DepositoRetiroDto();
        retiroDto.setCuenta(1L);
        retiroDto.setMonto(1000);
        retiroDto.setMoneda("PESOS");

        assertThrows(CuentaOrigenNoExisteExcepcion.class, () -> {
            depositoRetiroService.realizarRetiro(retiroDto);
        });
    }

@Test
public void testRealizarRetiroNoAlcanzaSaldo() {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(1000);
    cuenta.setMoneda(TipoMoneda.PESOS);

    when(cuentaDao.find(1L)).thenReturn(cuenta);
    DepositoRetiroDto retiroDto = new DepositoRetiroDto();
    retiroDto.setCuenta(1L);
    retiroDto.setMonto(2000);
    retiroDto.setMoneda("PESOS");

    assertThrows(SaldoInsuficienteExcepcion.class, () -> {
        depositoRetiroService.realizarRetiro(retiroDto);
    });
}
@Test
    public void testRealizarRetiroMonedaDistintaAPesosODolares() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setBalance(5000);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(1L)).thenReturn(cuenta);
        DepositoRetiroDto retiroDto = new DepositoRetiroDto();
        retiroDto.setCuenta(1L);
        retiroDto.setMonto(2000);
        retiroDto.setMoneda("euros");
        assertThrows(TipoDeMonedaIncorrectoExcepcion.class, () -> {
            depositoRetiroService.realizarDeposito(retiroDto);
        });
    }

@Test
public void testRealizarRetiroMonedaErronea() {
    Cuenta cuenta = new Cuenta();
    cuenta.setNumeroCuenta(1L);
    cuenta.setBalance(5000);
    cuenta.setMoneda(TipoMoneda.DOLARES);

    when(cuentaDao.find(1L)).thenReturn(cuenta);

    DepositoRetiroDto retiroDto = new DepositoRetiroDto();
    retiroDto.setCuenta(1L);
    retiroDto.setMonto(2000);
    retiroDto.setMoneda("PESOS");

    assertThrows(MonedaErroneaTransferenciaExcepcion.class, () -> {
        depositoRetiroService.realizarRetiro(retiroDto);
    });
}

}
