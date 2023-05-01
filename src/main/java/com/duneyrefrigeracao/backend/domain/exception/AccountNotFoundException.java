package com.duneyrefrigeracao.backend.domain.exception;

public class AccountNotFoundException extends RuntimeException{
  public AccountNotFoundException(String accountName) {
      super(String.format("A conta de nome %s não foi encontrada!", accountName));
  }

}
