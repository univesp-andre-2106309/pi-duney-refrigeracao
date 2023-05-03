package com.duneyrefrigeracao.backend.application.dataobject.request.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchProdutoEstoqueReq(
        @JsonProperty(required = true) int estoque
) {
}
