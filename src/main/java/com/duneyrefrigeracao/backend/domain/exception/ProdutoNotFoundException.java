package com.duneyrefrigeracao.backend.domain.exception;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(String nome){
        super(String.format("Produto %s nao foi encontrado no banco de ados, verifique se o id informado está correto", nome));
    }

    public ProdutoNotFoundException(){
        super("Não foi possivel encontrar o fornecedor atraves do parametro informado");
    }

}
