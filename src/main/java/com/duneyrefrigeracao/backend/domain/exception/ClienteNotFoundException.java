package com.duneyrefrigeracao.backend.domain.exception;

import com.duneyrefrigeracao.backend.domain.model.Cliente;

public class ClienteNotFoundException extends RuntimeException{

    public ClienteNotFoundException(String name) {
        super(String.format("Cliente %s não foi encontrado no banco de dados, verifique se o id informado está correto",name));
    }

    public ClienteNotFoundException(){
        super("Não foi possivel encontrar o cliente atraves do parametro informado");
    }

}
