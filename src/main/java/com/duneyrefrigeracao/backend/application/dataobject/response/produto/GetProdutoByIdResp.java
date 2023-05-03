package com.duneyrefrigeracao.backend.application.dataobject.response.produto;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetProdutoByIdResp(
        @JsonProperty String title,
        @JsonProperty ProdutoDTO result
        ) {
}
