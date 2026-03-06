package com.salinas.wallet_api.controller;

import com.salinas.wallet_api.dto.request.CashInRequestDTO;
import com.salinas.wallet_api.dto.request.TransaccionRequestDTO;
import com.salinas.wallet_api.dto.response.CashInResponseDTO;
import com.salinas.wallet_api.dto.response.TransaccionResponseDTO;
import com.salinas.wallet_api.entity.Transaccion;
import com.salinas.wallet_api.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
@Tag(name = "2. Gestión de Transacciones", description = "Endpoints para realizar movimientos financieros y visualizarlos")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/transferir")
    @Operation(summary = "Realizar una transferencia", description = "Permite realizar una transferencia de dinero hacia otro usuario")
    public ResponseEntity<TransaccionResponseDTO> registrarTransaccion(@Valid @RequestBody TransaccionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.realizarTransferencia(requestDTO));
    }

    @PostMapping("/cashin")
    @Operation(summary = "Ingresar dinero", description = "Permite ingresar dinero a la cuenta")
    public ResponseEntity<CashInResponseDTO> realizarCashIn(@Valid @RequestBody CashInRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionService.realizarCashIn(requestDTO));
    }

    @GetMapping("/historial/{identificador}")
    @Operation(summary = "Ver Historial de movimientos", description = "Permite visualizar el historial de movimientos de un usuario")
    public ResponseEntity<List<TransaccionResponseDTO>> verHistorial(@PathVariable String identificador) {
        return ResponseEntity.ok(transaccionService.obtenerHistorial(identificador));
    }
}

