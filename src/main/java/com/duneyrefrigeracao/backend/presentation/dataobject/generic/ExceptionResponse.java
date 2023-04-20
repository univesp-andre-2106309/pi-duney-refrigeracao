package com.duneyrefrigeracao.backend.presentation.dataobject.generic;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExceptionResponse(
        @JsonProperty String title,
        @JsonProperty String message
) {

}
