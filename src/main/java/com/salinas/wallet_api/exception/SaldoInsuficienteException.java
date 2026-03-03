package com.salinas.wallet_api.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
