package com.duneyrefrigeracao.backend.application.dataobject.response.servico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PutUpdateServicoResp(
        @JsonProperty String resposta,
        @JsonProperty ServicoDTO result
        ) {
}
