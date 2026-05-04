package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseImplTest {

    private UserRepository userRepository;
    private PasswordEncoderPort passwordEncoderPort;
    private RegisterUserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoderPort = Mockito.mock(PasswordEncoderPort.class);
        useCase = new RegisterUserUseCaseImpl(userRepository, passwordEncoderPort);
    }

    @Test
    @DisplayName("Deve registrar um usuário com sucesso quando o e-mail não existir")
    void shouldRegisterUserSuccessfully() {
        RegisterUserRequest request = new RegisterUserRequest("John Doe", "john@example.com", "password123");
        
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode("password123")).thenReturn("encoded_password");
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new User(UUID.randomUUID(), user.getName(), user.getEmail(), user.getPassword());
        });

        UserResponse response = useCase.execute(request);

        assertNotNull(response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        
        verify(userRepository).save(argThat(user -> 
            user.getName().equals("John Doe") && 
            user.getPassword().equals("encoded_password")
        ));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar um usuário com e-mail já existente")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest("Jane Doe", "jane@example.com", "password123");
        
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(
            new User(UUID.randomUUID(), "Existing User", "jane@example.com", "hash123")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            useCase.execute(request)
        );

        assertEquals("O email informado já está em uso", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
