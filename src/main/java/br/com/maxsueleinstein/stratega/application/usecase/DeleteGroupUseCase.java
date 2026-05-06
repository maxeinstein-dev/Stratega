package br.com.maxsueleinstein.stratega.application.usecase;

import java.util.UUID;

public interface DeleteGroupUseCase {
    void execute(UUID groupId, UUID userId);
}
