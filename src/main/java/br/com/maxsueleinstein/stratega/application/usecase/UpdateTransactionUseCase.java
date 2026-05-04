package br.com.maxsueleinstein.stratega.application.usecase;
 
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.dto.UpdateTransactionRequest;
 
import java.util.UUID;
 
public interface UpdateTransactionUseCase {
    TransactionResponse execute(UUID transactionId, UUID requesterId, UpdateTransactionRequest request);
}
