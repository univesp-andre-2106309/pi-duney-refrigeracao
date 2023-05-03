package com.duneyrefrigeracao.backend.application.dataobject.response.tecnico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.TecnicoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetTecnicoByIdResp(
        @JsonProperty String title,
        @JsonProperty TecnicoDTO tecnico
) {
}
