package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.exception.FornecedorExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.FornecedorNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class FornecedorService implements IFornecedorService {

    private final IUnitOfWork _unitOfWork;
    private final IMatcherService _matcherService;
    private static final int _pageSize = 5;

    private final ILogging _logging;

    public FornecedorService(IUnitOfWork unitOfWork, IMatcherService matcherService) {
        this._unitOfWork = unitOfWork;
        this._matcherService = matcherService;
        this._logging = new Logging(FornecedorService.class);
    }

    @Override
    public Tuple<Long, Collection<Fornecedor>> getFornecedoresByParams(String nome, String documento, int index) {
        this._logging.LogMessage(LogLevel.INFO, String.format("Busca será realizada com paginação de %d", _pageSize));
        Pageable pageable = PageRequest.of(index,_pageSize);
        Collection<Fornecedor> collection;

        Long count = this._unitOfWork.getFornecedorRepository().countByNomeLikeIgnoreCaseAndCnpjLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento));

        if (count == 0 && (long) index * _pageSize > count) {
            this._logging.LogMessage(LogLevel.INFO, "Busca vazia");
            return new Tuple<>(count, new ArrayList<>());
        }

        this._logging.LogMessage(LogLevel.INFO, String.format("Existem no total %d de resultados", count));

        Page<Fornecedor> result =
                this._unitOfWork.getFornecedorRepository().findByNomeLikeIgnoreCaseAndCnpjLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento), pageable);


        collection = result.getContent();
        if (!collection.isEmpty()) {
            this._logging.LogMessage(LogLevel.INFO, "Informando busca....");
            collection.forEach(item ->
                    this._logging.LogMessage(LogLevel.INFO, String.format("Nome: %s, Email %s", item.getNome(), item.getEmail()))
            );
        }

        return new Tuple<>(count, collection);
    }

    @Override
    public Fornecedor saveFornecedor(Fornecedor fornecedor) {

        if(!_matcherService.validateEmailPattern(fornecedor.getEmail())) {
            throw new EmailPatternException();
        }

        if(_unitOfWork.getFornecedorRepository().countByEmailIgnoreCase(fornecedor.getEmail()) > 0) {
            throw new FornecedorExistenteException();
        }

        fornecedor.setDtCadastro(new Date());
        fornecedor.setActive(true);

        this._logging.LogMessage(LogLevel.INFO, String.format("Armazenado o fornecedor %s ao banco de dados.....", fornecedor.getNome()));
        this._unitOfWork.getFornecedorRepository().save(fornecedor);
        this._logging.LogMessage(LogLevel.INFO, "Fornecedor salvo com sucesso!");

        return fornecedor;
    }

    @Override
    public void updateFornecedor(Fornecedor fornecedor, Long id) throws FornecedorNotFoundException {
        Fornecedor ogFornecedor;
        this._logging.LogMessage(LogLevel.INFO,String.format("Atualizando dados do fornecedor %s", fornecedor.getNome()));
        try{
            ogFornecedor = this._unitOfWork.getFornecedorRepository().getReferenceById(id);
        } catch(Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar fornecedor de id - %d, erro original -> %s",id, er.getMessage()));
           throw new FornecedorNotFoundException();
        }

        fornecedor.setId(ogFornecedor.getId());
        fornecedor.setActive(true);
        fornecedor.setDtCadastro(ogFornecedor.getDtCadastro());

        this._unitOfWork.getFornecedorRepository().save(fornecedor);

        this._logging.LogMessage(LogLevel.INFO, "Dados atualizados com sucesso!");
    }

    @Override
    public Fornecedor getFornecedorById(Long id) {
        try{
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados do fornecedor de Id - %s",id));
            Fornecedor fornecedor = this._unitOfWork.getFornecedorRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encotnrado dados do fornecedor - %s", fornecedor.toString()));

            return fornecedor;
        }catch (Exception er){
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar fornecedor por id - %d, erro original -> %s", id, er.getMessage()));
            throw new FornecedorNotFoundException();
        }
    }
}
