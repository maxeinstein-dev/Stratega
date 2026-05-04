package br.com.maxsueleinstein.stratega.application.usecase.impl;
 
import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
import br.com.maxsueleinstein.stratega.application.port.PasswordEncoderPort;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import java.math.BigDecimal;
 
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
 
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoderPort passwordEncoderPort;
 
    public RegisterUserUseCaseImpl(UserRepository userRepository, 
                                   WalletRepository walletRepository,
                                   PasswordEncoderPort passwordEncoderPort) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
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
 
        // Criar carteira padrão para o novo usuário
        Wallet defaultWallet = new Wallet(
            null,
            "Minha Carteira",
            BigDecimal.ZERO,
            savedUser.getId()
        );
        walletRepository.save(defaultWallet);
 
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
}
