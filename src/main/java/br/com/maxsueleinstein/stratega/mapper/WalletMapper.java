package br.com.maxsueleinstein.stratega.mapper;

import br.com.maxsueleinstein.stratega.domain.dto.WalletResponseDTO;
import br.com.maxsueleinstein.stratega.domain.entity.User;
import br.com.maxsueleinstein.stratega.domain.entity.Wallet;

public class WalletMapper {

    public static Wallet toEntity(WalletResponseDTO dto, User user) {
        return new Wallet(
                dto.name(), dto.type(), dto.balance(), user
        );
    }

    public static WalletResponseDTO toResponse(Wallet wallet) {
        return new WalletResponseDTO(
                wallet.getId(), wallet.getName(), wallet.getType(), wallet.getBalance()
        );
    }
}
