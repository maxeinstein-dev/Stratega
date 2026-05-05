package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Transfer;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.TransferEntity;

public class TransferMapper {

    public static TransferEntity toEntity(Transfer domain) {
        if (domain == null) return null;
        return TransferEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .fromWalletId(domain.getFromWalletId())
                .toWalletId(domain.getToWalletId())
                .amount(domain.getAmount())
                .description(domain.getDescription())
                .date(domain.getDate())
                .transactionOutId(domain.getTransactionOutId())
                .transactionInId(domain.getTransactionInId())
                .build();
    }

    public static Transfer toDomain(TransferEntity entity) {
        if (entity == null) return null;
        return new Transfer(
                entity.getId(),
                entity.getUserId(),
                entity.getFromWalletId(),
                entity.getToWalletId(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getDate(),
                entity.getTransactionOutId(),
                entity.getTransactionInId()
        );
    }
}
