package com.duneyrefrigeracao.backend.domain.exception;

public class FornecedorExistenteException extends RuntimeException {

    public FornecedorExistenteException(){
        super("Fornecedor já existente no banco");
    }
}
