package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;
import br.com.maxsueleinstein.stratega.application.usecase.impl.TransferFundsUseCaseImpl;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferFundsUseCaseTest {

    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private br.com.maxsueleinstein.stratega.domain.repository.TransferRepository transferRepository;
    private TransferFundsUseCase transferFundsUseCase;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        walletRepository = Mockito.mock(WalletRepository.class);
        transferRepository = Mockito.mock(br.com.maxsueleinstein.stratega.domain.repository.TransferRepository.class);
        transferFundsUseCase = new TransferFundsUseCaseImpl(walletRepository, transactionRepository, transferRepository);
    }

    @Test
    void shouldTransferFundsSuccessfully() {
        UUID originWalletId = UUID.randomUUID();
        UUID destinationWalletId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Wallet origin = new Wallet(originWalletId, "Origin", BigDecimal.valueOf(1000), UUID.randomUUID());
        Wallet destination = new Wallet(destinationWalletId, "Destination", BigDecimal.valueOf(500), UUID.randomUUID());

        when(walletRepository.findById(originWalletId)).thenReturn(Optional.of(origin));
        when(walletRepository.findById(destinationWalletId)).thenReturn(Optional.of(destination));

        TransferFundsRequest request = new TransferFundsRequest(
                "Transferência de aluguel",
                BigDecimal.valueOf(200),
                LocalDateTime.now(),
                originWalletId,
                destinationWalletId,
                categoryId
        );

        transferFundsUseCase.execute(request);

        assertEquals(BigDecimal.valueOf(800), origin.getBalance());
        assertEquals(BigDecimal.valueOf(700), destination.getBalance());

        verify(walletRepository).save(origin);
        verify(walletRepository).save(destination);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(transferRepository).save(any(br.com.maxsueleinstein.stratega.domain.model.Transfer.class));
    }
}
