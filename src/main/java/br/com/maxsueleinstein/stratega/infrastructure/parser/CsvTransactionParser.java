package br.com.maxsueleinstein.stratega.infrastructure.parser;

import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CsvTransactionParser implements TransactionParser {

    @Override
    public List<Transaction> parse(InputStream inputStream, UUID walletId) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split("[;,]");
                if (parts.length >= 3) {
                    String dateStr = parts[0].trim();
                    String description = parts[1].trim();
                    String amountStr = parts[2].trim();

                    BigDecimal amount = parseAmount(amountStr);
                    TransactionType type = amount.compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE;
                    amount = amount.abs();

                    LocalDateTime date = parseDate(dateStr);

                    transactions.add(new Transaction(
                            null,
                            description,
                            amount,
                            null,
                            date,
                            type,
                            walletId,
                            null,
                            null
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar arquivo CSV: " + e.getMessage(), e);
        }
        return transactions;
    }

    private BigDecimal parseAmount(String amountStr) {
        // Handle 1.234,56 or 1234.56
        String cleanAmount = amountStr.replace(".", "").replace(",", ".");
        try {
            return new BigDecimal(cleanAmount);
        } catch (Exception e) {
            // Try direct parse if it failed (maybe it was already dots-only)
            return new BigDecimal(amountStr.replace(",", "."));
        }
    }

    private LocalDateTime parseDate(String dateStr) {
        String[] patterns = {"dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy HH:mm:ss"};
        for (String pattern : patterns) {
            try {
                if (pattern.contains("HH")) {
                    return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
                } else {
                    return java.time.LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern)).atStartOfDay();
                }
            } catch (Exception ignored) {}
        }
        return LocalDateTime.now();
    }
}
