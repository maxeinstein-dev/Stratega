package br.com.maxsueleinstein.stratega.application.usecase;

import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface ImportTransactionsUseCase {
    int execute(UUID userId, UUID walletId, MultipartFile file);
}
