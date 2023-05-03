package com.duneyrefrigeracao.backend.application.dataobject.request.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PutUpdateProdutoReq(
        @JsonProperty String nome,
        @JsonProperty String descricao,
        @JsonProperty BigDecimal preco
) {

}
