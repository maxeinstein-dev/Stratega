package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findById(UUID id);
    List<Wallet> findByUserId(UUID userId);
}
