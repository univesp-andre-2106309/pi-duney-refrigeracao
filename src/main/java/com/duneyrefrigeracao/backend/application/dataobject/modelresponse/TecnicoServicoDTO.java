package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record TecnicoServicoDTO(
        @JsonProperty Long id,
        @JsonProperty TecnicoDTO tecnico,
        @JsonProperty Date dtCriacao
) {
}
