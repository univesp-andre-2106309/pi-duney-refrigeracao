package com.duneyrefrigeracao.backend.application.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostLoginStatus(
        @JsonProperty(required = true) String jwtToken,
        @JsonProperty(required = true) String refreshToken
) {
}
