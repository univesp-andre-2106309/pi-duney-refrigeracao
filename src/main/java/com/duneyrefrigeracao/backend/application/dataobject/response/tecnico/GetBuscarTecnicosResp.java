package com.duneyrefrigeracao.backend.application.dataobject.response.tecnico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.TecnicoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record GetBuscarTecnicosResp(
        @JsonProperty Long maxResultSize,
        @JsonProperty Collection<TecnicoDTO> result
) {
}
