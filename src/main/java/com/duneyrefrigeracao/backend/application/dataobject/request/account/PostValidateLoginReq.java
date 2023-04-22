package com.duneyrefrigeracao.backend.application.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostValidateLoginReq(
        @JsonProperty String email,
        @JsonProperty String password
) {
}
