package com.duneyrefrigeracao.backend.application.dataobject.response.servico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record GetBuscarServicosResp(
        @JsonProperty Long maxResultSize,
        @JsonProperty Collection<ServicoDTO> result
        ) {
}
