package com.duneyrefrigeracao.backend.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TecnicoMapper {

    TecnicoMapper INSTANCE = Mappers.getMapper(TecnicoMapper.class);
}
