package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.controller.MovimientoDto;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public void updateBalance(long id, double nuevoBalance){
        CuentaEntity cuentaEntity = ((CuentaEntity) getInMemoryDatabase().get(id));     //para actualizar el balance en las transferencias

        if (cuentaEntity!=null){
            cuentaEntity.setBalance(nuevoBalance);
            getInMemoryDatabase().put(cuentaEntity.getId(), cuentaEntity);
        }
    }

    public void guardarMovimiento(long id, MovimientoDto movimientoDto){
        CuentaEntity cuentaEntity = ((CuentaEntity) getInMemoryDatabase().get(id));     //para agregar movimiento a la cuenta

        if (cuentaEntity!=null){
            Movimiento movimiento1 = new Movimiento(movimientoDto);
            cuentaEntity.agregarMovimiento(movimiento1);
            getInMemoryDatabase().put(cuentaEntity.getId(), cuentaEntity);
        }
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }
}
