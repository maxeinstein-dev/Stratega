package br.com.maxsueleinstein.stratega.domain.dto;

import br.com.maxsueleinstein.stratega.domain.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequestDTO(Long userId,
                                    String description,
                                    BigDecimal amount,
                                    LocalDateTime date,
                                    TransactionType type,
                                    Long walletId,
                                    Long destinationWalletId,
                                    String categoryName
) {
}
