package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.exception.TecnicoExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.TecnicoNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Tecnico;
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
public class TecnicoServico implements ITecnicoService{

    private final IUnitOfWork _unitOfWork;
    private final IMatcherService _matcherService;
    private static final int _pageSize = 10;
    private final ILogging _logging;

    public TecnicoServico(IUnitOfWork unitOfWork, IMatcherService matcherService) {
        this._unitOfWork = unitOfWork;
        this._matcherService = matcherService;
        this._logging = new Logging(TecnicoServico.class);
    }


    @Override
    public Tuple<Long, Collection<Tecnico>> getTecnicosByParams(String nome, String documento, int index, int numPages) {
        if(numPages == 0) {
            numPages = Integer.MAX_VALUE;
        }
        this._logging.LogMessage(LogLevel.INFO, String.format("Busca será realizada com paginação de %d",numPages));
        Pageable pageable = PageRequest.of(index,numPages);
        Collection<Tecnico> collection;


        Long count =
                this._unitOfWork.getTecnicoRepository().countByParams(String.format("%%%s%%", nome), String.format("%%%s%%", documento));

        if (count == 0 && (long) index * numPages > count) {
            this._logging.LogMessage(LogLevel.INFO, "Busca vazia");
            return new Tuple<>(count, new ArrayList<>());
        }

        this._logging.LogMessage(LogLevel.INFO, String.format("Existem no total %d de resultados", count));

        Page<Tecnico> result =
                this._unitOfWork.getTecnicoRepository().findByParams(String.format("%%%s%%", nome), String.format("%%%s%%", documento), pageable);


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
    public Tecnico saveTecnico(Tecnico tecnico) {

        if(!_matcherService.validateEmailPattern(tecnico.getEmail())) {
            throw new EmailPatternException();
        }

        if(_unitOfWork.getTecnicoRepository().countByEmailIgnoreCase(tecnico.getEmail()) > 0) {
            throw new TecnicoExistenteException();
        }

        tecnico.setDtCadastro(new Date());
        tecnico.setActive(true);

        this._logging.LogMessage(LogLevel.INFO, String.format("Armazenando o tencico %s ao banco de dados.....", tecnico.getNome()));
        this._unitOfWork.getTecnicoRepository().save(tecnico);
        this._logging.LogMessage(LogLevel.INFO, "Tecnico salvo com sucesos");

        return tecnico;
    }

    @Override
    public Tecnico updateTecnico(Tecnico tecnico, Long id) throws TecnicoNotFoundException {
        Tecnico ogTecnico;
        this._logging.LogMessage(LogLevel.INFO, String.format("Atualizando dados de tecnico %s", tecnico.getNome()));
        try{
            ogTecnico = this._unitOfWork.getTecnicoRepository().getReferenceById(id);
        }catch(Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar tecnico de id - %d, erro original -> %s",id, er.getMessage()));
            throw new TecnicoNotFoundException();
        }

        tecnico.setId(ogTecnico.getId());
        tecnico.setActive(true);
        tecnico.setDtCadastro(ogTecnico.getDtCadastro());

        this._unitOfWork.getTecnicoRepository().save(tecnico);

        this._logging.LogMessage(LogLevel.INFO, "Dados atualizados com sucesso!");

        return tecnico;
    }

    @Override
    public Tecnico getTecnicoById(Long id) throws TecnicoNotFoundException {
        try{
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados de tecnico de id - %s", id));
            Tecnico tecnico = this._unitOfWork.getTecnicoRepository().getReferenceById(id);
            this._logging.LogMessage(LogLevel.INFO, String.format("Foi encontrado dados do tecnico - %s", tecnico.toString()));

            return tecnico;
        }catch(Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar tecnico por id - %d, erro original -> %s", id, er.getMessage()));
            throw new TecnicoNotFoundException();
        }
    }

    @Override
    public Tecnico deleteTecnicoById(Long id) {
        try{
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando dados de tecnico de id - %s", id));
            Tecnico tecnico = this._unitOfWork.getTecnicoRepository().getReferenceById(id);
            tecnico.setActive(false);
            this._unitOfWork.getTecnicoRepository().save(tecnico);
            this._logging.LogMessage(LogLevel.INFO, "Técnico removido com sucesso!");
            return tecnico;
        }catch(Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar tecnico por id - %d, erro original -> %s", id, er.getMessage()));
            throw new TecnicoNotFoundException();
        }
    }
}
