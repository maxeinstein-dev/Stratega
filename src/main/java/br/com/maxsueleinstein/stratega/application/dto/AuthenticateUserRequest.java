package br.com.maxsueleinstein.stratega.application.dto;

public record AuthenticateUserRequest(
        String email,
        String password
) {}
