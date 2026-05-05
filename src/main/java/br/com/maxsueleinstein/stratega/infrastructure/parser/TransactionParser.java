package br.com.maxsueleinstein.stratega.infrastructure.parser;

import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface TransactionParser {
    List<Transaction> parse(InputStream inputStream, UUID walletId);
}
