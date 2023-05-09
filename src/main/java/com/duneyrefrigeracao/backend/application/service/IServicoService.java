package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.model.FornecedorServico;
import com.duneyrefrigeracao.backend.domain.model.ProdutoServico;
import com.duneyrefrigeracao.backend.domain.model.Servico;
import com.duneyrefrigeracao.backend.domain.model.TecnicoServico;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IServicoService {

    Tuple<Long, Collection<ServicoDTO>> getServicosParams(Date dtInicial, Date dtFinal, OrderByEnum order, int index);
    Servico saveServico(Servico servico, List<ProdutoServico> colProduto,
                        List<TecnicoServico> colTecnico, List<FornecedorServico> colFornecedor);

    Servico updateServico(Servico servico, List<ProdutoServico> colProduto,
                          List<TecnicoServico> colTecnico, List<FornecedorServico> colFornecedor);
    ServicoDTO getServicoById(Long id);

}
