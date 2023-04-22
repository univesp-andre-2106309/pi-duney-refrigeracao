package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.util.Collection;

public interface IClienteService {

    Tuple<Long, Collection<Cliente>> getClientesByParams(String nome, String documento, int index);

    Cliente saveCliente(Cliente cliente);
}
