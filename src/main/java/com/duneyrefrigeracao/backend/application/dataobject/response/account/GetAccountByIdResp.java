package com.duneyrefrigeracao.backend.application.dataobject.response.account;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.AccountDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetAccountByIdResp(
        @JsonProperty String horario,
        @JsonProperty AccountDTO account
        ) {
}
