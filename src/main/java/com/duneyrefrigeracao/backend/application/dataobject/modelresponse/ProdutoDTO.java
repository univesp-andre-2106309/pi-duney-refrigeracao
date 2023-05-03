package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ProdutoDTO(
        @JsonProperty Long id,
        @JsonProperty String nome,
        @JsonProperty String descricao,
        @JsonProperty BigDecimal preco,
        @JsonProperty int estoque
) {
}
