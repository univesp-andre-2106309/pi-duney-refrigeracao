package com.duneyrefrigeracao.backend.application.dataobject.response.cliente;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ClienteDTO;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetClienteByIdResponse(
        @JsonProperty String title,
        @JsonProperty ClienteDTO cliente
) {
}
