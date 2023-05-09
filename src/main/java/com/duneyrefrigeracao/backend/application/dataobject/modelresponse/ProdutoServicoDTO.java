package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;

public record ProdutoServicoDTO(
        @JsonProperty Long id,
        @JsonProperty ProdutoDTO produto,
        @JsonProperty Date dtCriacao,
        @JsonProperty BigDecimal precoProduto
        ) {
}
