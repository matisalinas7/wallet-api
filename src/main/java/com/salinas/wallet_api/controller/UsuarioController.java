package com.salinas.wallet_api.controller;

import com.salinas.wallet_api.dto.request.UsuarioRequestDTO;
import com.salinas.wallet_api.dto.response.UsuarioResponseDTO;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@RequestBody UsuarioRequestDTO requestDTO) {

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(requestDTO.getNombre());
        nuevoUsuario.setApellido(requestDTO.getApellido());
        nuevoUsuario.setEmail(requestDTO.getEmail());
        nuevoUsuario.setContrasenia(requestDTO.getContrasenia());
        nuevoUsuario.setTelefono(requestDTO.getTelefono());

        Usuario usuarioGuardado = usuarioService.registrarUsuario(nuevoUsuario);

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setNombre(usuarioGuardado.getNombre());
        responseDTO.setApellido(usuarioGuardado.getApellido());
        responseDTO.setEmail(usuarioGuardado.getEmail());
        responseDTO.setTelefono(usuarioGuardado.getTelefono());
        responseDTO.setId(usuarioGuardado.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

}
