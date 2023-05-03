package com.duneyrefrigeracao.backend.domain.exception;

public class ValorMenorQueZeroException extends RuntimeException{
    public ValorMenorQueZeroException() {
        super("Valor informado é menor que zero em uma operação não permitida");
    }
}
