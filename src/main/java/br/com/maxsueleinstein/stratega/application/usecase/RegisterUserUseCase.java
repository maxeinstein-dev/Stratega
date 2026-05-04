package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;

public interface RegisterUserUseCase {
    UserResponse execute(RegisterUserRequest request);
}
