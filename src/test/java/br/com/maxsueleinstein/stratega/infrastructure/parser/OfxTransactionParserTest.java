package br.com.maxsueleinstein.stratega.infrastructure.parser;

import br.com.maxsueleinstein.stratega.domain.model.Transaction;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfxTransactionParserTest {

    private final OfxTransactionParser parser = new OfxTransactionParser();

    @Test
    @DisplayName("Deve fazer o parse de transações OFX com sucesso")
    void shouldParseOfxTransactions() {
        String ofxContent = """
                OFXHEADER:100
                DATA:OFXSGML
                VERSION:102
                <BANKMSGSRSV1>
                  <STMTTRN>
                    <TRNTYPE>DEBIT
                    <DTPOSTED>20260515120000[-3:BRT]
                    <TRNAMT>-200.50
                    <MEMO>Compra Supermercado
                  </STMTTRN>
                  <STMTTRN>
                    <TRNTYPE>CREDIT
                    <DTPOSTED>20260516
                    <TRNAMT>1500.00
                    <MEMO>Salario
                  </STMTTRN>
                </BANKMSGSRSV1>
                """;

        InputStream is = new ByteArrayInputStream(ofxContent.getBytes());
        UUID walletId = UUID.randomUUID();

        List<Transaction> transactions = parser.parse(is, walletId);

        assertEquals(2, transactions.size());

        Transaction t1 = transactions.get(0);
        assertEquals(new BigDecimal("200.50"), t1.getAmount());
        assertEquals(TransactionType.EXPENSE, t1.getType());
        assertEquals("Compra Supermercado", t1.getDescription());
        assertEquals(walletId, t1.getWalletId());
        
        Transaction t2 = transactions.get(1);
        assertEquals(new BigDecimal("1500.00"), t2.getAmount());
        assertEquals(TransactionType.INCOME, t2.getType());
        assertEquals("Salario", t2.getDescription());
    }

    @Test
    @DisplayName("Deve fazer o parse de transações reais do arquivo do Nubank")
    void shouldParseNubankOfx() throws Exception {
        java.io.File file = new java.io.File("C:\\Users\\Maxsuel Einstein\\Documents\\Projetos\\Stratega\\Nubank_2026-05-09.ofx");
        if (!file.exists()) {
            return; // Skip if file doesn't exist in environment
        }

        try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
            UUID walletId = UUID.randomUUID();
            List<Transaction> transactions = parser.parse(fis, walletId);
            
            // Just verifying it parses without throwing exceptions and returns some transactions
            org.junit.jupiter.api.Assertions.assertFalse(transactions.isEmpty(), "Deve extrair transacoes do OFX do Nubank");
            System.out.println("Importadas " + transactions.size() + " transacoes do Nubank!");
        }
    }
}
