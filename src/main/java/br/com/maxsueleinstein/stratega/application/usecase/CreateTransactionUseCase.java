package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.CreateTransactionRequest;
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import java.util.List;

public interface CreateTransactionUseCase {
    List<TransactionResponse> execute(CreateTransactionRequest request);
}
