package com.duneyrefrigeracao.backend.presentation.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostValidateLoginReq(
        @JsonProperty String email,
        @JsonProperty String password
) {
}
