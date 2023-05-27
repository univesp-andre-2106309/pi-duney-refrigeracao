package com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.FornecedorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PatchDeleteFornecedorResp(
        @JsonProperty FornecedorDTO fornecedor
        ) {
}
