package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByWalletId(UUID walletId);

    @Query("SELECT t FROM TransactionEntity t JOIN WalletEntity w ON t.walletId = w.id WHERE w.userId = :userId ORDER BY t.date DESC")
    List<TransactionEntity> findByUserId(@Param("userId") UUID userId);
}
