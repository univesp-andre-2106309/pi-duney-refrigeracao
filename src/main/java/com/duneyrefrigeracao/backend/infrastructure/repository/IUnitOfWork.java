package com.duneyrefrigeracao.backend.infrastructure.repository;

public interface IUnitOfWork {
    public AccountRepository getAccountRepository();
    public ClienteRepository getClienteRepository();

    public FornecedorRepository getFornecedorRepository();
    public ServicoRepository getServicoRepository();
    public TecnicoRepository getTecnicoRepository();
    public ProdutoRepository getProdutoRepository();
}
