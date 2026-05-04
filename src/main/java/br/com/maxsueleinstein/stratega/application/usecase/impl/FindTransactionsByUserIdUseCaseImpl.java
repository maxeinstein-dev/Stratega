package br.com.maxsueleinstein.stratega.application.usecase.impl;
 
import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
 
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
 
public class FindTransactionsByUserIdUseCaseImpl implements FindTransactionsByUserIdUseCase {
 
    private final TransactionRepository transactionRepository;
 
    public FindTransactionsByUserIdUseCaseImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
 
    @Override
    public List<TransactionResponse> execute(UUID userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getDescription(),
                        tx.getAmount(),
                        tx.getDate(),
                        tx.getType(),
                        tx.getWalletId(),
                        tx.getCategoryId(),
                        tx.getLinkedTransactionId()
                ))
                .collect(Collectors.toList());
    }
}
