package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImportTransactionsUseCaseImplTest {

  private TransactionRepository transactionRepository;
  private WalletRepository walletRepository;
  private ImportTransactionsUseCaseImpl useCase;

  @BeforeEach
  void setUp() {
    transactionRepository = mock(TransactionRepository.class);
    walletRepository = mock(WalletRepository.class);
    useCase = new ImportTransactionsUseCaseImpl(transactionRepository, walletRepository);
  }

  @Test
  @DisplayName("Deve importar arquivo OFX e atualizar saldo da carteira")
  void shouldImportOfxAndUpdateBalance() {
    UUID userId = UUID.randomUUID();
    UUID walletId = UUID.randomUUID();
    Wallet wallet = new Wallet(walletId, "Corrente", new BigDecimal("1000.00"), userId);

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

    String ofxContent = """
        <STMTTRN>
          <TRNTYPE>DEBIT
          <TRNAMT>-100.00
          <DTPOSTED>20260515120000
        </STMTTRN>
        <STMTTRN>
          <TRNTYPE>CREDIT
          <TRNAMT>500.00
          <DTPOSTED>20260516120000
        </STMTTRN>
        """;

    MockMultipartFile file = new MockMultipartFile("file", "extrato.ofx", "application/octet-stream",
        ofxContent.getBytes());

    int count = useCase.execute(userId, walletId, file);

    assertEquals(2, count);

    // Saldo inicial 1000 - 100 + 500 = 1400
    assertEquals(new BigDecimal("1400.00"), wallet.getBalance());

    verify(transactionRepository, times(2)).save(any());
    verify(walletRepository).save(wallet);
  }

  @Test
  @DisplayName("Nao deve permitir importacao para carteira de outro usuario")
  void shouldBlockImportForOtherUserWallet() {
    UUID userId = UUID.randomUUID();
    UUID walletId = UUID.randomUUID();
    Wallet wallet = new Wallet(walletId, "Corrente", new BigDecimal("1000.00"), UUID.randomUUID());

    when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
    MockMultipartFile file = new MockMultipartFile("file", "extrato.ofx", "application/octet-stream", new byte[0]);

    assertThrows(ForbiddenException.class, () -> useCase.execute(userId, walletId, file));
  }
}
