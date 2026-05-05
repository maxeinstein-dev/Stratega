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
    public List<TransactionResponse> execute(UUID userId, Integer month, Integer year) {
        return transactionRepository.findByUserId(userId).stream()
                .filter(tx -> {
                    if (month == null && year == null) return true;
                    if (tx.getDate() == null) return false;
                    boolean matchMonth = month == null || tx.getDate().getMonthValue() == month;
                    boolean matchYear = year == null || tx.getDate().getYear() == year;
                    return matchMonth && matchYear;
                })
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getDescription(),
                        tx.getAmount(),
                        tx.getNetAmount(),
                        tx.getDate(),
                        tx.getType(),
                        tx.getWalletId(),
                        tx.getCategoryId(),
                        tx.getLinkedTransactionId()
                ))
                .collect(Collectors.toList());
    }
}
