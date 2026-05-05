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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OfxTransactionParser implements TransactionParser {

    private static final Pattern TRNAMT_PATTERN = Pattern.compile("<TRNAMT>([^\\r\\n<]+)");
    private static final Pattern DTPOSTED_PATTERN = Pattern.compile("<DTPOSTED>([^\\r\\n<]+)");
    private static final Pattern MEMO_PATTERN = Pattern.compile("<MEMO>([^\\r\\n<]+)");

    @Override
    public List<Transaction> parse(InputStream inputStream, UUID walletId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String content = reader.lines().collect(Collectors.joining("\n"));

            // OFX tags sometimes don't have closing tags, making regex parsing tricky.
            // But usually <STMTTRN> is closed. If not, we split by <STMTTRN>
            String[] rawTransactions = content.split("<STMTTRN>");
            List<Transaction> transactions = new ArrayList<>();

            for (int i = 1; i < rawTransactions.length; i++) {
                String block = rawTransactions[i];

                String amtStr = extractData(TRNAMT_PATTERN, block);
                String dtPostedStr = extractData(DTPOSTED_PATTERN, block);
                String memoStr = extractData(MEMO_PATTERN, block);

                if (amtStr != null && dtPostedStr != null) {
                    BigDecimal amount = new BigDecimal(amtStr.replace(",", "."));

                    TransactionType type = amount.compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.INCOME
                            : TransactionType.EXPENSE;
                    amount = amount.abs(); // Keep positive value

                    LocalDateTime date = parseDate(dtPostedStr);
                    String description = memoStr != null && !memoStr.trim().isEmpty() ? memoStr.trim()
                            : "Importação OFX";

                    transactions.add(new Transaction(
                            null,
                            description,
                            amount,
                            null, // netAmount
                            date,
                            type,
                            walletId,
                            null, // Category will be null initially as requested
                            null));
                }
            }
            return transactions;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar arquivo OFX: " + e.getMessage(), e);
        }
    }

    private String extractData(Pattern pattern, String block) {
        Matcher matcher = pattern.matcher(block);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            // OFX Date format is usually YYYYMMDDHHMMSS.XXX[gmt offset:tz name]
            // We just extract the first 14 characters YYYYMMDDHHMMSS
            if (dateStr.length() >= 14) {
                String cleanDate = dateStr.substring(0, 14);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                return LocalDateTime.parse(cleanDate, formatter);
            } else if (dateStr.length() >= 8) {
                String cleanDate = dateStr.substring(0, 8);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                return java.time.LocalDate.parse(cleanDate, formatter).atStartOfDay();
            }
        } catch (Exception e) {
            // Log or handle parsing error if needed
        }
        return LocalDateTime.now();
    }
}
