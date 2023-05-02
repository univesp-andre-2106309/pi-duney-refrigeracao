package com.duneyrefrigeracao.backend.application.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchUpdatePasswordReq(
        @JsonProperty(required = true, value = "password") String oldPassword,
        @JsonProperty(required = true, value = "newPassword") String newPassword,
        @JsonProperty(required = true, value = "repeatPassword") String newPasswordCheck
) {
}
