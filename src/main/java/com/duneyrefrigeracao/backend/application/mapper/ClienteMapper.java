package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ClienteDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.account.PutUpdateClienteReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PostAdicionarClienteReq;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper( ClienteMapper.class );

    Cliente AddClienteParaCliente(PostAdicionarClienteReq req);

    Cliente UpdateClienteParaCliente(PutUpdateClienteReq req);

    ClienteDTO ClienteParaClienteDTO(Cliente req);


    List<ClienteDTO> ListaCLienteParaListaClienteDTO(List<Cliente> req);
}
