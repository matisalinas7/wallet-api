package com.salinas.wallet_api.service;

import com.salinas.wallet_api.dto.request.UsuarioRequestDTO;
import com.salinas.wallet_api.dto.response.UsuarioResponseDTO;
import com.salinas.wallet_api.entity.Cuenta;
import com.salinas.wallet_api.entity.Usuario;
import com.salinas.wallet_api.repository.CuentaRepository;
import com.salinas.wallet_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void registrarUsuario_conEmailNuevo_debeRetornarDTO() {

        // --- ARRANGE ---
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEmail("juan@email.com");
        request.setContrasenia("123456");
        request.setTelefono("1234567890123");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setNombre("Juan");
        usuarioGuardado.setApellido("Perez");
        usuarioGuardado.setEmail("juan@email.com");

        when(usuarioRepository.existsByEmail("juan@email.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("123456"))
                .thenReturn("$2a$10$hasheado");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioGuardado);

        // --- ACT ---
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(request);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan@email.com", resultado.getEmail());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void registrarUsuario_conEmailDuplicado_debeLanzarExcepcion() {

        // --- ARRANGE ---
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("juan@email.com");

        when(usuarioRepository.existsByEmail("juan@email.com"))
                .thenReturn(true); // simulamos que el email ya existe

        // --- ACT & ASSERT ---
        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrarUsuario(request)
        );

        // Si el email ya existe, nunca debería llamarse save()
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
