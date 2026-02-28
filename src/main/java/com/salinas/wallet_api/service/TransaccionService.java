package com.salinas.wallet_api.service;

import com.salinas.wallet_api.entity.Transaccion;

import java.math.BigDecimal;

public interface TransaccionService {

    Transaccion realizarTransferencia(String identificadorOrigen, String identificadorDestino, BigDecimal monto);

}
