package com.duneyrefrigeracao.backend.domain.exception;

public class TecnicoExistenteException extends RuntimeException{

    public TecnicoExistenteException(){
        super("Tecnico já existente no banco de dados");
    }

}
