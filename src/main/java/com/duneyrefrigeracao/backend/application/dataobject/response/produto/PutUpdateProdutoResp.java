package com.duneyrefrigeracao.backend.application.dataobject.response.produto;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PutUpdateProdutoResp(
        @JsonProperty String resposta,
        @JsonProperty ProdutoDTO result
        ) {
}
