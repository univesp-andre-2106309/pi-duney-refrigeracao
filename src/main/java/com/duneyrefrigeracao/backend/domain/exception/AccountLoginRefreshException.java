package com.duneyrefrigeracao.backend.domain.exception;

public class AccountLoginRefreshException extends RuntimeException{
    public AccountLoginRefreshException() {
        super("Não foi possivel manter o status de sessão");
    }
}
