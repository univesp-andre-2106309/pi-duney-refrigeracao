package com.duneyrefrigeracao.backend.domain.exception;

public class PasswordNotEqualException extends RuntimeException{

    public PasswordNotEqualException() {
        super("Nova senha informada não é igual a senha fornecida!");
    }
}
