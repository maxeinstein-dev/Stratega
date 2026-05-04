package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransactionEntity;

public class TransactionMapper {

    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;
        return new Transaction(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getDate(),
                entity.getType(),
                entity.getWalletId(),
                entity.getCategoryId(),
                entity.getLinkedTransactionId()
        );
    }

    public static TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;
        return TransactionEntity.builder()
                .id(domain.getId())
                .description(domain.getDescription())
                .amount(domain.getAmount())
                .date(domain.getDate())
                .type(domain.getType())
                .walletId(domain.getWalletId())
                .categoryId(domain.getCategoryId())
                .linkedTransactionId(domain.getLinkedTransactionId())
                .build();
    }
}
