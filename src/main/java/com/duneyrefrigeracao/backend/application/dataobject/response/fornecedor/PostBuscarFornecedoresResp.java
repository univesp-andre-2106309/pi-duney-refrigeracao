package com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.FornecedorDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record PostBuscarFornecedoresResp(
        @JsonProperty Long maxResultSize,
        @JsonProperty Collection<FornecedorDTO> result
        ) {
}
