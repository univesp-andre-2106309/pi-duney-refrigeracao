package com.duneyrefrigeracao.backend.domain.exception;

public class AccountNotAuthorizedException extends  RuntimeException{

    public AccountNotAuthorizedException(String accountName) {
        super(String.format("Operação não permitida para a conta %s!", accountName));
    }
}
