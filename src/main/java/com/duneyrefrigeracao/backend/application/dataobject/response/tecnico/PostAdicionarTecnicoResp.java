package com.duneyrefrigeracao.backend.application.dataobject.response.tecnico;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostAdicionarTecnicoResp(
        @JsonProperty String resposta,
        @JsonProperty String horario
) {
}
