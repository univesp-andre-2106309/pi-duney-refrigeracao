package com.duneyrefrigeracao.backend.application.dataobject.response.servico;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostCreateServicoResp(
        @JsonProperty String resposta,
        @JsonProperty String horario
) {
}
