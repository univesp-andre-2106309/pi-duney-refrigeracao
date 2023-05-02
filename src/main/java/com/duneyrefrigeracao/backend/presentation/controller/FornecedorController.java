package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.FornecedorDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.fornecedor.PostCreateFornecedorReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.fornecedor.PutUpdateFornecedorReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor.GetFornecedorByIdResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor.PostAdicionarFornecedorResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor.PostBuscarFornecedoresResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.fornecedor.PutUpdateFornecedorResp;
import com.duneyrefrigeracao.backend.application.mapper.FornecedorMapper;
import com.duneyrefrigeracao.backend.application.service.IFornecedorService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.exception.FornecedorNotFoundException;
import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
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
@RequestMapping("/api/fornecedor")
public class FornecedorController {

    private final ILogging _logging;
    private final IFornecedorService _service;

    public FornecedorController(IFornecedorService service) {
        this._service = service;
        this._logging = new Logging((ClienteController.class));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> postCreateFornecedor(@RequestBody PostCreateFornecedorReq request) {
        try {
            FornecedorMapper mapper = Mappers.getMapper(FornecedorMapper.class);
            Fornecedor fornecedor = mapper.AddFornecedorParaFornecedor(request);
            this._service.saveFornecedor(fornecedor);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PostAdicionarFornecedorResp(
                    "Adicionado com sucessso!",
                    formattedDate
            ));
        } catch (FornecedorNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Fornecedor já existente no banco de dados!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao adicionar fornecedor",
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

    @GetMapping("find-fornecedor")
    public ResponseEntity<Object> getFornecedorById(@RequestParam(name = "id") String id) {
        try {
            Fornecedor fornecedor = this._service.getFornecedorById(Long.valueOf(id));
            FornecedorMapper mapper = Mappers.getMapper(FornecedorMapper.class);
            FornecedorDTO fornecedorDTO = mapper.FornecedorParaFornecedorDTO(fornecedor);


            return ResponseEntity.ok().body(new GetFornecedorByIdResp(
                    "Busca por fornecedor",
                    fornecedorDTO
            ));
        } catch (NumberFormatException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch(FornecedorNotFoundException er){
            this._logging.LogMessage(LogLevel.ERROR, String.format("Fornecedor não encontrado!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Fornecedor"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUpdateFornecedor(@RequestParam(value = "id") String id,
                                                      @RequestBody PutUpdateFornecedorReq request) {
        try {
            FornecedorMapper mapper = Mappers.getMapper(FornecedorMapper.class);
            Fornecedor fornecedor = mapper.UpdateFornecedorParaFornecedor(request);

            this._service.updateFornecedor(fornecedor, Long.valueOf(id));

            FornecedorDTO fornecedorDTO = mapper.FornecedorParaFornecedorDTO(fornecedor);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PutUpdateFornecedorResp(
                    formattedDate,
                    fornecedorDTO
            ));

        } catch (NumberFormatException er) {

            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        }  catch(FornecedorNotFoundException er){
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao atualizar dados",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/busca")
    public ResponseEntity<Object> getBuscarFornecedores(@RequestParam(value = "documento", defaultValue = "") String documentoValue,
                                                        @RequestParam(value = "nome", defaultValue = "") String nomeValue,
                                                        @RequestParam(value = "index", defaultValue = "0") String indexValue) {
        try {
            Tuple<Long, Collection<Fornecedor>> result =
                    this._service.getFornecedoresByParams(nomeValue,documentoValue,Integer.parseInt(indexValue));
            FornecedorMapper mapper = Mappers.getMapper(FornecedorMapper.class);
            List<FornecedorDTO> lista = mapper.ListaFornecedorParaListaFornecedorDTO(result.getSecondValue().stream().toList());

            return ResponseEntity.ok().body(new PostBuscarFornecedoresResp(
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
