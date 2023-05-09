package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ServicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.servico.PostCreateServicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.servico.PutUpdateServicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.servico.GetBuscarServicosResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.servico.GetServicoByIdResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.servico.PostCreateServicoResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.servico.PutUpdateServicoResp;
import com.duneyrefrigeracao.backend.application.mapper.ServicoMapper;
import com.duneyrefrigeracao.backend.application.service.ServicoService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.exception.ServicoNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Servico;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/api/servico")
public class ServicoController {

    private final ILogging _logging;
    private final ServicoService _service;

    public ServicoController(ServicoService service) {
        this._service = service;
        this._logging = new Logging(ServicoController.class);
    }


    @PostMapping("/create")
    public ResponseEntity<Object> postCreateServico(@RequestBody PostCreateServicoReq request) {
        try {
            //Cria o serviço.
            ServicoMapper mapper = Mappers.getMapper(ServicoMapper.class);

            Servico servico = mapper.createServicoToServico(request);

            servico = this._service.saveServico(
                    servico,
                    mapper.prodServDtoToProService(request.listaProduto()),
                    mapper.tecServDtoToTecService(request.listaTecnico()),
                    mapper.forServDtoToForServ(request.listaFornecedor()));

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);


            return ResponseEntity.ok().body(new PostCreateServicoResp(
                    "Adicionado com sucesso",
                    formattedDate
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUpdateServico(@RequestBody PutUpdateServicoReq request) {
        try {
            ServicoMapper mapper = Mappers.getMapper(ServicoMapper.class);
            Servico servico = mapper.updateServicoToServico(request);

            servico = this._service.updateServico(
                    servico,
                    mapper.prodServDtoToProService(request.listaProduto()),
                    mapper.tecServDtoToTecService(request.listaTecnico()),
                    mapper.forServDtoToForServ(request.listaFornecedor()));

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PutUpdateServicoResp(
                    formattedDate,
                    mapper.servidoParaServicoDTO(servico)
            ));

        } catch (ServicoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao tentar encontrar o serviço -> %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao buscar o serviço",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/find")
    public ResponseEntity<Object> getServicoById(@RequestParam(name = "id") Long id) {
        try {
            ServicoDTO servicoDTO = this._service.getServicoById(id);

            return ResponseEntity.ok().body(new GetServicoByIdResp(
                    "Busca por Serviço",
                    servicoDTO
            ));
        } catch (ServicoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Erro ao tentar encontrar o serviço de id %d -> %s", id, er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao buscar o serviço",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getBuscarServicos(@RequestParam(value = "dtInicio", required = false) Date dtInicio,
                                                    @RequestParam(value = "dtFim", required = false) Date dtFim,
                                                    @RequestParam(value = "order", defaultValue = "DESC", required = false) OrderByEnum order,
                                                    @RequestParam(value = "index", defaultValue = "0") int indexValue) {
        try {
            Tuple<Long, Collection<ServicoDTO>> result =
                    this._service.getServicosParams(dtInicio, dtFim, order, indexValue);

            return ResponseEntity.ok().body(new GetBuscarServicosResp(
                    result.getFirstValue(),
                    result.getSecondValue()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


}
