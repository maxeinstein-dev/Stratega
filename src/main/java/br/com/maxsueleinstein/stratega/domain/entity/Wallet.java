package br.com.maxsueleinstein.stratega.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "Carteira Casa")
    private String name;

    @Schema(example = "HOUSE")
    private String type;

    @Schema(example = "0")
    private BigDecimal balance;
    @ManyToOne
    @Schema(example = "{\"id\":1}")
    private User user;

    public Wallet() {

    }

    public Wallet(String name, String type, BigDecimal balance, User user) {
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
