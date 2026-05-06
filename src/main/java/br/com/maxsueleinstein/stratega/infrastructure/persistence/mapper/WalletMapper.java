package br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper;

import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.model.Wallet;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.WalletEntity;

public class WalletMapper {

    public static Wallet toDomain(WalletEntity entity) {
        if (entity == null) return null;
        Currency currency = entity.getCurrency() != null 
            ? Currency.valueOf(entity.getCurrency()) 
            : Currency.BRL;
            
        return new Wallet(entity.getId(), entity.getName(), entity.getBalance(), entity.getUserId(), currency, entity.isActive(), entity.isAllowNegativeBalance());
    }

    public static WalletEntity toEntity(Wallet domain) {
        if (domain == null) return null;
        return WalletEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .balance(domain.getBalance())
                .userId(domain.getUserId())
                .currency(domain.getCurrency().name())
                .active(domain.isActive())
                .allowNegativeBalance(domain.isAllowNegativeBalance())
                .build();
    }
}
