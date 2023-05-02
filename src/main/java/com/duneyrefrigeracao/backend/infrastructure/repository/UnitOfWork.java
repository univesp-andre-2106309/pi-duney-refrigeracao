package com.duneyrefrigeracao.backend.infrastructure.repository;

import org.springframework.stereotype.Component;

@Component
public class UnitOfWork implements IUnitOfWork{

    private final AccountRepository accountRepository;

    private final ClienteRepository clienteRepository;

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final TecnicoRepository tecnicoRepository;

    public UnitOfWork(AccountRepository accountRepository,
                      ClienteRepository clienteRepository,
                      FornecedorRepository fornecedorRepository,
                      ProdutoRepository produtoRepository,
                      ServicoRepository servicoRepository,
                      TecnicoRepository tecnicoRepository) {
        this.accountRepository = accountRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.servicoRepository = servicoRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public AccountRepository getAccountRepository() {
        return this.accountRepository;
    }

    @Override
    public ClienteRepository getClienteRepository() {
        return this.clienteRepository;
    }

    @Override
    public FornecedorRepository getFornecedorRepository() {
        return this.fornecedorRepository;
    }

    @Override
    public ServicoRepository getServicoRepository() {
        return this.servicoRepository;
    }

    @Override
    public TecnicoRepository getTecnicoRepository() {
        return this.tecnicoRepository;
    }

    @Override
    public ProdutoRepository getProdutoRepository() {
        return this.produtoRepository;
    }
}
