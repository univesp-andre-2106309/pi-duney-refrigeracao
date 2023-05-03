package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.TecnicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.tecnico.PostCreateTecnicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.tecnico.PutUpdateTecnicoReq;
import com.duneyrefrigeracao.backend.domain.model.Tecnico;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TecnicoMapper {

    TecnicoMapper INSTANCE = Mappers.getMapper(TecnicoMapper.class);

    Tecnico AddTecnicoParaTecnico(PostCreateTecnicoReq req);
    Tecnico UpdateTecnicoParaTecnico(PutUpdateTecnicoReq req);
    TecnicoDTO TecnicoParaTecnicoDTO(Tecnico req);
    List<TecnicoDTO>  ListaTecnicoParaListaTecnicoDTO(List<Tecnico> req);
}
