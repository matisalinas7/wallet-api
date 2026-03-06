package com.salinas.wallet_api.controller;

import com.salinas.wallet_api.dto.request.UsuarioRequestDTO;
import com.salinas.wallet_api.dto.response.UsuarioResponseDTO;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "1. Gestión de Usuarios", description = "Endpoints para registrar y administrar usuarios en la billetera")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario en el sistema y le asigna automáticamente una cuenta/billetera virtual con saldo inicial de $0.")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(requestDTO));
    }

}
