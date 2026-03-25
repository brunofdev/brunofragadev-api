package com.brunofragadev.infrastructure.config;


import com.brunofragadev.module.user.domain.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtProvider {

    // Injeta a chave secreta do application.properties
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    // Injeta o tempo de expiração do application.properties
    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    /**
     * Gera um novo token JWT para um usuário.
     */
    public String generateToken(String username, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(username) // Define o "dono" do token (geralmente o username ou email)
                .issuedAt(now) // Define a data de criação
                .expiration(expiryDate) // Define a data de expiração
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Assina com o algoritmo HS256 e a chave secreta
                .compact(); // Constrói e serializa o token para uma string
    }

    /**
     * Extrai o username (subject) de um token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida se um token é autêntico e pertence a um usuário específico.
     * (Esta lógica será mais usada no API Gateway, mas é bom tê-la aqui).
     */
    public boolean isTokenValid(String token, String username) {
        final String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }
    /**
     * Tenta validar o token e retornar o usuário.
     * Se o token estiver inválido ou expirado, não quebra a aplicação, apenas retorna null.
     */
    public String validateToken(String token) {
        try {
            // Tenta extrair o usuário (isso vai falhar se o token for inválido/expirado)
            return extractUsername(token);
        } catch (Exception e) {
            // Se der qualquer erro (token expirado, assinatura ruim, malformado), retorna null
            return null;
        }
    }

    // --- Métodos Privados Auxiliares ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Converte a chave secreta (que está em Base64) para um objeto Key
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}