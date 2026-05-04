package br.com.maxsueleinstein.stratega.application.usecase;
 
import br.com.maxsueleinstein.stratega.application.dto.GroupResponse;
import java.util.List;
import java.util.UUID;
 
public interface FindGroupsByUserIdUseCase {
    List<GroupResponse> execute(UUID userId);
}
