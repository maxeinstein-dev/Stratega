package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.WalletEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.WalletMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataWalletRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    private final SpringDataWalletRepository repository;

    public WalletRepositoryAdapter(SpringDataWalletRepository repository) {
        this.repository = repository;
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = WalletMapper.toEntity(wallet);
        WalletEntity saved = repository.save(entity);
        return WalletMapper.toDomain(saved);
    }

    @Override
    public Optional<Wallet> findById(UUID id) {
        return repository.findById(id).map(WalletMapper::toDomain);
    }

    @Override
    public List<Wallet> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(WalletMapper::toDomain)
                .collect(Collectors.toList());
    }
}
