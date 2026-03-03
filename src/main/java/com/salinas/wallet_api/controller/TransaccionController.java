package com.salinas.wallet_api.controller;

import com.salinas.wallet_api.dto.request.CashInRequestDTO;
import com.salinas.wallet_api.dto.request.TransaccionRequestDTO;
import com.salinas.wallet_api.dto.response.CashInResponseDTO;
import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Transaccion;
import com.salinas.wallet_api.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransaccionResponseDTO> registrarTransaccion(@RequestBody TransaccionRequestDTO transaccionRequestDTO) {

        Transaccion transaccion = transaccionService.realizarTransferencia(transaccionRequestDTO.getIdentificadorOrigen(), transaccionRequestDTO.getIdentificadorDestino(), transaccionRequestDTO.getMonto());

        TransaccionResponseDTO transaccionResponseDTO = new TransaccionResponseDTO();
        transaccionResponseDTO.setId(transaccion.getId());
        transaccionResponseDTO.setMonto(transaccion.getMonto());
        transaccionResponseDTO.setTipoTransaccion(transaccion.getTipoTransaccion());
        transaccionResponseDTO.setFecha(transaccion.getFechaAlta());

        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionResponseDTO);
    }

    @PostMapping("/cashin")
    public ResponseEntity<CashInResponseDTO> realizarCashIn(@RequestBody CashInRequestDTO cashInRequestDTO) {

        Transaccion transaccion = transaccionService.realizarCashIn(cashInRequestDTO.getIdentificadorDestino(), cashInRequestDTO.getMonto());

        CashInResponseDTO cashInResponseDTO = new CashInResponseDTO();
        cashInResponseDTO.setId(transaccion.getId());
        cashInResponseDTO.setMonto(transaccion.getMonto());
        cashInResponseDTO.setFecha(transaccion.getFechaAlta());

        return ResponseEntity.status(HttpStatus.CREATED).body(cashInResponseDTO);
    }

    @GetMapping("/historial/{identificador}")
    public ResponseEntity<List<TransaccionResponseDTO>> verHistorial(@PathVariable String identificador) {
        return ResponseEntity.ok(transaccionService.obtenerHistorial(identificador));
    }
}

