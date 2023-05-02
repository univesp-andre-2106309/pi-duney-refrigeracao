package com.duneyrefrigeracao.backend.application.dataobject.request.fornecedor;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostBuscarFornecedoresReq(
        @JsonProperty String nome,
        @JsonProperty String documento
) {
}
