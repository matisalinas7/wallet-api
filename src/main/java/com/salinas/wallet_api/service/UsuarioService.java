package com.salinas.wallet_api.service;

import com.salinas.wallet_api.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario registrarUsuario(Usuario usuario);
    Optional<Usuario> findUsuarioById(Long id);
    Optional<Usuario> findUsuarioByEmail(String email);
}
