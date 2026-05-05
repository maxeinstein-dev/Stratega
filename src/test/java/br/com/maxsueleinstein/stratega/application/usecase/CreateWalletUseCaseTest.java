package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateWalletRequest;
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import br.com.maxsueleinstein.stratega.application.usecase.impl.CreateWalletUseCaseImpl;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateWalletUseCaseTest {

    private WalletRepository walletRepository;
    private CreateWalletUseCase createWalletUseCase;

    @BeforeEach
    void setUp() {
        walletRepository = Mockito.mock(WalletRepository.class);
        createWalletUseCase = new CreateWalletUseCaseImpl(walletRepository);
    }

    @Test
    void shouldCreateWalletSuccessfully() {
        UUID userId = UUID.randomUUID();
        CreateWalletRequest request = new CreateWalletRequest("Carteira Casa", BigDecimal.ZERO, userId, Currency.BRL);
        
        Wallet savedWallet = new Wallet(UUID.randomUUID(), request.name(), request.initialBalance(), request.userId());
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);

        WalletResponse response = createWalletUseCase.execute(request);

        assertNotNull(response.id());
        assertEquals("Carteira Casa", response.name());
        assertEquals(BigDecimal.ZERO, response.balance());
        assertEquals(userId, response.userId());
    }
}
