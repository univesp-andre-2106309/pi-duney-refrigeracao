package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.exception.FornecedorNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.util.Collection;

public interface IFornecedorService {

    Tuple<Long, Collection<Fornecedor>> getFornecedoresByParams(String nome, String documento, int index);
    Fornecedor saveFornecedor(Fornecedor fornecedor);
    void updateFornecedor(Fornecedor fornecedor, Long id) throws FornecedorNotFoundException;
    Fornecedor getFornecedorById(Long id) throws FornecedorNotFoundException;
}
