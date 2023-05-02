package com.duneyrefrigeracao.backend.domain.exception;

public class FornecedorNotFoundException extends RuntimeException{
    public FornecedorNotFoundException(String nome) {
        super(String.format("Fornecedor %s nao foi encontrado no banco de ados, verifique se o id informado está correto", nome));
    }

    public FornecedorNotFoundException(){
        super("Não foi possivel encontrar o cliente atraves do parametro informado");
    }
}
