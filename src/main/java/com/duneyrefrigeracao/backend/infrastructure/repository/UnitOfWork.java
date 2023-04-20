package com.duneyrefrigeracao.backend.infrastructure.repository;

import org.springframework.stereotype.Component;

@Component
public class UnitOfWork implements IUnitOfWork{

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    private final AccountRepository accountRepository;

    public UnitOfWork(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
