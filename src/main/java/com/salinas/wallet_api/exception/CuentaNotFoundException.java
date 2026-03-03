package com.salinas.wallet_api.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException(String mensaje) {
        super(mensaje);
    }

}
