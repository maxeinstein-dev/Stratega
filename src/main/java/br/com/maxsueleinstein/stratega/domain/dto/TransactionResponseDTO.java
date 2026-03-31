package br.com.maxsueleinstein.stratega.domain.dto;

import br.com.maxsueleinstein.stratega.domain.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(Long id, String description, BigDecimal amount, LocalDateTime date,
                                     TransactionType type, String categoryName) {
}
