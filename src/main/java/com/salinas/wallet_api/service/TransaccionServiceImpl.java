package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.CashInRequestDTO;
import com.salinas.wallet_api.dto.request.TransaccionRequestDTO;
import com.salinas.wallet_api.dto.response.CashInResponseDTO;
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
    public TransaccionResponseDTO realizarTransferencia(TransaccionRequestDTO requestDTO) {

        Cuenta cuentaOrigen = buscarCuentaPorIdentificador(requestDTO.getIdentificadorOrigen());
        Cuenta cuentaDestino = buscarCuentaPorIdentificador(requestDTO.getIdentificadorDestino());

        if (cuentaOrigen.getSaldo().compareTo(requestDTO.getMonto()) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar la transferencia");
        }

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(requestDTO.getMonto()));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(requestDTO.getMonto()));

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TRANSFERENCIA);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(requestDTO.getMonto());

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        Transaccion transaccionGuardada =  transaccionRepository.save(transaccion);

        TransaccionResponseDTO  response = new TransaccionResponseDTO();
        response.setId(transaccionGuardada.getId());
        response.setMonto(transaccionGuardada.getMonto());
        response.setFecha(transaccionGuardada.getFechaAlta());
        response.setTipoTransaccion(transaccionGuardada.getTipoTransaccion());

        return response;
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
    public CashInResponseDTO realizarCashIn(CashInRequestDTO requestDTO) {

        Cuenta cuentaDestino = buscarCuentaPorIdentificador(requestDTO.getIdentificadorDestino());

        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(requestDTO.getMonto()));

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(CASH_IN);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(requestDTO.getMonto());

        cuentaRepository.save(cuentaDestino);

        Transaccion transaccionGuardada = transaccionRepository.save(transaccion);

        CashInResponseDTO response = new CashInResponseDTO();
        response.setId(transaccionGuardada.getId());
        response.setMonto(transaccionGuardada.getMonto());
        response.setFecha(transaccionGuardada.getFechaAlta());

        return response;
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
