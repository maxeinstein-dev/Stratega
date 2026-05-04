package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransactionEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.TransactionMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = TransactionMapper.toEntity(transaction);
        TransactionEntity saved = repository.save(entity);
        return TransactionMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return repository.findById(id).map(TransactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findByWalletId(UUID walletId) {
        return repository.findByWalletId(walletId).stream()
                .map(TransactionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(TransactionMapper::toDomain)
                .collect(Collectors.toList());
    }
}
