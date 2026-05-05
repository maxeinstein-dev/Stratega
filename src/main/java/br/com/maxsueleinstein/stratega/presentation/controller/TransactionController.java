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
    private final br.com.maxsueleinstein.stratega.application.usecase.ImportTransactionsUseCase importTransactionsUseCase;
    private final br.com.maxsueleinstein.stratega.application.usecase.DeleteTransactionUseCase deleteTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
            TransferFundsUseCase transferFundsUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.UpdateTransactionUseCase updateTransactionUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.ImportTransactionsUseCase importTransactionsUseCase,
            br.com.maxsueleinstein.stratega.application.usecase.DeleteTransactionUseCase deleteTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.findTransactionsByUserIdUseCase = findTransactionsByUserIdUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.importTransactionsUseCase = importTransactionsUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<java.util.List<TransactionResponse>> createTransaction(@RequestBody CreateTransactionRequest request) {
        java.util.List<TransactionResponse> response = createTransactionUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(@RequestBody TransferFundsRequest request) {
        transferFundsUseCase.execute(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/import", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<java.util.Map<String, Object>> importTransactions(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("walletId") java.util.UUID walletId) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        int importedCount = importTransactionsUseCase.execute(userId, walletId, file);
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Importação realizada com sucesso.");
        response.put("importedTransactions", importedCount);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<java.util.List<TransactionResponse>> getTransactions(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        java.util.List<TransactionResponse> transactions = findTransactionsByUserIdUseCase.execute(userId, month, year);
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

    @GetMapping("/export")
    public ResponseEntity<String> exportTransactions(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        java.util.List<TransactionResponse> transactions = findTransactionsByUserIdUseCase.execute(userId, month, year);

        StringBuilder csv = new StringBuilder("ID,Data,Descrição,Valor,Tipo,Categoria\n");
        for (TransactionResponse tx : transactions) {
            csv.append(tx.id()).append(",")
               .append(tx.date() != null ? tx.date().toString() : "").append(",")
               .append("\"").append(tx.description()).append("\",")
               .append(tx.amount()).append(",")
               .append(tx.type()).append(",")
               .append(tx.categoryId() != null ? tx.categoryId().toString() : "")
               .append("\n");
        }

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv");
        headers.add(org.springframework.http.HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return new ResponseEntity<>(csv.toString(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable java.util.UUID id) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        deleteTransactionUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
