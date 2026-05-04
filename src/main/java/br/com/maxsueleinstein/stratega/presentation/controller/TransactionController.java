package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;
import br.com.maxsueleinstein.stratega.application.usecase.CreateTransactionUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.TransferFundsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final TransferFundsUseCase transferFundsUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, TransferFundsUseCase transferFundsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody CreateTransactionRequest request) {
        
        // No momento o userId do header é injetado, mas a requisição pode não usar ativamente ainda
        // a não ser que validássemos se a carteira pertence ao userId logado.
        TransactionResponse response = createTransactionUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody TransferFundsRequest request) {

        transferFundsUseCase.execute(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
