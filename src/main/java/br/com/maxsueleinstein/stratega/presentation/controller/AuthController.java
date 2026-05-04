package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
import br.com.maxsueleinstein.stratega.application.usecase.AuthenticateUserUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody RegisterUserRequest request) {
        return registerUserUseCase.execute(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody AuthenticateUserRequest request) {
        return authenticateUserUseCase.execute(request);
    }
}
