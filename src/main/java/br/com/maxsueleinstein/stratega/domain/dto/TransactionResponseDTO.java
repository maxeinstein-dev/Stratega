package br.com.maxsueleinstein.stratega.domain.dto;

import br.com.maxsueleinstein.stratega.domain.entity.TransactionType;

import java.math.BigDecimal;

public record TransactionResponseDTO(Long id, String description, BigDecimal amount,
                                     TransactionType type, BigDecimal walletBalance) {
}
