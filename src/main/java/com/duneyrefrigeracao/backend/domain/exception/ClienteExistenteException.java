package com.duneyrefrigeracao.backend.domain.exception;

public class ClienteExistenteException extends RuntimeException{

    public ClienteExistenteException() {
        super("Cliente já existente no banco");
    }
}
