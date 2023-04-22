package com.duneyrefrigeracao.backend.application.dataobject.generic;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty String title,
        @JsonProperty String message
) {

}
