package com.duneyrefrigeracao.backend.application.dataobject.response.produto;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchProdutoEstoqueResp(
        @JsonProperty String horario,
        @JsonProperty ProdutoDTO result
) {
}
