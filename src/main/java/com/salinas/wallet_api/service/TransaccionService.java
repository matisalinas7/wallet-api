package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.CashInRequestDTO;
import com.salinas.wallet_api.dto.request.TransaccionRequestDTO;
import com.salinas.wallet_api.dto.response.CashInResponseDTO;
import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface TransaccionService {

    TransaccionResponseDTO realizarTransferencia(TransaccionRequestDTO requestDTO);
    CashInResponseDTO realizarCashIn(CashInRequestDTO requestDTO);
    List<TransaccionResponseDTO> obtenerHistorial(String identificador);
}
