package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.TecnicoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.tecnico.PostCreateTecnicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.tecnico.PutUpdateTecnicoReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.tecnico.GetBuscarTecnicosResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.tecnico.GetTecnicoByIdResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.tecnico.PostAdicionarTecnicoResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.tecnico.PutUpdateTecnicoResp;
import com.duneyrefrigeracao.backend.application.mapper.TecnicoMapper;
import com.duneyrefrigeracao.backend.application.service.ITecnicoService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.exception.TecnicoExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.TecnicoNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Tecnico;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tecnico")
public class TecnicoController {

    private final ILogging _logging;
    private final ITecnicoService _service;

    public TecnicoController(ITecnicoService service) {
        this._service = service;
        this._logging = new Logging(TecnicoController.class);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> postCreateTecnico(@RequestBody PostCreateTecnicoReq request) {
        try {
            TecnicoMapper mapper = Mappers.getMapper(TecnicoMapper.class);
            Tecnico tecnico = mapper.AddTecnicoParaTecnico(request);
            this._service.saveTecnico(tecnico);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);


            return ResponseEntity.ok().body(new PostAdicionarTecnicoResp(
                    "Adicionado com sucesso",
                    formattedDate
            ));
        } catch (TecnicoExistenteException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Tecnico já existent eno banco de dados!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao adicionar tecnico",
                    er.getMessage()
            ));
        } catch (EmailPatternException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Formatação do campo email invalida - %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de validação",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("find-tecnico")
    public ResponseEntity<Object> getTecnicoById(@RequestParam(name = "id") String id) {
        try {
            Tecnico tecnico = this._service.getTecnicoById(Long.valueOf(id));
            TecnicoMapper mapper = Mappers.getMapper(TecnicoMapper.class);
            TecnicoDTO tecnicoDTO = mapper.TecnicoParaTecnicoDTO(tecnico);

            return ResponseEntity.ok().body(new GetTecnicoByIdResp(
                    "Busca por tecnico",
                    tecnicoDTO
            ));
        } catch (NumberFormatException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (TecnicoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Tecnico não encontrado"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Tecnico"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUpdateTecnico(@RequestParam(value = "id") String id,
                                                   @RequestBody PutUpdateTecnicoReq request) {
        try {
            TecnicoMapper mapper = Mappers.getMapper(TecnicoMapper.class);
            Tecnico tecnico = mapper.UpdateTecnicoParaTecnico(request);

            this._service.updateTecnico(tecnico, Long.valueOf(id));

            TecnicoDTO tecnicoDTO = mapper.TecnicoParaTecnicoDTO(tecnico);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PutUpdateTecnicoResp(
                    formattedDate,
                    tecnicoDTO
            ));
        } catch (NumberFormatException er) {

            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (TecnicoNotFoundException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao atualizar dados",
                    er.getMessage()
            ));
        }
        catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/busca")
    public ResponseEntity<Object> getBuscarTecnicos(@RequestParam(value = "documento", defaultValue = "") String documentoValue,
                                                    @RequestParam(value = "nome", defaultValue = "") String nomeValue,
                                                    @RequestParam(value = "index", defaultValue = "0") String indexValue) {
        try {
            Tuple<Long, Collection<Tecnico>> result =
                    this._service.getTecnicosByParams(nomeValue,documentoValue,Integer.parseInt(indexValue));
            TecnicoMapper mapper = Mappers.getMapper(TecnicoMapper.class);
            List<TecnicoDTO> lista = mapper.ListaTecnicoParaListaTecnicoDTO(result.getSecondValue().stream().toList());


            return ResponseEntity.ok().body(new GetBuscarTecnicosResp(
                    result.getFirstValue(),
                    lista
            ));
        } catch (NumberFormatException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

}
