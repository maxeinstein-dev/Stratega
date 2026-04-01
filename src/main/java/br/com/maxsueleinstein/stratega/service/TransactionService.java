package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.dto.TransactionRequestDTO;
import br.com.maxsueleinstein.stratega.domain.dto.TransactionResponseDTO;
import br.com.maxsueleinstein.stratega.domain.entity.Category;
import br.com.maxsueleinstein.stratega.domain.entity.Transaction;
import br.com.maxsueleinstein.stratega.domain.entity.Wallet;
import br.com.maxsueleinstein.stratega.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request) {
        Wallet wallet = walletRepository
                .findById(request.walletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        String categoryName = request.categoryName().trim().toUpperCase();
        String categoryType = request.type().name();

        Category category = categoryRepository
                .findByNameAndUserId(
                        categoryName, request.userId())
                .orElseGet(() -> categoryRepository.save(
                        new Category(categoryName, categoryType)
                ));

        Transaction transaction = new Transaction(
                request.description(),
                request.amount(),
                request.date(),
                request.type(),
                wallet,
                category
        );

        switch (transaction.getType()) {
            case INCOME -> wallet.credit(transaction.getAmount());
            case EXPENSE -> wallet.debit(transaction.getAmount());
            case TRANSFER -> {
                if (request.originWalletId() == null) {
                    throw new IllegalArgumentException("Destination wallet is required for transfer");
                }
                Wallet destination = walletRepository
                        .findById(request.originWalletId())
                        .orElseThrow(() -> new RuntimeException("Destination wallet not found"));

                wallet.debit(transaction.getAmount());
                destination.credit(transaction.getAmount());
            }
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                wallet.getBalance()
        );

    }
}
