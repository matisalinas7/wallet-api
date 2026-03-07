package com.salinas.wallet_api.service;

import com.salinas.wallet_api.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    // @Value inyecta el valor que pusimos en el application.yaml
    @Value("${api.security.token.secret}")
    private String secretKey;

    // Tiempo de validez del token: 2 horas en milisegundos
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 2;

    // 1. LA IMPRENTA: Genera el token con los datos del usuario
    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail()) // El "dueño" del pase VIP (usamos el email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de vencimiento
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Lo firmamos con nuestra llave maestra y el algoritmo HS256
                .compact(); // Lo compacta en un String (el JWT)
    }

    // 2. EL LECTOR: Extrae el email leyendo el token
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // 3. EL VALIDADOR: Chequea que el token sea del usuario y no esté vencido
    public boolean esTokenValido(String token, String emailUsuario) {
        final String email = extraerEmail(token);
        return (email.equals(emailUsuario)) && !esTokenExpirado(token);
    }

    private boolean esTokenExpirado(String token) {
        return extraerExpiration(token).before(new Date());
    }

    private Date extraerExpiration(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Usa la llave maestra para abrir el candado
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Transforma nuestro String del application.yaml en una Key criptográfica
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
