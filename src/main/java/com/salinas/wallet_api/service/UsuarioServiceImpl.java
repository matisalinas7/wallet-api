package com.salinas.wallet_api.service;

import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.repository.CuentaRepository;
import com.salinas.wallet_api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email existe en la base de datos!");
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cuenta  cuenta = new Cuenta();
        cuenta.setUsuario(usuarioGuardado);
        cuenta.setCvu(UUID.randomUUID().toString());
        cuenta.setAlias(UUID.randomUUID().toString());

        cuentaRepository.save(cuenta);

        return usuarioGuardado;
    }

    @Override
    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
