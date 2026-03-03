package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface TransaccionService {

    Transaccion realizarTransferencia(String identificadorOrigen, String identificadorDestino, BigDecimal monto);
    Transaccion realizarCashIn(String identificadorDestino, BigDecimal monto);
    List<TransaccionResponseDTO> obtenerHistorial(String identificador);
}
