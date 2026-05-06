package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.TransactionResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindTransactionsByUserIdUseCase;
import br.com.maxsueleinstein.stratega.domain.model.TransactionType;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FindTransactionsByUserIdUseCaseImpl implements FindTransactionsByUserIdUseCase {

    private final TransactionRepository transactionRepository;
    private final ExpenseGroupRepository groupRepository;

    public FindTransactionsByUserIdUseCaseImpl(TransactionRepository transactionRepository, ExpenseGroupRepository groupRepository) {
        this.transactionRepository = transactionRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<TransactionResponse> execute(UUID userId, Integer month, Integer year) {
        // 1. Transações reais do banco
        List<TransactionResponse> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(tx -> matchDate(tx.getDate(), month, year))
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getDescription(),
                        tx.getAmount(),
                        tx.getNetAmount(),
                        tx.getDate(),
                        tx.getType(),
                        tx.getWalletId(),
                        tx.getCategoryId(),
                        tx.getLinkedTransactionId(),
                        tx.getGroupId()
                ))
                .collect(Collectors.toList());

        // 2. Participações em grupos (Transações Virtuais)
        List<TransactionResponse> virtualTransactions = new ArrayList<>();
        groupRepository.findByUserId(userId).forEach(group -> {
            group.getExpenses().forEach(expense -> {
                // Se eu NÃO sou o pagador, mas estou no split, é um gasto virtual (dívida)
                boolean isPayer = expense.getPaidBy().getUserId().equals(userId);
                
                if (!isPayer && matchDate(expense.getDate(), month, year)) {
                    expense.getSplits().stream()
                        .filter(s -> s.getMember().getUserId().equals(userId))
                        .findFirst()
                        .ifPresent(mySplit -> {
                            virtualTransactions.add(new TransactionResponse(
                                expense.getId(),
                                "Grupo: " + group.getName() + " - " + expense.getDescription(),
                                expense.getTotalAmount(),
                                mySplit.getAmount(), // Meu custo real
                                expense.getDate(),
                                TransactionType.EXPENSE,
                                null, // Sem carteira vinculada (é virtual)
                                null,
                                null,
                                group.getId()
                            ));
                        });
                }
            });
        });

        List<TransactionResponse> all = new ArrayList<>(transactions);
        all.addAll(virtualTransactions);
        
        // Ordenar por data decrescente
        all.sort((a, b) -> b.date().compareTo(a.date()));
        
        return all;
    }

    private boolean matchDate(java.time.LocalDateTime date, Integer month, Integer year) {
        if (month == null && year == null) return true;
        if (date == null) return false;
        boolean matchMonth = month == null || date.getMonthValue() == month;
        boolean matchYear = year == null || date.getYear() == year;
        return matchMonth && matchYear;
    }
}
