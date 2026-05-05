package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.SpendingTrendResponse;
import java.util.UUID;

public interface GetSpendingTrendUseCase {
    SpendingTrendResponse execute(UUID userId, int month, int year);
}
