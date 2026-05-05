package br.com.maxsueleinstein.stratega.domain.service;

import br.com.maxsueleinstein.stratega.domain.model.Currency;
import java.math.BigDecimal;

public interface ExchangeRateService {
    /**
     * Gets the exchange rate from source to target currency.
     * @param from Source currency
     * @param to Target currency
     * @return The exchange rate
     */
    BigDecimal getRate(Currency from, Currency to);

    /**
     * Converts an amount from one currency to another.
     * @param amount The amount to convert
     * @param from Source currency
     * @param to Target currency
     * @return The converted amount
     */
    default BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from == to) return amount;
        return amount.multiply(getRate(from, to));
    }
}
