package com.duneyrefrigeracao.backend.domain.exception;

public class ClienteNotFoundException extends RuntimeException{

    public ClienteNotFoundException(String name) {
        super(String.format("Cliente %s não foi encontrado no banco de dados, verifique se o id informado está correto",name));
    }
}
