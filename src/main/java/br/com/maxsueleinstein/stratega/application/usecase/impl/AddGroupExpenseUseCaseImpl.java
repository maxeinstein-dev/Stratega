package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.AddGroupExpenseRequest;
import br.com.maxsueleinstein.stratega.application.usecase.AddGroupExpenseUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroupMember;
import br.com.maxsueleinstein.stratega.domain.model.GroupExpense;
import br.com.maxsueleinstein.stratega.domain.model.split.*;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddGroupExpenseUseCaseImpl implements AddGroupExpenseUseCase {

    private final ExpenseGroupRepository repository;
    private final br.com.maxsueleinstein.stratega.domain.repository.WalletRepository walletRepository;
    private final br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository transactionRepository;

    public AddGroupExpenseUseCaseImpl(ExpenseGroupRepository repository,
                                    br.com.maxsueleinstein.stratega.domain.repository.WalletRepository walletRepository,
                                    br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository transactionRepository) {
        this.repository = repository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public ExpenseGroup execute(AddGroupExpenseRequest request) {
        ExpenseGroup group = repository.findById(request.groupId())
                .orElseThrow(() -> new br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException("Grupo não encontrado"));

        if (!group.isUserAllowed(request.requesterId())) {
            throw new br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException("Usuário não tem permissão para adicionar despesas neste grupo");
        }

        ExpenseGroupMember paidBy = group.getMembers().stream()
                .filter(m -> m.getId().equals(request.paidByMemberId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro pagador não encontrado no grupo"));

        List<Split> splits = new ArrayList<>();
        SplitStrategy strategy = switch (request.splitType()) {
            case "UNIFORM" -> {
                for (ExpenseGroupMember member : group.getMembers()) {
                    splits.add(new UniformSplit(member));
                }
                yield new UniformSplitStrategy();
            }
            case "EXACT" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, UUID.fromString(memberId.toString()));
                    splits.add(new ExactSplit(member, new BigDecimal(value.toString())));
                });
                yield new ExactSplitStrategy();
            }
            case "PERCENTAGE" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, UUID.fromString(memberId.toString()));
                    splits.add(new PercentageSplit(member, Double.parseDouble(value.toString())));
                });
                yield new PercentageSplitStrategy();
            }
            case "SHARE" -> {
                request.splitValues().forEach((memberId, value) -> {
                    ExpenseGroupMember member = findMember(group, UUID.fromString(memberId.toString()));
                    splits.add(new ShareSplit(member, Integer.parseInt(value.toString())));
                });
                yield new ShareSplitStrategy();
            }
            default -> throw new IllegalArgumentException("Tipo de divisão inválido");
        };

        GroupExpense expense = new GroupExpense(null, request.description(), request.amount(), paidBy, request.date(), splits, strategy, "EXPENSE");
        group.addExpense(expense);

        // 2. Impacto no Mundo Real: Se houver walletId, tira da carteira
        if (request.walletId() != null) {
            br.com.maxsueleinstein.stratega.domain.model.Wallet wallet = walletRepository.findById(request.walletId())
                    .orElseThrow(() -> new br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException("Carteira não encontrada"));
            
            if (!wallet.getUserId().equals(request.requesterId())) {
                throw new br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException("A carteira deve pertencer a você");
            }

            // Detectar overdraft: se carteira restrita e operação causaria saldo negativo,
            // converter automaticamente e notificar
            if (!wallet.isAllowNegativeBalance() && wallet.wouldGoNegative(request.amount())) {
                wallet.enableOverdraft();
            }

            wallet.removeFunds(request.amount());
            walletRepository.save(wallet);

            br.com.maxsueleinstein.stratega.domain.model.Transaction tx = new br.com.maxsueleinstein.stratega.domain.model.Transaction(
                    null,
                    "Gasto em Grupo: " + request.description(),
                    request.amount(),
                    null,
                    request.date() != null ? request.date() : java.time.LocalDateTime.now(),
                    br.com.maxsueleinstein.stratega.domain.model.TransactionType.EXPENSE,
                    wallet.getId(),
                    null,
                    null, // linkedTransactionId
                    group.getId()); // groupId
            transactionRepository.save(tx);
        }

        return repository.save(group);
    }

    private ExpenseGroupMember findMember(ExpenseGroup group, UUID memberId) {
        return group.getMembers().stream()
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membro " + memberId + " não encontrado no grupo"));
    }
}
