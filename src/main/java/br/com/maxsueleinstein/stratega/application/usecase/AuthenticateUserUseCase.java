package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;

public interface AuthenticateUserUseCase {
    TokenResponse execute(AuthenticateUserRequest request);
}
