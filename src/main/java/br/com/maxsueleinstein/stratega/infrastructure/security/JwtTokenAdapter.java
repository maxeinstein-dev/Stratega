package br.com.maxsueleinstein.stratega.infrastructure.security;

import br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenAdapter implements JwtTokenProviderPort {

    @Value("${jwt.secret:defaultSecretKeyWithAtLeast32CharactersForHmacSha256}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private int jwtExpirationMs;

    @Override
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }
    
    @Override
    public String getUserIdFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    @Override
    public boolean validateToken(String authToken) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
