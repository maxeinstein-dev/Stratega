package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTransferRepository extends JpaRepository<TransferEntity, UUID> {
    @Query("SELECT t FROM TransferEntity t WHERE t.transactionOutId = :txId OR t.transactionInId = :txId")
    Optional<TransferEntity> findByTransactionId(UUID txId);
}
