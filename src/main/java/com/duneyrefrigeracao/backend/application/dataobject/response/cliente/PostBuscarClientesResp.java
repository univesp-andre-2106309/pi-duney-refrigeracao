package com.duneyrefrigeracao.backend.application.dataobject.response.cliente;

import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record PostBuscarClientesResp(
        //Deve ser configurado o valor TOTAL do resultado, não o valor PARCIAL da paginação
        @JsonProperty Long maxResultSize,
        @JsonProperty Collection<Cliente> result
        ) {
}
