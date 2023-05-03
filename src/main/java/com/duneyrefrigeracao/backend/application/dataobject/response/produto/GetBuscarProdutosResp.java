package com.duneyrefrigeracao.backend.application.dataobject.response.produto;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record GetBuscarProdutosResp(
        @JsonProperty Long maxResultSize,
        @JsonProperty Collection<ProdutoDTO> result
        ) {
}
