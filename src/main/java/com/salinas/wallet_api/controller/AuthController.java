package com.salinas.wallet_api.controller;

import com.salinas.wallet_api.dto.request.LoginRequestDTO;
import com.salinas.wallet_api.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "3. Autenticación", description = "Endpoints para login y obtención de tokens")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        return ResponseEntity.ok(authService.autenticar(loginDTO));
    }
}
