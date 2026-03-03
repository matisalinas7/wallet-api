package com.salinas.wallet_api.exception;

import com.salinas.wallet_api.dto.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponseDTO> handleSaldoInsuficiente(SaldoInsuficienteException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCuentaNoEncontrada(CuentaNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}