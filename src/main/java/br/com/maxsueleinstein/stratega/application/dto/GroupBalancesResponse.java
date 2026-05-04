package br.com.maxsueleinstein.stratega.application.dto;

import br.com.maxsueleinstein.stratega.domain.model.SuggestedTransfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GroupBalancesResponse(
    UUID groupId,
    String groupName,
    Map<UUID, BigDecimal> memberBalances,
    List<SuggestedTransfer> suggestedTransfers
) {}
