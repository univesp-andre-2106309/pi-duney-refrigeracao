package com.duneyrefrigeracao.backend.domain.exception;

public class AccountValidationException extends  RuntimeException{

    public AccountValidationException(String field, String message) {
        super(String.format("Ocorreu um erro de validação no campo %s: %s",field, message));
    }
}
