package com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAdicionarFornecedorResp(
        @JsonProperty String resposta,
        @JsonProperty String horario
) {
}
