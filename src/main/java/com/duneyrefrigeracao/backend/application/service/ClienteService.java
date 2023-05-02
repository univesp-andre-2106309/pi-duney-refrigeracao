package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.AccountValidationException;
import com.duneyrefrigeracao.backend.domain.exception.ClienteExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.ClienteNotFoundException;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
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

@Service
public class ClienteService implements IClienteService {

    private final IUnitOfWork _unitOfWork;
    private final IMatcherService _matcherService;
    private static final int _pageSize = 5;

    private final ILogging _logging;

    public ClienteService(IUnitOfWork _unitOfWork, IMatcherService matcherService) {
        this._matcherService = matcherService;
        this._unitOfWork = _unitOfWork;
        this._logging = new Logging(ClienteService.class);
    }

    public Tuple<Long, Collection<Cliente>> getClientesByParams(String nome, String documento, int index) {

        this._logging.LogMessage(LogLevel.INFO, String.format("Busca será realizada com paginação de %d", _pageSize));

        Pageable pageable = PageRequest.of(index, _pageSize);
        Collection<Cliente> collection;

        Long count = this._unitOfWork.getClienteRepository().countByNomeLikeIgnoreCaseAndDocumentoLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento));

        if (count == 0 && (long) index * _pageSize > count) {
            this._logging.LogMessage(LogLevel.INFO, "Busca vazia");
            return new Tuple<>(count, new ArrayList<>());
        }

        this._logging.LogMessage(LogLevel.INFO, String.format("Existem no total %d de resultados", count));

        Page<Cliente> result =
                this._unitOfWork.getClienteRepository().findByNomeLikeIgnoreCaseAndDocumentoLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento), pageable);

        collection = result.getContent();

        if (!collection.isEmpty()) {
            this._logging.LogMessage(LogLevel.INFO, "Informando busca....");
            collection.forEach(item ->
                    this._logging.LogMessage(LogLevel.INFO, String.format("Nome: %s, Email %s", item.getNome(), item.getEmail()))
            );
        }

        return new Tuple<>(count, collection);
    }

    public Cliente saveCliente(Cliente cliente) {

        if (!_matcherService.validateEmailPattern(cliente.getEmail())) {
            throw new EmailPatternException();
        }

        if (this._unitOfWork.getClienteRepository().countByEmail(cliente.getEmail()) > 0) {
            throw new ClienteExistenteException();
        }
        this._logging.LogMessage(LogLevel.INFO, String.format("Armazenado o cliente %s ao banco de dados.....", cliente.getNome()));
        this._unitOfWork.getClienteRepository().save(cliente);
        this._logging.LogMessage(LogLevel.INFO, "Cliente salvo com sucesso!");

        return cliente;
    }

    @Override
    public void updateCliente(Cliente upCliente, Long id) throws ClienteNotFoundException {
        Cliente ogCliente;
        this._logging.LogMessage(LogLevel.INFO,String.format("Atualizando dados do cliente %s", upCliente.getNome()));
        try{
            ogCliente = this._unitOfWork.getClienteRepository().getReferenceById(id);
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao consultar cliente, erro será jogado como ClienteNotFoundException, erro original -> %s", er.getMessage()));
            throw new ClienteNotFoundException(upCliente.getNome());
        }

        upCliente.setId(ogCliente.getId());
        upCliente.setEnabled(ogCliente.isEnabled());

        this._unitOfWork.getClienteRepository().save(upCliente);

        this._logging.LogMessage(LogLevel.INFO, "Dados atualizados com sucesso!");
    }


}
