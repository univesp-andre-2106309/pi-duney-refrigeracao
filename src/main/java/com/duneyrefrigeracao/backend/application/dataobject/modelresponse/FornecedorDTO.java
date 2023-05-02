package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FornecedorDTO(
        @JsonProperty(required = true) Long id,
        @JsonProperty(required = true) String nome,
        @JsonProperty(required = true) String endereco,
        @JsonProperty(required = true) String cidade,
        @JsonProperty(required = true) String estado,
        @JsonProperty(required = true) String cep,
        @JsonProperty(required = true) String telefone,
        @JsonProperty(required = true) String email,
        @JsonProperty(required = true) String cnpj
) {
}
