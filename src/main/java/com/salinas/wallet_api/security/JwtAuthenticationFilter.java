package com.salinas.wallet_api.security;

import com.salinas.wallet_api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // ✅ acá

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Buscar el Header llamado "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si no hay header o no empieza con "Bearer ", lo dejamos seguir su camino
        // (ej, va a /login o a Swagger, que no requieren token)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (cortar los primeros 7 caracteres de "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 4. Le pedimos a nuestra imprenta que lea el email del token
            userEmail = jwtService.extraerEmail(jwt);

            // 5. Si tiene email y el usuario aún no está logueado en la memoria temporal de esta petición...
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Validamos que la firma sea nuestra y no esté vencido
                if (jwtService.esTokenValido(jwt, userEmail)) {

                    // TOKEN VÁLIDO, Le armamos el pase oficial de Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            new ArrayList<>() // Acá irían los roles (ej: ADMIN, USER) si los tuviéramos
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Guardamos el pase en el contexto de seguridad (El patovica lo anota en su lista VIP)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si el token expiró o alguien lo alteró, la librería lanza un error.
            // Lo atrapamos silenciosamente para que el patovica lo rechace después.
            log.warn("Token inválido o expirado: {}", e.getMessage());
        }

        // 6. Continúa la cadena hacia el controlador
        filterChain.doFilter(request, response);
    }
}
