package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.usecase.ImportTransactionsUseCase;
import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class ImportTransactionsUseCaseImpl implements ImportTransactionsUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public ImportTransactionsUseCaseImpl(TransactionRepository transactionRepository,
            WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int execute(UUID userId, UUID walletId, MultipartFile file) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada."));

        if (!wallet.getUserId().equals(userId)) {
            throw new ForbiddenException("Você não tem permissão para importar transações para esta carteira.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de importação está vazio.");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("Nome do arquivo inválido.");
        }

        br.com.maxsueleinstein.stratega.infrastructure.parser.TransactionParser parser;
        if (filename.toLowerCase().endsWith(".ofx")) {
            parser = new br.com.maxsueleinstein.stratega.infrastructure.parser.OfxTransactionParser();
        } else if (filename.toLowerCase().endsWith(".csv")) {
            parser = new br.com.maxsueleinstein.stratega.infrastructure.parser.CsvTransactionParser();
        } else {
            throw new IllegalArgumentException("Apenas arquivos .ofx e .csv são suportados no momento.");
        }

        try {
            List<Transaction> transactions = parser.parse(file.getInputStream(), walletId);

            for (Transaction tx : transactions) {
                if (tx.isIncome()) {
                    wallet.addFunds(tx.getAmount());
                } else if (tx.isExpense()) {
                    wallet.removeFunds(tx.getAmount());
                }
                transactionRepository.save(tx);
            }

            walletRepository.save(wallet);

            return transactions.size();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a importação: " + e.getMessage(), e);
        }
    }
}
