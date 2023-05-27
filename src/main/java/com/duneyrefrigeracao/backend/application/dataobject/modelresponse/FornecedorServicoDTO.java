package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record FornecedorServicoDTO(
        @JsonProperty Long id,
        @JsonProperty FornecedorDTO fornecedor,
        @JsonProperty Date dtCriacao,
        @JsonProperty int quantidade

) {

}
