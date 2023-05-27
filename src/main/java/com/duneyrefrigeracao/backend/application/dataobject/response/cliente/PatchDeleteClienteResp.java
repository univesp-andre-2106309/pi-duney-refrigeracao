package com.duneyrefrigeracao.backend.application.dataobject.response.cliente;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ClienteDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchDeleteClienteResp(
        @JsonProperty ClienteDTO cliente
        ) {
}
