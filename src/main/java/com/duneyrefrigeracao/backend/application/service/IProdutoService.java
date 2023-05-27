package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.exception.ProdutoExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.ValorMenorQueZeroException;
import com.duneyrefrigeracao.backend.domain.model.Produto;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.math.BigDecimal;
import java.util.Collection;

public interface IProdutoService {

    Tuple<Long, Collection<Produto>> getProdutoByParams(String nome, BigDecimal precoMin, BigDecimal precoMax, OrderByEnum order, int index, int numPages);

    Produto saveProduto(Produto produto) throws ValorMenorQueZeroException, ProdutoExistenteException;
    Produto updateProduto(Produto produto, Long id);
    Produto getProdutoById(Long id);

    Produto updateProdutoPreco(BigDecimal preco, Long id) throws ValorMenorQueZeroException;

    Produto updateProdutoEstoque(int estoque, Long id) throws ValorMenorQueZeroException;

    Produto removeProdutoById(Long id);
}
