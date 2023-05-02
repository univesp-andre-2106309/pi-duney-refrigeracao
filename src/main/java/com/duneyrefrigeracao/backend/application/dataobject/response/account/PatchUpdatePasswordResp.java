package com.duneyrefrigeracao.backend.application.dataobject.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchUpdatePasswordResp(
        @JsonProperty String horario
) {
}
