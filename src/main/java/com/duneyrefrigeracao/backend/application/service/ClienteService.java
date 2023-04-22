package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.exception.ClienteExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
public class ClienteService implements IClienteService{

    private final IUnitOfWork _unitOfWork;
    private final IMatcherService _matcherService;
    private static final int _pageSize = 10;



    public ClienteService(IUnitOfWork _unitOfWork, IMatcherService matcherService) {
        this._matcherService = matcherService;
        this._unitOfWork = _unitOfWork;
    }

    public Tuple<Long, Collection<Cliente>> getClientesByParams(String nome, String documento, int index) {

        Pageable pageable = PageRequest.of(index, _pageSize);
        Collection<Cliente> collection;

        Long count = this._unitOfWork.getClienteRepository().countByNomeLikeIgnoreCaseAndDocumentoLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento));


        Page<Cliente> result =
                this._unitOfWork.getClienteRepository().findByNomeLikeIgnoreCaseAndDocumentoLikeIgnoreCase(String.format("%%%s%%", nome), String.format("%%%s%%", documento), pageable);

        collection = result.getContent();

        return new Tuple<>(count,collection);
    }

    public Cliente saveCliente(Cliente cliente){

        if(!_matcherService.validateEmailPattern(cliente.getEmail())){
            throw new EmailPatternException();
        }

        if(this._unitOfWork.getClienteRepository().countByEmail(cliente.getEmail()) > 0) {
            throw new ClienteExistenteException();
        }

        this._unitOfWork.getClienteRepository().save(cliente);

        return cliente;
    }
}
