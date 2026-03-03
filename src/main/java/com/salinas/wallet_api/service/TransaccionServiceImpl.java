package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Transaccion;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.exception.CuentaNotFoundException;
import com.salinas.wallet_api.exception.SaldoInsuficienteException;
import com.salinas.wallet_api.repository.CuentaRepository;
import com.salinas.wallet_api.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.salinas.wallet_api.enums.TipoTransaccion.CASH_IN;
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
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia");
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
                    .orElseThrow(() -> new CuentaNotFoundException("No existe cuenta asociada al email: " + identificador));
        } else {
            return cuentaRepository.findByCvu(identificador)
                    .orElseGet(() -> cuentaRepository.findByAlias(identificador)
                            .orElseThrow(() -> new CuentaNotFoundException("No existe cuenta con el CVU o Alias: " + identificador)));
        }
    }

    @Override
    @Transactional
    public Transaccion realizarCashIn(String identificadorDestino, BigDecimal monto) {

        Cuenta cuentaDestino = buscarCuentaPorIdentificador(identificadorDestino);

        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(CASH_IN);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);

        cuentaRepository.save(cuentaDestino);

        return transaccionRepository.save(transaccion);
    }

    public List<TransaccionResponseDTO> obtenerHistorial(String identificador) {
        Cuenta cuentaSeleccionada = buscarCuentaPorIdentificador(identificador);

        return transaccionRepository
                .findByCuentaOrigenOrCuentaDestinoOrderByFechaAltaDesc(cuentaSeleccionada, cuentaSeleccionada)
                .stream()
                .map(transaccion -> new TransaccionResponseDTO(
                        transaccion.getId(),
                        transaccion.getMonto(),
                        transaccion.getFechaAlta(),
                        transaccion.getTipoTransaccion()
                ))
                .collect(Collectors.toList());
    }



}
