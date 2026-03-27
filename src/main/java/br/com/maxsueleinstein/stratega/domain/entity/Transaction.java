package br.com.maxsueleinstein.stratega.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "Conta de energia")
    private String description;

    @Schema(example = "150.50")
    private BigDecimal amount;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Schema(example = "EXPENSE")
    private TransactionType type;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @Schema(example = "{\"id\":1}")
    private Wallet wallet;
    @ManyToOne
    private Wallet destinationWallet;
    @ManyToOne
    @Schema(example = "{\"name\":\"Casa\",\"type\":\"EXPENSE\"}")
    private Category category;
    @ManyToOne
    @Schema(example = "{\"id\":1}")
    private User user;

    public Transaction() {
    }

    public Transaction(String description, BigDecimal amount, LocalDateTime date, TransactionType type, Wallet wallet, Category category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.wallet = wallet;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getDestinationWallet() {
        return destinationWallet;
    }

    public void setDestinationWallet(Wallet destinationWallet) {
        this.destinationWallet = destinationWallet;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
