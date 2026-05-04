package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateTransactionUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.TransferFundsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase;

    private final br.com.maxsueleinstein.stratega.application.usecase.UpdateTransactionUseCase updateTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
            TransferFundsUseCase transferFundsUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.UpdateTransactionUseCase updateTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.findTransactionsByUserIdUseCase = findTransactionsByUserIdUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        TransactionResponse response = createTransactionUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(@RequestBody TransferFundsRequest request) {
        transferFundsUseCase.execute(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<java.util.List<TransactionResponse>> getTransactions() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        java.util.List<TransactionResponse> transactions = findTransactionsByUserIdUseCase.execute(userId);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable java.util.UUID id,
            @RequestBody br.com.maxsueleinstein.stratega.application.dto.UpdateTransactionRequest request) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID requesterId = java.util.UUID.fromString((String) authentication.getPrincipal());

        TransactionResponse response = updateTransactionUseCase.execute(id, requesterId, request);
        return ResponseEntity.ok(response);
    }
}
