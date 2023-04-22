package com.duneyrefrigeracao.backend.infrastructure.repository;

import org.springframework.stereotype.Component;

@Component
public class UnitOfWork implements IUnitOfWork{

    public AccountRepository getAccountRepository() {
        return this.accountRepository;
    }

    @Override
    public ClienteRepository getClienteRepository() {
        return this.clienteRepository;
    }

    private final AccountRepository accountRepository;

    private final ClienteRepository clienteRepository;

    public UnitOfWork(AccountRepository accountRepository, ClienteRepository clienteRepository) {
        this.accountRepository = accountRepository;
        this.clienteRepository = clienteRepository;
    }
}
