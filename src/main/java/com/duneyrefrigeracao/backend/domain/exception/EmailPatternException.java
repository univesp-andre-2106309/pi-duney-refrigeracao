package com.duneyrefrigeracao.backend.domain.exception;

public class EmailPatternException extends RuntimeException{

    public EmailPatternException() {
        super("O campo de email está formatado incorretamente");
    }
}
