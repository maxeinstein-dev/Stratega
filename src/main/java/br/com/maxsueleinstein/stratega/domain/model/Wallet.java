package br.com.maxsueleinstein.stratega.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a Wallet.
 * Handles the logic of adding and removing funds.
 */
public class Wallet {

    private final UUID id;
    private String name;
    private BigDecimal balance;
    private final UUID userId;
    private final Currency currency;
    private boolean active;
    private boolean allowNegativeBalance;

    public Wallet(UUID id, String name, BigDecimal initialBalance, UUID userId) {
        this(id, name, initialBalance, userId, Currency.BRL, true, true);
    }

    public Wallet(UUID id, String name, BigDecimal initialBalance, UUID userId, Currency currency, boolean active) {
        this(id, name, initialBalance, userId, currency, active, true);
    }

    public Wallet(UUID id, String name, BigDecimal initialBalance, UUID userId, Currency currency, boolean active, boolean allowNegativeBalance) {
        validateName(name);
        if (userId == null) {
            throw new IllegalArgumentException("O usuário dono da carteira é obrigatório");
        }

        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.balance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        this.userId = userId;
        this.currency = currency != null ? currency : Currency.BRL;
        this.active = active;
        this.allowNegativeBalance = allowNegativeBalance;
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public void addFunds(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor a ser adicionado deve ser maior que zero");
        }
        this.balance = this.balance.add(amount);
    }

    public void removeFunds(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor a ser removido deve ser maior que zero");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void archive() {
        this.active = false;
    }

    /**
     * Retorna true se a operação de remoção causaria saldo negativo.
     * Usado pelos UseCases para detectar overdraft antes de executar.
     */
    public boolean wouldGoNegative(BigDecimal amount) {
        if (amount == null) return false;
        return this.balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Converte a carteira para modo overdraft (permite saldo negativo).
     * Chamado automaticamente pelos UseCases quando uma operação causaria overdraft
     * em uma carteira com allowNegativeBalance = false.
     */
    public void enableOverdraft() {
        this.allowNegativeBalance = true;
    }

    /**
     * Permite alterar a política de saldo negativo da carteira.
     */
    public void setAllowNegativeBalance(boolean allowNegativeBalance) {
        this.allowNegativeBalance = allowNegativeBalance;
    }

    public boolean isActive() {
        return active;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da carteira não pode estar em branco");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getUserId() {
        return userId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isAllowNegativeBalance() {
        return allowNegativeBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
