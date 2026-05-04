package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByWalletId(UUID walletId);
}
