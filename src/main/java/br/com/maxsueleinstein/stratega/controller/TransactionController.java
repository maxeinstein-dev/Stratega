package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.dto.TransactionRequestDTO;
import br.com.maxsueleinstein.stratega.domain.dto.TransactionResponseDTO;
import br.com.maxsueleinstein.stratega.service.TransactionService;
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
    public ResponseEntity<TransactionResponseDTO> createTransactions(@RequestBody TransactionRequestDTO transaction) {
        TransactionResponseDTO response = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public void getTransactions() {

    }

    @PostMapping("/transfer")
    public void createTransfer() {

    }
}
