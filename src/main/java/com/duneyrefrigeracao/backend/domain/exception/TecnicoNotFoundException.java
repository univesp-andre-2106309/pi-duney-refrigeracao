package com.duneyrefrigeracao.backend.domain.exception;

public class TecnicoNotFoundException extends RuntimeException{

    public TecnicoNotFoundException(String nome){
        super(String.format("Tecnico %s não foi encontrado no banco de dados, verifique se o id informado está correto", nome));
    }

    public TecnicoNotFoundException(){
        super("Não foi possivel encontrar o Tecnico no atraves do parametor informado");
    }

}
