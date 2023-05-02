package com.duneyrefrigeracao.backend.domain.exception;

public class AccountNotFoundException extends RuntimeException{
  public AccountNotFoundException(String accountName) {
      super(String.format("A conta de nome %s não foi encontrada!", accountName));
  }

  public AccountNotFoundException(){
      super("Não foi possivel encontrar a conta pelos parametros de busca");
  }

}
