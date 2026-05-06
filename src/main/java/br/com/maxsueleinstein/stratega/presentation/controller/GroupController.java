package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.*;
import br.com.maxsueleinstein.stratega.application.usecase.*;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import br.com.maxsueleinstein.stratega.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@Tag(name = "Groups", description = "Endpoints para gerenciamento de grupos de despesas")
public class GroupController {

    private final CreateGroupUseCase createGroupUseCase;
    private final AddGroupExpenseUseCase addGroupExpenseUseCase;
    private final CalculateGroupBalancesUseCase calculateGroupBalancesUseCase;
    private final FindGroupsByUserIdUseCase findGroupsByUserIdUseCase;
    private final SettleGroupDebtUseCase settleGroupDebtUseCase;
    private final DeleteGroupUseCase deleteGroupUseCase;
    private final FindGroupByIdUseCase findGroupByIdUseCase;
    private final FindGroupMovementsUseCase findGroupMovementsUseCase;

    public GroupController(CreateGroupUseCase createGroupUseCase, 
                           AddGroupExpenseUseCase addGroupExpenseUseCase, 
                           CalculateGroupBalancesUseCase calculateGroupBalancesUseCase,
                           FindGroupsByUserIdUseCase findGroupsByUserIdUseCase,
                           SettleGroupDebtUseCase settleGroupDebtUseCase,
                           DeleteGroupUseCase deleteGroupUseCase,
                           FindGroupByIdUseCase findGroupByIdUseCase,
                           FindGroupMovementsUseCase findGroupMovementsUseCase) {
        this.createGroupUseCase = createGroupUseCase;
        this.addGroupExpenseUseCase = addGroupExpenseUseCase;
        this.calculateGroupBalancesUseCase = calculateGroupBalancesUseCase;
        this.findGroupsByUserIdUseCase = findGroupsByUserIdUseCase;
        this.settleGroupDebtUseCase = settleGroupDebtUseCase;
        this.deleteGroupUseCase = deleteGroupUseCase;
        this.findGroupByIdUseCase = findGroupByIdUseCase;
        this.findGroupMovementsUseCase = findGroupMovementsUseCase;
    }

    @GetMapping("/{groupId}/movements")
    @Operation(summary = "Obter histórico de movimentações (despesas e acertos) do grupo")
    public ResponseEntity<List<GroupMovementResponse>> getGroupMovements(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal User user) {
        List<GroupMovementResponse> movements = findGroupMovementsUseCase.execute(groupId, user.getId());
        return ResponseEntity.ok(movements);
    }
 
    @PostMapping
    @Operation(summary = "Criar novo grupo de despesas")
    public ResponseEntity<GroupResponse> create(
            @AuthenticationPrincipal User user,
            @RequestBody CreateGroupRequest request) {
        
        CreateGroupRequest finalRequest = new CreateGroupRequest(
            request.name(), 
            user.getId(), 
            user.getName(), 
            request.memberNames()
        );
        ExpenseGroup group = createGroupUseCase.execute(finalRequest);
        return ResponseEntity.ok(toResponse(group));
    }

    @GetMapping
    @Operation(summary = "Listar grupos do usuário autenticado")
    public ResponseEntity<List<GroupResponse>> getGroups(@AuthenticationPrincipal User user) {
        List<GroupResponse> groups = findGroupsByUserIdUseCase.execute(user.getId());
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Obter detalhes de um grupo específico")
    public ResponseEntity<GroupResponse> getGroupById(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal User user) {
        ExpenseGroup group = findGroupByIdUseCase.execute(groupId, user.getId());
        return ResponseEntity.ok(toResponse(group));
    }
 
    @PostMapping("/{groupId}/expenses")
    @Operation(summary = "Adicionar despesa ao grupo")
    public ResponseEntity<GroupResponse> addExpense(
            @PathVariable UUID groupId, 
            @AuthenticationPrincipal User user,
            @RequestBody AddGroupExpenseRequest request) {

        AddGroupExpenseRequest finalRequest = new AddGroupExpenseRequest(
            groupId,
            user.getId(),
            request.description(),
            request.amount(),
            request.paidByMemberId(),
            request.walletId(),
            request.date(),
            request.splitType(),
            request.splitValues()
        );
        ExpenseGroup group = addGroupExpenseUseCase.execute(finalRequest);
        return ResponseEntity.ok(toResponse(group));
    }
 
    @GetMapping("/{groupId}/balances")
    @Operation(summary = "Calcular balanço e transferências sugeridas")
    public ResponseEntity<GroupBalancesResponse> getBalances(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(calculateGroupBalancesUseCase.execute(groupId, user.getId()));
    }

    @PostMapping("/{groupId}/settle")
    @Operation(summary = "Liquidar dívida e registrar entrada em carteira")
    public ResponseEntity<Void> settleDebt(
            @PathVariable UUID groupId, 
            @AuthenticationPrincipal User user,
            @RequestBody SettleDebtRequest request) {
 
        settleGroupDebtUseCase.execute(groupId, user.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "Excluir um grupo de despesas")
    public ResponseEntity<Void> delete(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal User user) {
        deleteGroupUseCase.execute(groupId, user.getId());
        return ResponseEntity.noContent().build();
    }
 
    private GroupResponse toResponse(ExpenseGroup group) {
        List<MemberResponse> members = group.getMembers().stream()
            .map(m -> new MemberResponse(m.getId(), m.getName(), m.getUserId()))
            .toList();
        return new GroupResponse(group.getId(), group.getName(), group.getOwnerId(), members);
    }
}
