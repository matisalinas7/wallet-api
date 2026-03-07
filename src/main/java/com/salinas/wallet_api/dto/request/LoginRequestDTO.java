package com.salinas.wallet_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe tener un formato de correo válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;
}
