package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.UsuarioRequestDTO;
import com.salinas.wallet_api.dto.response.UsuarioResponseDTO;
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
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {

        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya existe en la base de datos");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());
        usuario.setEmail(requestDTO.getEmail());
        usuario.setContrasenia(requestDTO.getContrasenia());
        usuario.setTelefono(requestDTO.getTelefono());

        Usuario guardado = usuarioRepository.save(usuario);

        Cuenta cuenta = new Cuenta();
        cuenta.setUsuario(guardado);
        cuenta.setCvu(UUID.randomUUID().toString());
        cuenta.setAlias(UUID.randomUUID().toString());
        cuentaRepository.save(cuenta);

        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(guardado.getId());
        response.setNombre(guardado.getNombre());
        response.setApellido(guardado.getApellido());
        response.setEmail(guardado.getEmail());
        response.setTelefono(guardado.getTelefono());

        return response;
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
