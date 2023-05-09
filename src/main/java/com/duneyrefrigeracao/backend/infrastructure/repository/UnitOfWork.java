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

    private final ProdutoServicoRepository produtoServicoRepository;
    private final TecnicoServicoRepository tecnicoServicoRepository;
    private final FornecedorServicoRepository fornecedorServicoRepository;

    public UnitOfWork(AccountRepository accountRepository,
                      ClienteRepository clienteRepository,
                      FornecedorRepository fornecedorRepository,
                      ProdutoRepository produtoRepository,
                      ServicoRepository servicoRepository,
                      TecnicoRepository tecnicoRepository, ProdutoServicoRepository produtoServicoRepository, TecnicoServicoRepository tecnicoServicoRepository, FornecedorServicoRepository fornecedorServicoRepository) {
        this.accountRepository = accountRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.servicoRepository = servicoRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.produtoServicoRepository = produtoServicoRepository;
        this.tecnicoServicoRepository = tecnicoServicoRepository;
        this.fornecedorServicoRepository = fornecedorServicoRepository;
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

    @Override
    public ProdutoServicoRepository getProdutoServicoRepository() {
        return this.produtoServicoRepository;
    }

    @Override
    public TecnicoServicoRepository getTecnicoServicoRepository() {
        return this.tecnicoServicoRepository;
    }

    @Override
    public FornecedorServicoRepository getFornecedorServicoRepository() {
        return this.fornecedorServicoRepository;
    }
}
