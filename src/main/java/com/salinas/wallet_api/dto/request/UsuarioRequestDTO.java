package com.salinas.wallet_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio!")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio!")
    private String apellido;
    @NotBlank
    @Email(message = "El formato del correo no es válido")
    private String email;

    private String contrasenia;
    @NotBlank
    private String telefono;

}
