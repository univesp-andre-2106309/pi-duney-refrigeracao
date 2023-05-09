package com.duneyrefrigeracao.backend.domain.exception;

public class ServicoNotFoundException extends RuntimeException{

    public ServicoNotFoundException() {
        super("Não foi possivel encontrar um serviço atráves dos parametros especificados");
    }
}
