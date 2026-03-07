package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.LoginRequestDTO;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.exception.CredencialesInvalidasException;
import com.salinas.wallet_api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // 1. Agregamos el JwtService

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String autenticar(LoginRequestDTO loginDTO) {
        // 1. Buscar al usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas")); // Usar un error genérico por seguridad

        // 2. Comparar la contraseña en texto plano con el hash de la BD
        if (!passwordEncoder.matches(loginDTO.getContrasenia(), usuario.getContrasenia())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        // 3. El usuario existe y la contraseña es correcta. Le fabrica su Token.
        return jwtService.generarToken(usuario);
    }

}
