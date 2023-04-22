package com.duneyrefrigeracao.backend.application.dataobject.request.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.util.Date;

public record PostAdicionarClienteReq(

        @JsonProperty(required = true)
         String nome,
        @JsonProperty(required = true)
         String documento,
        @JsonProperty(required = true)
         String cidade,
        @JsonProperty(required = true)
         String estado,
        @JsonProperty(required = true)
         String uf,
        @JsonProperty(required = true)
         String rua,
        @JsonProperty(required = true)
         String numResidencia,
        @JsonProperty(required = true)
         String bairro,
        @JsonProperty(required = true)
         String cep,

        @JsonProperty(required = true)
         String numTel,
        @JsonProperty
         String numCel,
        @JsonProperty(required = true)
         String email,

        @JsonProperty
         String info,

        @JsonProperty(required = true) @JsonFormat(pattern = "dd/MM/YYYY")
         Date dtNascimento
) {
}
