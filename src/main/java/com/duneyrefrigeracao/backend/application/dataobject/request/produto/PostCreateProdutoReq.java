package com.duneyrefrigeracao.backend.application.dataobject.request.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PostCreateProdutoReq(
        @JsonProperty String nome,
        @JsonProperty String descricao,
        @JsonProperty BigDecimal preco
) {
}
