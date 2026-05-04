package br.com.maxsueleinstein.stratega.application.usecase;

import br.com.maxsueleinstein.stratega.application.dto.TransferFundsRequest;

public interface TransferFundsUseCase {
    void execute(TransferFundsRequest request);
}
