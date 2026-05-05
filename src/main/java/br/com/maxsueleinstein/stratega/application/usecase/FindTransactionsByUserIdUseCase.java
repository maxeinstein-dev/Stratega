package br.com.maxsueleinstein.stratega.application.usecase;
 
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import java.util.List;
import java.util.UUID;
 
public interface FindTransactionsByUserIdUseCase {
    List<TransactionResponse> execute(UUID userId, Integer month, Integer year);
}
