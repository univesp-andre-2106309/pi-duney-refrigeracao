package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.FornecedorDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.fornecedor.PostCreateFornecedorReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.fornecedor.PutUpdateFornecedorReq;
import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FornecedorMapper {

    FornecedorMapper INSTANCE = Mappers.getMapper(FornecedorMapper.class);

    Fornecedor AddFornecedorParaFornecedor(PostCreateFornecedorReq req);
    Fornecedor UpdateFornecedorParaFornecedor(PutUpdateFornecedorReq req);
    FornecedorDTO FornecedorParaFornecedorDTO(Fornecedor req);
    List<FornecedorDTO> ListaFornecedorParaListaFornecedorDTO(List<Fornecedor> req);

}
