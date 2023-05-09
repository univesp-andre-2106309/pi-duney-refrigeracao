package com.duneyrefrigeracao.backend.application.mapper;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.FornecedorServicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoServicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.TecnicoServicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.servico.PostCreateServicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.servico.PutUpdateServicoReq;
import com.duneyrefrigeracao.backend.domain.model.FornecedorServico;
import com.duneyrefrigeracao.backend.domain.model.ProdutoServico;
import com.duneyrefrigeracao.backend.domain.model.Servico;
import com.duneyrefrigeracao.backend.domain.model.TecnicoServico;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ServicoMapper {

    ServicoMapper INSTANCE = Mappers.getMapper(ServicoMapper.class);


    Servico createServicoToServico(PostCreateServicoReq request);
    Servico updateServicoToServico(PutUpdateServicoReq request);
    ServicoDTO servidoParaServicoDTO(Servico req);


    List<ProdutoServico> prodServDtoToProService(Collection<ProdutoServicoDTO> req);
    List<TecnicoServico> tecServDtoToTecService(Collection<TecnicoServicoDTO> req);
    List<FornecedorServico> forServDtoToForServ(Collection<FornecedorServicoDTO> req);


    Collection<ProdutoServicoDTO> prodServicoToProdServicoDto(List<ProdutoServico> req);
    Collection<TecnicoServicoDTO> tecServToTecServDto(List<TecnicoServico> req);
    Collection<FornecedorServicoDTO> forServToForServDto(List<FornecedorServico> req);

}
