package com.duneyrefrigeracao.backend.domain.exception;

public class AccountNotAvailableException extends RuntimeException{

    public AccountNotAvailableException(){
        super("Não foi possivel buscar os dados relacionados a uma conta");
    }

}
