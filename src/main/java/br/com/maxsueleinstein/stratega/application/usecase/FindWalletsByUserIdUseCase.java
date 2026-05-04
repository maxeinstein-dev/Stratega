package br.com.maxsueleinstein.stratega.application.usecase;
 
import br.com.maxsueleinstein.stratega.application.dto.WalletResponse;
import java.util.List;
import java.util.UUID;
 
public interface FindWalletsByUserIdUseCase {
    List<WalletResponse> execute(UUID userId);
}
