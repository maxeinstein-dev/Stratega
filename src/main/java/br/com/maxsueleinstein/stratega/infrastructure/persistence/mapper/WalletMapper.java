package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.WalletEntity;

public class WalletMapper {

    public static Wallet toDomain(WalletEntity entity) {
        if (entity == null) return null;
        return new Wallet(entity.getId(), entity.getName(), entity.getBalance(), entity.getUserId(), entity.isActive());
    }

    public static WalletEntity toEntity(Wallet domain) {
        if (domain == null) return null;
        return WalletEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .balance(domain.getBalance())
                .userId(domain.getUserId())
                .active(domain.isActive())
                .build();
    }
}
