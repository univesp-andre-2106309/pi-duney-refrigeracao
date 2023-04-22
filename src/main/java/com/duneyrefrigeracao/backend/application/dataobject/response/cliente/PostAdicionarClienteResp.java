package com.duneyrefrigeracao.backend.application.dataobject.response.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAdicionarClienteResp(
        @JsonProperty String resposta,
        @JsonProperty String horario
) {
}
