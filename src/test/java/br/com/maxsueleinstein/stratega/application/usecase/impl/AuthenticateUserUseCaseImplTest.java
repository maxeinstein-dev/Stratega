package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;
import br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort;
import br.com.maxsueleinstein.stratega.application.port.PasswordEncoderPort;
import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticateUserUseCaseImplTest {

    private UserRepository userRepository;
    private PasswordEncoderPort passwordEncoderPort;
    private JwtTokenProviderPort jwtTokenProviderPort;
    private AuthenticateUserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoderPort = Mockito.mock(PasswordEncoderPort.class);
        jwtTokenProviderPort = Mockito.mock(JwtTokenProviderPort.class);
        useCase = new AuthenticateUserUseCaseImpl(userRepository, passwordEncoderPort, jwtTokenProviderPort);
    }

    @Test
    @DisplayName("Deve autenticar com sucesso e retornar token JWT")
    void shouldAuthenticateSuccessfully() {
        AuthenticateUserRequest request = new AuthenticateUserRequest("john@example.com", "password123");
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com", "encoded-pass");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("password123", "encoded-pass")).thenReturn(true);
        when(jwtTokenProviderPort.generateToken(user)).thenReturn("fake-jwt-token");

        TokenResponse response = useCase.execute(request);

        assertEquals("fake-jwt-token", response.accessToken());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email não for encontrado")
    void shouldThrowExceptionWhenEmailNotFound() {
        AuthenticateUserRequest request = new AuthenticateUserRequest("invalid@example.com", "password123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            useCase.execute(request)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha estiver incorreta")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        AuthenticateUserRequest request = new AuthenticateUserRequest("john@example.com", "wrong-pass");
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com", "encoded-pass");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("wrong-pass", "encoded-pass")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            useCase.execute(request)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
    }
}
