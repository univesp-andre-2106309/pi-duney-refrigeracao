package com.duneyrefrigeracao.backend.application.dataobject.modelresponse;

import com.duneyrefrigeracao.backend.domain.enums.StatusServico;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoDTO {
    @JsonProperty
    private Long id;
    @JsonProperty
    private String descricao;
    @JsonProperty
    private StatusServico statusServico;
    @JsonProperty @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dtInicial;
    @JsonProperty @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dtFinalizacao;
    @JsonProperty
    private Collection<ProdutoServicoDTO> listaProduto;
    @JsonProperty
    private Collection<TecnicoServicoDTO> listaTecnico;
    @JsonProperty
    private Collection<FornecedorServicoDTO> listaFornecedor;
}
