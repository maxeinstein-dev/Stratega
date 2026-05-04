package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;
import br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort;
import br.com.maxsueleinstein.stratega.application.port.PasswordEncoderPort;
import br.com.maxsueleinstein.stratega.application.usecase.AuthenticateUserUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenProviderPort jwtTokenProviderPort;

    public AuthenticateUserUseCaseImpl(UserRepository userRepository,
                                       PasswordEncoderPort passwordEncoderPort,
                                       JwtTokenProviderPort jwtTokenProviderPort) {
        this.userRepository = userRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtTokenProviderPort = jwtTokenProviderPort;
    }

    @Override
    public TokenResponse execute(AuthenticateUserRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordEncoderPort.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        String token = jwtTokenProviderPort.generateToken(user);

        return new TokenResponse(token);
    }
}
