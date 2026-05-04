package br.com.maxsueleinstein.stratega.application.port;

import br.com.maxsueleinstein.stratega.domain.model.User;

public interface JwtTokenProviderPort {
    String generateToken(User user);
    boolean validateToken(String token);
    String getUserIdFromToken(String token);
}
