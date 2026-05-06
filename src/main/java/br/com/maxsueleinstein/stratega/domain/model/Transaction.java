package br.com.maxsueleinstein.stratega.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a financial transaction.
 */
public class Transaction {

    private final UUID id;
    private String description;
    private BigDecimal amount;
    private BigDecimal netAmount; // Valor líquido (cota real do usuário em despesas de grupo). Null = usa amount.
    private LocalDateTime date;
    private final TransactionType type;
    private UUID walletId;
    private UUID categoryId;
    private final UUID linkedTransactionId; // Used for transfers
    private final UUID groupId; // Opcional: Para transações originadas em grupos

    public Transaction(UUID id, String description, BigDecimal amount, LocalDateTime date,
            TransactionType type, UUID walletId, UUID categoryId, UUID linkedTransactionId) {
        this(id, description, amount, null, date, type, walletId, categoryId, linkedTransactionId, null);
    }

    public Transaction(UUID id, String description, BigDecimal amount, BigDecimal netAmount, LocalDateTime date,
            TransactionType type, UUID walletId, UUID categoryId, UUID linkedTransactionId) {
        this(id, description, amount, netAmount, date, type, walletId, categoryId, linkedTransactionId, null);
    }

    public Transaction(UUID id, String description, BigDecimal amount, BigDecimal netAmount, LocalDateTime date,
            TransactionType type, UUID walletId, UUID categoryId, UUID linkedTransactionId, UUID groupId) {
        validateDescription(description);
        validateAmount(amount);
        validateDate(date);

        if (type == null) {
            throw new IllegalArgumentException("O tipo da transação é obrigatório");
        }
        if (walletId == null) {
            throw new IllegalArgumentException("A carteira da transação é obrigatória");
        }

        this.id = id != null ? id : UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.netAmount = netAmount;
        this.date = date;
        this.type = type;
        this.walletId = walletId;
        this.categoryId = categoryId;
        this.linkedTransactionId = linkedTransactionId;
        this.groupId = groupId;
    }

    public void updateDetails(String description, BigDecimal amount, LocalDateTime date, UUID categoryId, UUID walletId) {
        validateDescription(description);
        validateAmount(amount);
        validateDate(date);
        
        if (walletId == null) {
            throw new IllegalArgumentException("A carteira da transação é obrigatória");
        }

        this.description = description;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.walletId = walletId;
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da transação não pode estar em branco");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero");
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("A data da transação é obrigatória");
        }
    }

    public boolean isIncome() {
        return type == TransactionType.INCOME || type == TransactionType.TRANSFER_IN;
    }

    public boolean isExpense() {
        return type == TransactionType.EXPENSE || type == TransactionType.TRANSFER_OUT;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    /** Retorna o valor líquido (cota real) se definido, ou o valor total caso contrário. */
    public BigDecimal getEffectiveAmount() {
        return netAmount != null ? netAmount : amount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getLinkedTransactionId() {
        return linkedTransactionId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
