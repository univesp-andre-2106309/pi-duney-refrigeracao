package com.duneyrefrigeracao.backend.application.dataobject.request.cliente;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostBuscarClientesReq(
        @JsonProperty String nome,
        @JsonProperty String documento
) {
}
