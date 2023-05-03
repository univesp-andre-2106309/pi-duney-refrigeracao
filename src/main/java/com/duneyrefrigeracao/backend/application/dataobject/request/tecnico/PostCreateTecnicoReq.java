package com.duneyrefrigeracao.backend.application.dataobject.request.tecnico;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostCreateTecnicoReq(
       @JsonProperty String nome,
       @JsonProperty String endereco,
       @JsonProperty String cidade,
       @JsonProperty String estado,
       @JsonProperty String cep,
       @JsonProperty String telefone,
       @JsonProperty String email,
       @JsonProperty String cpf
) {
}
