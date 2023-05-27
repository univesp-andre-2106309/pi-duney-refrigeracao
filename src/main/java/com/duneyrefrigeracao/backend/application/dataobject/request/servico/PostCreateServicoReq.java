package com.duneyrefrigeracao.backend.application.dataobject.request.servico;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.*;
import com.duneyrefrigeracao.backend.domain.enums.StatusServico;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Date;

public record PostCreateServicoReq(

        @JsonProperty Long clienteId,
        @JsonProperty String descricao,
        @JsonProperty StatusServico statusServico,

        @JsonProperty @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East") Date dtInicial,
        @JsonProperty @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East") Date dtFinalizacao,

        @JsonProperty Collection<ProdutoServicoDTO> listaProduto,
        @JsonProperty Collection<FornecedorServicoDTO> listaFornecedor,
        @JsonProperty Collection<TecnicoServicoDTO> listaTecnico
) {
}
