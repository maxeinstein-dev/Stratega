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

    public GroupController(CreateGroupUseCase createGroupUseCase, 
                           AddGroupExpenseUseCase addGroupExpenseUseCase, 
                           CalculateGroupBalancesUseCase calculateGroupBalancesUseCase) {
        this.createGroupUseCase = createGroupUseCase;
        this.addGroupExpenseUseCase = addGroupExpenseUseCase;
        this.calculateGroupBalancesUseCase = calculateGroupBalancesUseCase;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> create(@RequestBody CreateGroupRequest request) {
        ExpenseGroup group = createGroupUseCase.execute(request);
        return ResponseEntity.ok(toResponse(group));
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<GroupResponse> addExpense(@PathVariable UUID groupId, @RequestBody AddGroupExpenseRequest request) {
        AddGroupExpenseRequest finalRequest = new AddGroupExpenseRequest(
            groupId,
            request.description(),
            request.amount(),
            request.paidByMemberId(),
            request.splitType(),
            request.splitValues()
        );
        ExpenseGroup group = addGroupExpenseUseCase.execute(finalRequest);
        return ResponseEntity.ok(toResponse(group));
    }

    @GetMapping("/{groupId}/balances")
    public ResponseEntity<GroupBalancesResponse> getBalances(@PathVariable UUID groupId) {
        return ResponseEntity.ok(calculateGroupBalancesUseCase.execute(groupId));
    }

    private GroupResponse toResponse(ExpenseGroup group) {
        List<MemberResponse> members = group.getMembers().stream()
            .map(m -> new MemberResponse(m.getId(), m.getName(), m.getUserId()))
            .toList();
        return new GroupResponse(group.getId(), group.getName(), group.getOwnerId(), members);
    }
}
