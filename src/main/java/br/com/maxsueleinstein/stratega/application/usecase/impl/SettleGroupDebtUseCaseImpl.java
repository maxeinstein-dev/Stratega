package br.com.maxsueleinstein.stratega.application.usecase.impl;

import br.com.maxsueleinstein.stratega.application.dto.SettleDebtRequest;
import br.com.maxsueleinstein.stratega.application.usecase.SettleGroupDebtUseCase;
import br.com.maxsueleinstein.stratega.domain.model.*;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
import br.com.maxsueleinstein.stratega.domain.repository.TransactionRepository;
import br.com.maxsueleinstein.stratega.domain.repository.WalletRepository;
import br.com.maxsueleinstein.stratega.presentation.exception.ForbiddenException;
import br.com.maxsueleinstein.stratega.presentation.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SettleGroupDebtUseCaseImpl implements SettleGroupDebtUseCase {

        private final ExpenseGroupRepository groupRepository;
        private final WalletRepository walletRepository;
        private final TransactionRepository transactionRepository;

        public SettleGroupDebtUseCaseImpl(ExpenseGroupRepository groupRepository,
                        WalletRepository walletRepository,
                        TransactionRepository transactionRepository) {
                this.groupRepository = groupRepository;
                this.walletRepository = walletRepository;
                this.transactionRepository = transactionRepository;
        }

        @Override
        @Transactional
        public void execute(UUID groupId, UUID requesterId, SettleDebtRequest request) {
                ExpenseGroup group = groupRepository.findById(groupId)
                                .orElseThrow(() -> new ResourceNotFoundException("Grupo não encontrado"));

                if (!group.isUserAllowed(requesterId)) {
                        throw new ForbiddenException("Você não tem permissão para gerenciar este grupo");
                }

                // 1. Encontrar membros
                ExpenseGroupMember payer = group.getMembers().stream()
                                .filter(m -> m.getId().equals(request.memberId()))
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Membro pagador não encontrado no grupo"));

                ExpenseGroupMember receiver = group.getMembers().stream()
                                .filter(m -> requesterId.equals(m.getUserId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(
                                                "Membro recebedor (você) não encontrado no grupo"));

                // 2. Impacto no Grupo: Zera/diminui a dívida
                // Criamos uma despesa onde o payer pagou o valor, e o receiver ficou com 100%
                // do custo.
                // Isso aumenta o "crédito" do payer e o "débito" do receiver no grupo.
                GroupExpense settlementExpense = new GroupExpense(
                                UUID.randomUUID(),
                                request.description() != null ? request.description()
                                                : "Liquidação de dívida: " + payer.getName(),
                                request.amount(),
                                payer,
                                LocalDateTime.now(),
                                List.of(new br.com.maxsueleinstein.stratega.domain.model.split.ExactSplit(receiver,
                                                request.amount())),
                                null, // Sem estratégia, split manual
                                "SETTLEMENT"
                );
                group.addExpense(settlementExpense);
                groupRepository.save(group);

                // 3. Impacto no Mundo Real: Injeta na carteira
                Wallet wallet = walletRepository.findById(request.destinationWalletId())
                                .orElseThrow(() -> new ResourceNotFoundException("Carteira de destino não encontrada"));

                if (!wallet.getUserId().equals(requesterId)) {
                        throw new ForbiddenException("A carteira de destino deve pertencer a você");
                }

                wallet.addFunds(request.amount());
                walletRepository.save(wallet);

                Transaction incomeTx = new Transaction(
                                UUID.randomUUID(),
                                "Recebimento de Grupo: " + payer.getName(),
                                request.amount(),
                                null,
                                LocalDateTime.now(),
                                TransactionType.INCOME,
                                wallet.getId(),
                                null, // Categoria opcional
                                null);
                transactionRepository.save(incomeTx);
        }
}
