package ar.edu.utn.frbb.tup.presentation.input;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.CuentaService;

public class ShowInfoCuenta {

    private CuentaService cuentaService;
    private ClienteService clienteService;

    public ShowInfoCuenta() {
        this.cuentaService = new CuentaService();
    }

    public void mostrarInfoCuenta(long id) {
        Cuenta cuenta = cuentaService.find(id);
        Cliente cliente = clienteService.buscarClientePorDni(id);     //creo un tipo cliente para saber su dni y asi tener el titular    
        if(cuenta == null) {
            System.out.println("Cuenta no encontrada!");
        }
        System.out.println("Información de la Cuenta: ");
        System.out.println("Cuenta id: " + cuenta.getNumeroCuenta());
        System.out.println("Tipo de Cuenta: " + cuenta.getTipoCuenta());
        System.out.println("Tipo de Moneda: " + cuenta.getMoneda());
        System.out.println("Balance: " + cuenta.getBalance());
        System.out.println("Titular: " + cliente.getNombre() + " " + cliente.getApellido()); //como aca
        System.out.println("Fecha de Creación: " + cuenta.getFechaCreacion());
    }
}
