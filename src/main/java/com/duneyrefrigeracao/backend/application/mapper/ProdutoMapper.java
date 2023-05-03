package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PostCreateProdutoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PutUpdateProdutoReq;
import com.duneyrefrigeracao.backend.domain.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    Produto AddProdutoParaProduto(PostCreateProdutoReq req);

    Produto UpdateProdutoParaProduto(PutUpdateProdutoReq req);
    ProdutoDTO ProdutoParaProdutoDTO(Produto req);
    List<ProdutoDTO> ListaProdutoParaListaProdutoDTO(List<Produto> req);
}
