package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PostAdicionarClienteReq;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper( ClienteMapper.class );

    Cliente AddClienteParaCliente(PostAdicionarClienteReq req);
}
