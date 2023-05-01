package com.duneyrefrigeracao.backend.domain.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Senha informada incorreta!");
    }
}
