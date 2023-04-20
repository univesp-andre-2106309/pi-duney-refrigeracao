package com.duneyrefrigeracao.backend.presentation.dataobject.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostValidateLoginResp(
        @JsonProperty String message,
        @JsonProperty boolean validated
) {
}
