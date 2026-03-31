package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.entity.Category;
import br.com.maxsueleinstein.stratega.domain.entity.Transaction;
import br.com.maxsueleinstein.stratega.domain.entity.TransactionType;
import br.com.maxsueleinstein.stratega.domain.entity.Wallet;
import br.com.maxsueleinstein.stratega.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Transaction createTransaction(Transaction transaction) {
        Category category = transaction.getCategory();

        if (category != null) {
            Optional<Category> existingCategory =
                    categoryRepository.findByNameAndUserId(category.getName(), transaction.getUser().getId());

            if (existingCategory.isEmpty()) {
                category = categoryRepository.save(category);
            } else {
                category = existingCategory.get();
            }
            transaction.setCategory(category);
        }

        Wallet wallet = walletRepository
                .findById(transaction.getWallet().getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (transaction.getType() == TransactionType.INCOME) {
            wallet.credit(transaction.getAmount());
        }
        if (transaction.getType() == TransactionType.EXPENSE) {
            wallet.debit(transaction.getAmount());
        }
        if (transaction.getType() == TransactionType.TRANSFER) {
            Wallet origin = walletRepository.findById(transaction.getWallet().getId())
                    .orElseThrow(() -> new RuntimeException("Origin wallet not found"));
            Wallet destination = walletRepository
                    .findById(transaction.getDestinationWallet().getId())
                    .orElseThrow(() -> new RuntimeException("Destination wallet not found"));

            origin.debit(transaction.getAmount());
            destination.credit(transaction.getAmount());
        }
        return transactionRepository.save(transaction);

    }
}
