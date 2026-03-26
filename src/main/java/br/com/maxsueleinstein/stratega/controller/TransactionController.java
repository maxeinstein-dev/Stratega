package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.entity.Transaction;
import br.com.maxsueleinstein.stratega.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransactions(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public void getTransactions() {

    }

    @PostMapping("/transfer")
    public void createTransfer() {

    }
}
