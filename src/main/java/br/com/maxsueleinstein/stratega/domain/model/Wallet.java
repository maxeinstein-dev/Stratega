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

    public Wallet(UUID id, String name, BigDecimal initialBalance, UUID userId) {
        this(id, name, initialBalance, userId, Currency.BRL, true);
    }

    public Wallet(UUID id, String name, BigDecimal initialBalance, UUID userId, Currency currency, boolean active) {
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
