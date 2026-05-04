package br.com.maxsueleinstein.stratega.application.usecase.impl;
 
import br.com.maxsueleinstein.stratega.application.dto.GroupResponse;
import br.com.maxsueleinstein.stratega.application.dto.MemberResponse;
import br.com.maxsueleinstein.stratega.application.usecase.FindGroupsByUserIdUseCase;
import br.com.maxsueleinstein.stratega.domain.repository.ExpenseGroupRepository;
 
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
 
public class FindGroupsByUserIdUseCaseImpl implements FindGroupsByUserIdUseCase {
 
    private final ExpenseGroupRepository groupRepository;
 
    public FindGroupsByUserIdUseCaseImpl(ExpenseGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
 
    @Override
    public List<GroupResponse> execute(UUID userId) {
        return groupRepository.findByUserId(userId).stream()
                .map(group -> {
                    List<MemberResponse> members = group.getMembers().stream()
                            .map(m -> new MemberResponse(m.getId(), m.getName(), m.getUserId()))
                            .collect(Collectors.toList());
                    return new GroupResponse(group.getId(), group.getName(), group.getOwnerId(), members);
                })
                .collect(Collectors.toList());
    }
}
