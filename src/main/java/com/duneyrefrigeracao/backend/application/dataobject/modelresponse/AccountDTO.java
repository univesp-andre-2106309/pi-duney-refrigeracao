package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record AccountDTO(
        @JsonProperty String firstName,
        @JsonProperty String lastName,
        @JsonProperty String username,
        @JsonProperty String email,
        @JsonProperty String password,
        @JsonProperty @JsonFormat(pattern = "dd/MM/YYYY") Date birthDate
) {
}
