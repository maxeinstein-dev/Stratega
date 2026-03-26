package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.entity.Category;
import br.com.maxsueleinstein.stratega.domain.entity.Transaction;
import br.com.maxsueleinstein.stratega.repository.CategoryRepository;
import br.com.maxsueleinstein.stratega.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        Category category = transaction.getCategory();

        if (category != null) {
            Optional<Category> existingCategory = categoryRepository.findByName(category.getName());

            if (existingCategory.isEmpty()) {
                category = categoryRepository.save(category);
            } else {
                category = existingCategory.get();
            }
            transaction.setCategory(category);
        }
        return transactionRepository.save(transaction);

    }
}
