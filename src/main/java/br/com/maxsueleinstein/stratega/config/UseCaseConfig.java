package br.com.maxsueleinstein.stratega.config;

import br.com.maxsueleinstein.stratega.application.usecase.AuthenticateUserUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.CreateTransactionUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.CreateWalletUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.TransferFundsUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.impl.AuthenticateUserUseCaseImpl;
import br.com.maxsueleinstein.stratega.application.usecase.impl.CreateTransactionUseCaseImpl;
import br.com.maxsueleinstein.stratega.application.usecase.impl.CreateWalletUseCaseImpl;
import br.com.maxsueleinstein.stratega.application.usecase.impl.RegisterUserUseCaseImpl;
import br.com.maxsueleinstein.stratega.application.usecase.impl.TransferFundsUseCaseImpl;
import br.com.maxsueleinstein.stratega.application.port.PasswordEncoderPort;
import br.com.maxsueleinstein.stratega.application.port.JwtTokenProviderPort;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoderPort passwordEncoderPort) {
        return new RegisterUserUseCaseImpl(userRepository, walletRepository, passwordEncoderPort);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository, PasswordEncoderPort passwordEncoderPort, JwtTokenProviderPort jwtTokenProviderPort) {
        return new AuthenticateUserUseCaseImpl(userRepository, passwordEncoderPort, jwtTokenProviderPort);
    }

    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        return new CreateTransactionUseCaseImpl(transactionRepository, walletRepository);
    }

    @Bean
    public CreateWalletUseCase createWalletUseCase(WalletRepository walletRepository) {
        return new CreateWalletUseCaseImpl(walletRepository);
    }

    @Bean
    public TransferFundsUseCase transferFundsUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        return new TransferFundsUseCaseImpl(walletRepository, transactionRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.CreateGroupUseCase createGroupUseCase(br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository repository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.CreateGroupUseCaseImpl(repository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.AddGroupExpenseUseCase addGroupExpenseUseCase(br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository repository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.AddGroupExpenseUseCaseImpl(repository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.CalculateGroupBalancesUseCase calculateGroupBalancesUseCase(br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository repository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.CalculateGroupBalancesUseCaseImpl(repository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.FindWalletsByUserIdUseCase findWalletsByUserIdUseCase(WalletRepository walletRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.FindWalletsByUserIdUseCaseImpl(walletRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase(TransactionRepository transactionRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.FindTransactionsByUserIdUseCaseImpl(transactionRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.FindGroupsByUserIdUseCase findGroupsByUserIdUseCase(br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository repository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.FindGroupsByUserIdUseCaseImpl(repository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.UpdateTransactionUseCase updateTransactionUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.UpdateTransactionUseCaseImpl(transactionRepository, walletRepository);
    }
}
