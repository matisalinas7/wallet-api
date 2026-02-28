package com.salinas.wallet_api.service;

import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Transaccion;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.repository.CuentaRepository;
import com.salinas.wallet_api.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.salinas.wallet_api.enums.TipoTransaccion.TRANSFERENCIA;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public Transaccion realizarTransferencia(String identificadorOrigen, String identificadorDestino, BigDecimal monto) {

        Cuenta cuentaOrigen = buscarCuentaPorIdentificador(identificadorOrigen);
        Cuenta cuentaDestino = buscarCuentaPorIdentificador(identificadorDestino);

        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar la transferencia");
        }

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TRANSFERENCIA);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        return transaccionRepository.save(transaccion);
    }

    private Cuenta buscarCuentaPorIdentificador(String identificador) {
        if (identificador.contains("@")) {
            return cuentaRepository.findByUsuarioEmail(identificador)
                    .orElseThrow(() -> new RuntimeException("No existe cuenta asociada al email: " + identificador));
        } else {
            return cuentaRepository.findByCvu(identificador)
                    .orElseGet(() -> cuentaRepository.findByAlias(identificador)
                            .orElseThrow(() -> new RuntimeException("No existe cuenta con el CVU o Alias: " + identificador)));
        }
    }
}
