package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.*;
import br.com.maxsueleinstein.stratega.application.usecase.AddGroupExpenseUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.CalculateGroupBalancesUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.CreateGroupUseCase;
import br.com.maxsueleinstein.stratega.domain.model.ExpenseGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final CreateGroupUseCase createGroupUseCase;
    private final AddGroupExpenseUseCase addGroupExpenseUseCase;
    private final CalculateGroupBalancesUseCase calculateGroupBalancesUseCase;
    private final br.com.maxsueleinstein.stratega.application.usecase.FindGroupsByUserIdUseCase findGroupsByUserIdUseCase;
    private final br.com.maxsueleinstein.stratega.application.usecase.SettleGroupDebtUseCase settleGroupDebtUseCase;

    public GroupController(CreateGroupUseCase createGroupUseCase, 
                           AddGroupExpenseUseCase addGroupExpenseUseCase, 
                           CalculateGroupBalancesUseCase calculateGroupBalancesUseCase,
                           br.com.maxsueleinstein.stratega.application.usecase.FindGroupsByUserIdUseCase findGroupsByUserIdUseCase,
                           br.com.maxsueleinstein.stratega.application.usecase.SettleGroupDebtUseCase settleGroupDebtUseCase) {
        this.createGroupUseCase = createGroupUseCase;
        this.addGroupExpenseUseCase = addGroupExpenseUseCase;
        this.calculateGroupBalancesUseCase = calculateGroupBalancesUseCase;
        this.findGroupsByUserIdUseCase = findGroupsByUserIdUseCase;
        this.settleGroupDebtUseCase = settleGroupDebtUseCase;
    }
 
    @PostMapping
    public ResponseEntity<GroupResponse> create(@RequestBody CreateGroupRequest request) {
        // Automatically set the ownerId to the logged in user if not provided or to enforce security
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());
        
        CreateGroupRequest finalRequest = new CreateGroupRequest(request.name(), userId, request.memberNames());
        ExpenseGroup group = createGroupUseCase.execute(finalRequest);
        return ResponseEntity.ok(toResponse(group));
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getGroups() {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());
        
        List<GroupResponse> groups = findGroupsByUserIdUseCase.execute(userId);
        return ResponseEntity.ok(groups);
    }
 
    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<GroupResponse> addExpense(@PathVariable UUID groupId, @RequestBody AddGroupExpenseRequest request) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        AddGroupExpenseRequest finalRequest = new AddGroupExpenseRequest(
            groupId,
            userId,
            request.description(),
            request.amount(),
            request.paidByMemberId(),
            request.date(),
            request.splitType(),
            request.splitValues()
        );
        ExpenseGroup group = addGroupExpenseUseCase.execute(finalRequest);
        return ResponseEntity.ok(toResponse(group));
    }
 
    @GetMapping("/{groupId}/balances")
    public ResponseEntity<GroupBalancesResponse> getBalances(@PathVariable UUID groupId) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());

        return ResponseEntity.ok(calculateGroupBalancesUseCase.execute(groupId, userId));
    }
    @PostMapping("/{groupId}/settle")
    public ResponseEntity<Void> settleDebt(@PathVariable UUID groupId, @RequestBody SettleDebtRequest request) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        java.util.UUID userId = java.util.UUID.fromString((String) authentication.getPrincipal());
 
        settleGroupDebtUseCase.execute(groupId, userId, request);
        return ResponseEntity.noContent().build();
    }
 
    private GroupResponse toResponse(ExpenseGroup group) {
        List<MemberResponse> members = group.getMembers().stream()
            .map(m -> new MemberResponse(m.getId(), m.getName(), m.getUserId()))
            .toList();
        return new GroupResponse(group.getId(), group.getName(), group.getOwnerId(), members);
    }
}
