package com.duneyrefrigeracao.backend.presentation.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record PostCreateAccountReq(
        @JsonProperty String firstName,
        @JsonProperty String lastName,
        @JsonProperty String username,
        @JsonProperty String email,
        @JsonProperty String password,
        @JsonProperty @JsonFormat(pattern = "dd/MM/YYYY") Date birthDate
) {
};
