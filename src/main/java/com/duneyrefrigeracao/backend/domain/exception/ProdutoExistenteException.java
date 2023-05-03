package com.duneyrefrigeracao.backend.domain.exception;

public class ProdutoExistenteException extends RuntimeException{

    public ProdutoExistenteException(String nome) {
        super(String.format("Produto de noem %s já existe no banco de dados!",nome));
    }
}
