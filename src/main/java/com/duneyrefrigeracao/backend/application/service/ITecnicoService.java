package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.domain.exception.TecnicoNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Tecnico;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.util.Collection;

public interface ITecnicoService {
    Tuple<Long, Collection<Tecnico>> getTecnicosByParams(String nome, String documento, int index);
    Tecnico saveTecnico(Tecnico tecnico);
    Tecnico updateTecnico(Tecnico tecnico, Long id) throws TecnicoNotFoundException;
    Tecnico getTecnicoById(Long id) throws TecnicoNotFoundException;
}
