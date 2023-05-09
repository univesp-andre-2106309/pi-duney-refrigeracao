package com.duneyrefrigeracao.backend.application.dataobject.response.servico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetServicoByIdResp(
        @JsonProperty String title,
        @JsonProperty ServicoDTO result
        ) {
}
