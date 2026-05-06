package br.com.maxsueleinstein.stratega.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Evento disparado quando uma carteira com allowNegativeBalance=false
 * entra em overdraft automaticamente durante uma operação financeira.
 */
public class OverdraftActivatedEvent {

    private final UUID userId;
    private final String walletName;
    private final BigDecimal balanceBefore;
    private final BigDecimal operationAmount;

    public OverdraftActivatedEvent(UUID userId, String walletName, BigDecimal balanceBefore, BigDecimal operationAmount) {
        this.userId = userId;
        this.walletName = walletName;
        this.balanceBefore = balanceBefore;
        this.operationAmount = operationAmount;
    }

    public UUID getUserId() { return userId; }
    public String getWalletName() { return walletName; }
    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public BigDecimal getOperationAmount() { return operationAmount; }
}
