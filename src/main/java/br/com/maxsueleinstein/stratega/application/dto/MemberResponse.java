package br.com.maxsueleinstein.stratega.application.dto;

import java.util.UUID;

public record MemberResponse(
    UUID id,
    String name,
    UUID userId
) {}
