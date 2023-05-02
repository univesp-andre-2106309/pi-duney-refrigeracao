package com.duneyrefrigeracao.backend.application.dataobject.request.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record PutUpdateAccountReq(
        @JsonProperty(required = true) String firstName,
        @JsonProperty(required = true) String lastName,
        @JsonProperty(required = true) String email,
        @JsonProperty(required = true) @JsonFormat(pattern = "dd/MM/YYYY") Date birthDate
){
}
