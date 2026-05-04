package br.com.maxsueleinstein.stratega.application.dto;

import java.util.List;
import java.util.UUID;

public record CreateGroupRequest(
    String name,
    UUID ownerId,
    List<String> memberNames // Membros iniciais (virtuais ou não, aqui simplificaremos para nomes)
) {}
