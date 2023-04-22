package com.duneyrefrigeracao.backend.application.dataobject.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostCreateAccountResp(
        @JsonProperty String message,
        @JsonProperty String username,
        @JsonProperty String email
) {
}
