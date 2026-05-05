package br.com.maxsueleinstein.stratega.domain.model;

/**
 * Supported currencies in the Stratega system.
 * Based on ISO 4217 codes.
 */
public enum Currency {
    BRL("Real Brasileiro", "R$"),
    USD("Dólar Americano", "$"),
    EUR("Euro", "€"),
    GBP("Libra Esterlina", "£"),
    BTC("Bitcoin", "₿");

    private final String description;
    private final String symbol;

    Currency(String description, String symbol) {
        this.description = description;
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public String getSymbol() {
        return symbol;
    }
}
