package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.exception.ClienteNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.util.Collection;

public interface IClienteService {

    Tuple<Long, Collection<Cliente>> getClientesByParams(String nome, String documento, int index, int numPages);
    Cliente saveCliente(Cliente cliente);
    void updateCliente(Cliente upCliente, Long id) throws ClienteNotFoundException;

    Cliente getClienteById(Long id);

    Cliente removeClienteById(Long id);
}
