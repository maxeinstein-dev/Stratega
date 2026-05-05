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
    public CreateTransactionUseCase createTransactionUseCase(
            TransactionRepository transactionRepository, 
            WalletRepository walletRepository,
            br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository budgetRepository,
            br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository,
            org.springframework.context.ApplicationEventPublisher eventPublisher) {
        return new CreateTransactionUseCaseImpl(transactionRepository, walletRepository, budgetRepository, categoryRepository, eventPublisher);
    }

    @Bean
    public CreateWalletUseCase createWalletUseCase(WalletRepository walletRepository) {
        return new CreateWalletUseCaseImpl(walletRepository);
    }

    @Bean
    public TransferFundsUseCase transferFundsUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository, br.com.maxsueleinstein.stratega.domain.repository.TransferRepository transferRepository) {
        return new TransferFundsUseCaseImpl(walletRepository, transactionRepository, transferRepository);
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

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.GetDashboardSummaryUseCase getDashboardSummaryUseCase(
            TransactionRepository transactionRepository, 
            br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository,
            WalletRepository walletRepository,
            br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService exchangeRateService) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.GetDashboardSummaryUseCaseImpl(
                transactionRepository, categoryRepository, walletRepository, exchangeRateService);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.SetBudgetUseCase setBudgetUseCase(br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository budgetRepository, br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.SetBudgetUseCaseImpl(budgetRepository, categoryRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.GetBudgetsUseCase getBudgetsUseCase(br.com.maxsueleinstein.stratega.domain.repository.BudgetRepository budgetRepository, TransactionRepository transactionRepository, br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.GetBudgetsUseCaseImpl(budgetRepository, transactionRepository, categoryRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.ImportTransactionsUseCase importTransactionsUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.ImportTransactionsUseCaseImpl(transactionRepository, walletRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.UpdateWalletUseCase updateWalletUseCase(WalletRepository walletRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.UpdateWalletUseCaseImpl(walletRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.DeleteWalletUseCase deleteWalletUseCase(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.DeleteWalletUseCaseImpl(walletRepository, transactionRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.CreateCategoryUseCase createCategoryUseCase(br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.CreateCategoryUseCaseImpl(categoryRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.FindCategoriesByUserIdUseCase findCategoriesByUserIdUseCase(br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.FindCategoriesByUserIdUseCaseImpl(categoryRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.UpdateCategoryUseCase updateCategoryUseCase(br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.UpdateCategoryUseCaseImpl(categoryRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.DeleteCategoryUseCase deleteCategoryUseCase(br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.DeleteCategoryUseCaseImpl(categoryRepository, transactionRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase deleteTransferUseCase(br.com.maxsueleinstein.stratega.domain.repository.TransferRepository transferRepository, TransactionRepository transactionRepository, WalletRepository walletRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.DeleteTransferUseCaseImpl(transferRepository, transactionRepository, walletRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.DeleteTransactionUseCase deleteTransactionUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository, br.com.maxsueleinstein.stratega.application.usecase.DeleteTransferUseCase deleteTransferUseCase) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.DeleteTransactionUseCaseImpl(transactionRepository, walletRepository, deleteTransferUseCase);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.SettleGroupDebtUseCase settleGroupDebtUseCase(br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository groupRepository, WalletRepository walletRepository, TransactionRepository transactionRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.SettleGroupDebtUseCaseImpl(groupRepository, walletRepository, transactionRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.GetNotificationsUseCase getNotificationsUseCase(br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository notificationRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.GetNotificationsUseCaseImpl(notificationRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.MarkNotificationAsReadUseCase markNotificationAsReadUseCase(br.com.maxsueleinstein.stratega.domain.repository.NotificationRepository notificationRepository) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.MarkNotificationAsReadUseCaseImpl(notificationRepository);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.GetSpendingTrendUseCase getSpendingTrendUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository, br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService exchangeRateService) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.GetSpendingTrendUseCaseImpl(transactionRepository, walletRepository, exchangeRateService);
    }

    @Bean
    public br.com.maxsueleinstein.stratega.application.usecase.GetCategoryComparisonUseCase getCategoryComparisonUseCase(TransactionRepository transactionRepository, br.com.maxsueleinstein.stratega.domain.repository.CategoryRepository categoryRepository, WalletRepository walletRepository, br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService exchangeRateService) {
        return new br.com.maxsueleinstein.stratega.application.usecase.impl.GetCategoryComparisonUseCaseImpl(transactionRepository, categoryRepository, walletRepository, exchangeRateService);
    }
}
