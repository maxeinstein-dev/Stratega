package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;
import br.com.maxsueleinstein.stratega.application.dto.UpdateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.usecase.*;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Endpoints para gerenciamento de movimentações financeiras")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final ImportTransactionsUseCase importTransactionsUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
            TransferFundsUseCase transferFundsUseCase,
            FindTransactionsByUserIdUseCase findTransactionsByUserIdUseCase,
            UpdateTransactionUseCase updateTransactionUseCase,
            ImportTransactionsUseCase importTransactionsUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.findTransactionsByUserIdUseCase = findTransactionsByUserIdUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.importTransactionsUseCase = importTransactionsUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar nova transação (Receita ou Despesa)")
    public ResponseEntity<List<TransactionResponse>> createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody CreateTransactionRequest request) {
        List<TransactionResponse> response = createTransactionUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Realizar transferência entre carteiras")
    public ResponseEntity<Void> transferFunds(
            @AuthenticationPrincipal User user,
            @RequestBody TransferFundsRequest request) {
        transferFundsUseCase.execute(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importar extrato bancário (OFX ou CSV)")
    public ResponseEntity<Map<String, Object>> importTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            @RequestParam("walletId") UUID walletId) {

        int importedCount = importTransactionsUseCase.execute(user.getId(), walletId, file);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Importação realizada com sucesso.");
        response.put("importedTransactions", importedCount);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar transações do usuário com filtros opcionais")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        List<TransactionResponse> transactions = findTransactionsByUserIdUseCase.execute(user.getId(), month, year);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar uma transação existente")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateTransactionRequest request) {
        TransactionResponse response = updateTransactionUseCase.execute(id, user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export")
    @Operation(summary = "Exportar transações do usuário para CSV")
    public ResponseEntity<String> exportTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        List<TransactionResponse> transactions = findTransactionsByUserIdUseCase.execute(user.getId(), month, year);

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

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return new ResponseEntity<>(csv.toString(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma transação e reverter impacto no saldo")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        deleteTransactionUseCase.execute(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
