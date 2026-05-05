package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.Transfer;
import br.com.maxsueleinstein.stratega.domain.repository.TransferRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransferEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.TransferMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataTransferRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TransferRepositoryAdapter implements TransferRepository {

    private final SpringDataTransferRepository repository;

    public TransferRepositoryAdapter(SpringDataTransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transfer save(Transfer transfer) {
        TransferEntity entity = TransferMapper.toEntity(transfer);
        TransferEntity saved = repository.save(entity);
        return TransferMapper.toDomain(saved);
    }

    @Override
    public Optional<Transfer> findById(UUID id) {
        return repository.findById(id).map(TransferMapper::toDomain);
    }

    @Override
    public Optional<Transfer> findByTransactionId(UUID transactionId) {
        return repository.findByTransactionId(transactionId).map(TransferMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
