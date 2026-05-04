package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;

public interface CreateTransactionUseCase {
    TransactionResponse execute(CreateTransactionRequest request);
}
