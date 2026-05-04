package br.com.maxsueleinstein.stratega.domain.model;

import java.math.BigDecimal;

public record SuggestedTransfer(
        ExpenseGroupMember from,
        ExpenseGroupMember to,
        BigDecimal amount
) {}
