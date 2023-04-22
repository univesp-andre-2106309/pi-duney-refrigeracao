package com.duneyrefrigeracao.backend.infrastructure.repository;

public interface IUnitOfWork {
    public AccountRepository getAccountRepository();

    public ClienteRepository getClienteRepository();
}
