package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.exception.ProdutoExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.ProdutoNotFoundException;
import com.duneyrefrigeracao.backend.domain.exception.ValorMenorQueZeroException;
import com.duneyrefrigeracao.backend.domain.model.Produto;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class ProdutoService implements IProdutoService {

    private final ILogging _logging;
    private final IUnitOfWork _unitOfWork;
    private final IMatcherService _matcherService;
    private static final int _pageSize = 5;

    public ProdutoService(IUnitOfWork unitOfWork, IMatcherService matcherService) {
        _unitOfWork = unitOfWork;
        _matcherService = matcherService;
        this._logging = new Logging(ProdutoService.class);
    }

    @Override
    public Tuple<Long, Collection<Produto>> getProdutoByParams(String nome, BigDecimal precoMin, BigDecimal precoMax, OrderByEnum order, int index) {
        this._logging.LogMessage(LogLevel.INFO, String.format("Busca será realizada com paginação de %d", _pageSize));
        Pageable pageable = PageRequest.of(index, _pageSize);
        Collection<Produto> collection;
        Page<Produto> result;

        if (precoMin == null) {
            precoMin = new BigDecimal(0L);
        }
        if (precoMax == null) {
            precoMax = new BigDecimal(Long.MAX_VALUE);
        }

        Long count =
                this._unitOfWork.getProdutoRepository().findAndCountByPriceBetween(precoMin, precoMax, nome);

        if (count == 0 && (long) index * _pageSize > count) {
            this._logging.LogMessage(LogLevel.INFO, "Busca vazia");
            return new Tuple<>(count, new ArrayList<>());
        }

        this._logging.LogMessage(LogLevel.INFO, String.format("Existem no total %d de resultados", count));

        if(order.equals(OrderByEnum.ASC)) {
            result =
                    this._unitOfWork.getProdutoRepository().findByPriceBetweenAscend(precoMin, precoMax, nome, pageable);
        } else {
            result =
                    this._unitOfWork.getProdutoRepository().findByPriceBetweenDescend(precoMin, precoMax, nome, pageable);
        }

        collection = result.getContent();
        if (!collection.isEmpty()) {
            this._logging.LogMessage(LogLevel.INFO, "Informando busca....");
            collection.forEach(item ->
                    this._logging.LogMessage(LogLevel.INFO, String.format("Nome: %s, Preco %.2f", item.getNome(), item.getPreco()))
            );
        }

        return new Tuple<>(count, collection);
    }

    @Override
    public Produto saveProduto(Produto produto) throws ValorMenorQueZeroException, ProdutoExistenteException {
        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorMenorQueZeroException();
        }

        if (this._unitOfWork.getProdutoRepository().countByNomeLikeIgnoreCase(String.format("%%%s%%", produto.getNome())) > 0) {
            throw new ProdutoExistenteException(produto.getNome());
        }

        produto.setEstoque(0);
        produto.setActive(true);
        produto.setDtCadastro(new Date());

        this._logging.LogMessage(LogLevel.INFO, String.format("Armazenando o produto %s ao banco de dados.....", produto.getNome()));
        this._unitOfWork.getProdutoRepository().save(produto);
        this._logging.LogMessage(LogLevel.INFO, "produto salvo com sucesos");

        return produto;
    }

    @Override
    public Produto updateProduto(Produto produto, Long id) {
        Produto ogProduto;
        this._logging.LogMessage(LogLevel.INFO, String.format("Atualizando dados de produto %s", produto.getNome()));

        try {
            ogProduto = this._unitOfWork.getProdutoRepository().getReferenceById(id);
        } catch (Exception er) {
            return null;
        }

        produto.setId(ogProduto.getId());
        produto.setActive(true);
        produto.setPreco(ogProduto.getPreco());
        produto.setDtCadastro(ogProduto.getDtCadastro());

        this._unitOfWork.getProdutoRepository().save(produto);

        this._logging.LogMessage(LogLevel.INFO, "Dados atualizados com sucesso!");

        return produto;
    }

    @Override
    public Produto getProdutoById(Long id) {
        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados de produto de id - %s", id));
            Produto produto = this._unitOfWork.getProdutoRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado dados do produto - %s", produto.toString()));

            return produto;
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar produto por id - %d, erro original -> %s", id, er.getMessage()));
            throw new ProdutoNotFoundException();
        }
    }

    @Override
    public Produto updateProdutoPreco(BigDecimal preco, Long id) throws ValorMenorQueZeroException {
        if (preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorMenorQueZeroException();
        }

        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados de produto de id - %s", id));
            Produto produto = this._unitOfWork.getProdutoRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado dados do produto - %s", produto.toString()));


            produto.setPreco(preco);

            this._logging.LogMessage(LogLevel.INFO, String.format("Atualizando preço do produto %s no banco de dados.....", produto.getNome()));
            this._unitOfWork.getProdutoRepository().save(produto);
            this._logging.LogMessage(LogLevel.INFO, "Produto atualizado com sucesos");

            return produto;

        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar produto por id - %d, erro original -> %s", id, er.getMessage()));
            throw new ProdutoNotFoundException();
        }
    }

    @Override
    public Produto updateProdutoEstoque(int estoque, Long id) throws ValorMenorQueZeroException {

        if (estoque < 0) {
            throw new ValorMenorQueZeroException();
        }

        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados de produto de id - %s", id));
            Produto produto = this._unitOfWork.getProdutoRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado dados do produto - %s", produto.toString()));


            produto.setEstoque(estoque);

            this._logging.LogMessage(LogLevel.INFO, String.format("Atualizando estoque de produto %s no banco de dados.....", produto.getNome()));
            this._unitOfWork.getProdutoRepository().save(produto);
            this._logging.LogMessage(LogLevel.INFO, "Produto atualizado com sucesos");

            return produto;

        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar produto por id - %d, erro original -> %s", id, er.getMessage()));
            throw new ProdutoNotFoundException();
        }
    }
}
