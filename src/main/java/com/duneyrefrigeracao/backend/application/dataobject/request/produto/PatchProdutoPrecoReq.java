package com.duneyrefrigeracao.backend.application.dataobject.request.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PatchProdutoPrecoReq(
        @JsonProperty(required = true) BigDecimal preco
) {
}
