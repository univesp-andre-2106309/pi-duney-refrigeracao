package com.duneyrefrigeracao.backend.application.dataobject.response.cliente;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ClienteDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PutUpdateClienteResp(
        @JsonProperty String resposta,
        @JsonProperty ClienteDTO cliente
) {
}
