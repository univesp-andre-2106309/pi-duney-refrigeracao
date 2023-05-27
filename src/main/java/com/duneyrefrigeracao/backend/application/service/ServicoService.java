package com.duneyrefrigeracao.backend.application.service;

import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.duneyrefrigeracao.backend.application.mapper.ServicoMapper;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.enums.StatusServico;
import com.duneyrefrigeracao.backend.domain.exception.ServicoNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.*;
import com.duneyrefrigeracao.backend.domain.model.TecnicoServico;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import com.duneyrefrigeracao.backend.infrastructure.repository.IUnitOfWork;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicoService implements IServicoService {


    private final IUnitOfWork _unitOfWork;
    private static final int _pageSize = 10;
    private final ILogging _logging;
    private final SimpleDateFormat dateFormat;

    public ServicoService(IUnitOfWork unitOfWork) {
        this._unitOfWork = unitOfWork;
        this._logging = new Logging(ServicoService.class);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public Tuple<Long, Collection<ServicoDTO>> getServicosParams(Date dtInicial, Date dtFinal, OrderByEnum order, int index, StatusServico statusServico) {

        try {

            Collection<Long> collection;
            Collection<ServicoDTO> servicoCollection;
            Pageable pageable = PageRequest.of(index, _pageSize);
            Page<Servico> page;
            Long count;


            String DB_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

            String dateMin = "1900-01-01 00:00:00";
            String dateMax = "2300-12-31 23:59:59";
            DateFormat formatter = new SimpleDateFormat(DB_DATE_PATTERN);


            if (dtInicial == null) {
                dtInicial = formatter.parse(dateMin);
            }

            if (dtFinal == null) {
                dtFinal = formatter.parse(dateMax);
            }

            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando por parametros: dtInicial: %s, dtFinal: %s, Ordem: %s, Index: %d", dateFormat.format(dtInicial), dateFormat.format(dtFinal), order.name(), index));

            if(statusServico == null) {
                count = this._unitOfWork.getServicoRepository().countByDtCriacaoBetween(dtInicial, dtFinal);
            } else{
                count = this._unitOfWork.getServicoRepository().countByDtCriacaoBetweenAndStatusServico(dtInicial, dtFinal,statusServico);
            }

            this._logging.LogMessage(LogLevel.INFO, String.format("Foram encontrado %d resultados", count));

            if (order.equals(OrderByEnum.ASC)) {
                page = this._unitOfWork.getServicoRepository().searchAscend(dtInicial, dtFinal, pageable);
            } else {
                page = this._unitOfWork.getServicoRepository().searchDescend(dtInicial, dtFinal, pageable);
            }

            collection = page.getContent().stream().map(Servico::getId).collect(Collectors.toList());


            servicoCollection = collection.stream().map(item -> {
                        try {
                            return this.getServicoById(item);
                        } catch (Exception er) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if(statusServico != null) {
                servicoCollection = servicoCollection.stream().filter(item -> item.getStatusServico().equals(statusServico)).collect(Collectors.toList());
            }

            _logging.LogMessage(LogLevel.INFO, String.format("Resultado -> %s", servicoCollection.toString()));


            return new Tuple<>(count, servicoCollection);
        } catch (ParseException er) {
            return new Tuple<>(0L, new ArrayList<>());
        } catch (Exception er) {
            throw er;
        }

    }

    @Override
    public Servico saveServico(Servico servico, List<ProdutoServico> colProduto,
                               List<TecnicoServico> colTecnico,
                               List<FornecedorServico> colFornecedor,
                               Long clienteId) {

        servico.setDtCriacao(new Date());

        this._logging.LogMessage(LogLevel.INFO, String.format("Salvando Servico -> %s", servico.toString()));

        Cliente cliente =this._unitOfWork.getClienteRepository().getReferenceById(clienteId);
        servico.setCliente(cliente);

        Servico savedServico = this._unitOfWork.getServicoRepository().save(servico);


        colProduto = colProduto.stream().peek(item ->
                {
                    item.setServico(savedServico);
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setProduto(this._unitOfWork.getProdutoRepository().getReferenceById(item.getProduto().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setProduto(null);
                    }
                    item.setId(null);
                }
        ).filter(item -> item.getProduto() != null).toList();

        this._logging.LogMessage(LogLevel.INFO, String.format("ProdutoServico -> %s", colProduto.toString()));

        colFornecedor = colFornecedor.stream().peek(item -> {

                    item.setServico(savedServico);
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setFornecedor(this._unitOfWork.getFornecedorRepository().getReferenceById(item.getFornecedor().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setFornecedor(null);
                    }
                    item.setId(null);
                })
                .filter(item -> item.getFornecedor() != null).toList();

        this._logging.LogMessage(LogLevel.INFO, String.format("FornecedorServico -> %s", colFornecedor.toString()));

        colTecnico = colTecnico.stream().peek(item ->
                {

                    item.setServico(savedServico);
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setTecnico(this._unitOfWork.getTecnicoRepository().getReferenceById(item.getTecnico().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setTecnico(null);
                    }
                    item.setId(null);
                }
        ).filter(item -> item.getTecnico() != null).toList();

        this._logging.LogMessage(LogLevel.INFO, String.format("TecnicoServico -> %s", colTecnico.toString()));

        this._unitOfWork.getFornecedorServicoRepository().saveAll(colFornecedor);
        this._unitOfWork.getProdutoServicoRepository().saveAll(colProduto);
        this._unitOfWork.getTecnicoServicoRepository().saveAll(colTecnico);


        this._logging.LogMessage(LogLevel.INFO, String.format("Servico salvo com sucesso ID -> %d", savedServico.getId()));
        return servico;
    }


    @Override
    public Servico updateServico(Servico servico, List<ProdutoServico> colProduto, List<TecnicoServico> colTecnico, List<FornecedorServico> colFornecedor, Long clienteId) {
        Servico upServico;
        try {
            upServico = this._unitOfWork.getServicoRepository().getReferenceById(servico.getId());

            this._logging.LogMessage(LogLevel.INFO, String.format("Atualizando Servico -> %s", upServico.toString()));
            Cliente cliente =this._unitOfWork.getClienteRepository().getReferenceById(clienteId);
            servico.setCliente(cliente);
            servico.setDtCriacao(upServico.getDtCriacao());

            this._unitOfWork.getServicoRepository().save(servico);

            colProduto = colProduto.stream().peek(item -> {
                item.setServico(upServico);
                if (item.getId() == null  || item.getId() == 0) {
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setProduto(this._unitOfWork.getProdutoRepository().getReferenceById(item.getProduto().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setProduto(null);
                    }
                }
            }).filter(item -> item.getProduto() != null).collect(Collectors.toList());

            this._logging.LogMessage(LogLevel.INFO, String.format("ProdutoServico -> %s", colProduto.toString()));

            colFornecedor = colFornecedor.stream().peek(item -> {
                item.setServico(upServico);
                if (item.getId() == null  || item.getId() == 0) {
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setFornecedor(this._unitOfWork.getFornecedorRepository().getReferenceById(item.getFornecedor().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setFornecedor(null);
                    }
                }
            }).filter(item -> item.getFornecedor() != null).collect(Collectors.toList());

            this._logging.LogMessage(LogLevel.INFO, String.format("FornecedorServico -> %s", colFornecedor.toString()));

            colTecnico = colTecnico.stream().peek(item -> {
                item.setServico(upServico);
                if (item.getId() == null  || item.getId() == 0) {
                    item.setDtCriacao(new Date());
                    item.setDeleted(false);
                    try {
                        item.setTecnico(this._unitOfWork.getTecnicoRepository().getReferenceById(item.getTecnico().getId()));
                    } catch (ServicoNotFoundException er) {
                        item.setTecnico(null);
                    }
                }
            }).filter(item -> item.getTecnico() != null).collect(Collectors.toList());

            this._logging.LogMessage(LogLevel.INFO, String.format("TecnicoServico -> %s", colTecnico.toString()));

            //Salvando os novos

            this._logging.LogMessage(LogLevel.INFO, "Salvando chave estrangeiras de servico...");

            List<ProdutoServico> returnedProdServ = this._unitOfWork.getProdutoServicoRepository().saveAll(colProduto);
            List<TecnicoServico> returnedTecServ = this._unitOfWork.getTecnicoServicoRepository().saveAll(colTecnico);
            List<FornecedorServico> returnedForServ = this._unitOfWork.getFornecedorServicoRepository().saveAll(colFornecedor);



            //Desativando os inutilizados

            List<Long> listProdutoId = returnedProdServ.stream().map(ProdutoServico::getId).toList();
            List<Long> listTecnicoId = returnedTecServ.stream().map(TecnicoServico::getId).toList();
            List<Long> listFornecedorId = returnedForServ.stream().map(FornecedorServico::getId).toList();

            this._logging.LogMessage(LogLevel.INFO, "desabilitando conexões desnecessarias de servico...");

            if(listProdutoId.isEmpty()) {
                this._unitOfWork.getProdutoServicoRepository().deleteByServicoId(upServico.getId());
            } else {
                this._unitOfWork.getProdutoServicoRepository().deleteByServicoIdAndIdNotIn(upServico.getId(), listProdutoId);
            }

            if(listTecnicoId.isEmpty()) {
                this._unitOfWork.getTecnicoServicoRepository().deleteByServicoId(upServico.getId());
            } else {
                this._unitOfWork.getTecnicoServicoRepository().deleteByServicoIdAndIdNotIn(upServico.getId(), listTecnicoId);
            }

            if(listFornecedorId.isEmpty()) {
                this._unitOfWork.getFornecedorServicoRepository().deleteByServicoId(upServico.getId());
            } else {
                this._unitOfWork.getFornecedorServicoRepository().deleteByServicoIdAndIdNotIn(upServico.getId(), listFornecedorId);
            }

            this._logging.LogMessage(LogLevel.INFO, "Serviço atualizado com sucesso!");
            return upServico;
        } catch (Exception er) {
            throw new ServicoNotFoundException();
        }
    }


    @Override
    public ServicoDTO getServicoById(Long id) {
        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando serviço de ID - %d", id));
            ServicoMapper mapper = Mappers.getMapper(ServicoMapper.class);
            Servico servico = this._unitOfWork.getServicoRepository().getReferenceById(id);

            this._logging.LogMessage(LogLevel.INFO, String.format("Encontrado serviço - %s", servico.toString()));

            ServicoDTO servicoDTO = mapper.servidoParaServicoDTO(servico);

            servicoDTO.setListaProduto(mapper.prodServicoToProdServicoDto(
                    this._unitOfWork.getProdutoServicoRepository().findAllByServicoIdAndAndDeletedIsFalse(id)));
            servicoDTO.setListaTecnico(mapper.tecServToTecServDto(
                    this._unitOfWork.getTecnicoServicoRepository().findAllByServicoIdAndAndDeletedIsFalse(id)));
            servicoDTO.setListaFornecedor(mapper.forServToForServDto(
                    this._unitOfWork.getFornecedorServicoRepository().findAllByServicoIdAndAndDeletedIsFalse(id)));




            return servicoDTO;
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao busca serviço de ID - %d", id));
            throw new ServicoNotFoundException();
        }

    }

}
