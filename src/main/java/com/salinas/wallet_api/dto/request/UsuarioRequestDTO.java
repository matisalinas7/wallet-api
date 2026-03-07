package com.salinas.wallet_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "El teléfono debe contener solo números y tener entre 10 y 15 dígitos")
    private String telefono;

}
