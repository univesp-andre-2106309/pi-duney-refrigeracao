package com.duneyrefrigeracao.backend.application.dataobject.response.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAdicionarProdutoResp(
        @JsonProperty String resposta,
        @JsonProperty String horario
) {
}
