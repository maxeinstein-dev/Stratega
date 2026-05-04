package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
import br.com.maxsueleinstein.stratega.application.port.PasswordEncoderPort;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegisterUserUseCaseImpl(UserRepository userRepository, PasswordEncoderPort passwordEncoderPort) {
        this.userRepository = userRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserResponse execute(RegisterUserRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            throw new IllegalArgumentException("O email informado já está em uso");
        });

        String encodedPassword = passwordEncoderPort.encode(request.password());
        
        User newUser = new User(
                null,
                request.name(),
                request.email(),
                encodedPassword
        );

        User savedUser = userRepository.save(newUser);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
}
