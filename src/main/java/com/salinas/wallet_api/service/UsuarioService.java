package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.UsuarioRequestDTO;
import com.salinas.wallet_api.dto.response.UsuarioResponseDTO;
import com.salinas.wallet_api.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO);
    Optional<Usuario> findUsuarioById(Long id);
    Optional<Usuario> findUsuarioByEmail(String email);
}
