package ar.edu.utn.frbb.tup.service;
import org.springframework.stereotype.Service;

@Service
public class BanelcoService {

    public boolean realizarTransferenciaDistintoBanco() {
        boolean estadoTransferencia = Math.random() > 0.4;
        return estadoTransferencia;
    }
}

