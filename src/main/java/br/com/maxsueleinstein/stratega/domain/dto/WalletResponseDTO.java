package br.com.maxsueleinstein.stratega.domain.dto;

import br.com.maxsueleinstein.stratega.domain.entity.WalletType;

import java.math.BigDecimal;

public record WalletResponseDTO(Long id, String name,
                                WalletType type, BigDecimal balance) {
}
