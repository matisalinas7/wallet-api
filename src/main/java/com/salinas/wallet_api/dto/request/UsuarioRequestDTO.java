package com.salinas.wallet_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
    private String telefono;

}
