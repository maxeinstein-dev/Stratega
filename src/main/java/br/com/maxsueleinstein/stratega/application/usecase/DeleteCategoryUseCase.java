package br.com.maxsueleinstein.stratega.application.usecase;

import java.util.UUID;

public interface DeleteCategoryUseCase {
    void execute(UUID categoryId, UUID userId);
}
