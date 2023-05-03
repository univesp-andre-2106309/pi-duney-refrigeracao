package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ClienteDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PutUpdateClienteReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PostAdicionarClienteReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.GetClienteByIdResponse;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.PostAdicionarClienteResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.PostBuscarClientesResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.PutUpdateClienteResp;
import com.duneyrefrigeracao.backend.application.mapper.ClienteMapper;
import com.duneyrefrigeracao.backend.application.service.IClienteService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.exception.ClienteExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.ClienteNotFoundException;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
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
@RequestMapping("/api/cliente")
public class ClienteController {


    private final IClienteService _service;
    private final ILogging _logging;

    public ClienteController(IClienteService _service) {
        this._service = _service;
        this._logging = new Logging(ClienteController.class);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> postBuscarClientes(@RequestParam(value = "documento", defaultValue = "") String documentoValue,
                                                     @RequestParam(value = "nome", defaultValue = "") String nomeValue,
                                                     @RequestParam(value = "index", defaultValue = "0") Integer indexValue) {
        try {
            this._logging.LogMessage(LogLevel.INFO, String.format("Buscando clientes, index %s, campo nome: %s, campo documento %s", indexValue, nomeValue, documentoValue));
            Tuple<Long, Collection<Cliente>> result =
                    _service.getClientesByParams(nomeValue, documentoValue, indexValue );

            ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);
            List<ClienteDTO> lista = mapper.ListaCLienteParaListaClienteDTO(result.getSecondValue().stream().toList());

            return ResponseEntity.ok().body(new PostBuscarClientesResp(
                    result.getFirstValue(),
                    lista
            ));

        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> postAdicionarClientes(@RequestBody PostAdicionarClienteReq request) {
        try {
            ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);
            Cliente cliente = mapper.AddClienteParaCliente(request);
            _service.saveCliente(cliente);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(date);


            return ResponseEntity.ok().body(new PostAdicionarClienteResp(
                    "Adicionado com sucessso!",
                    formattedDate
            ));
        } catch (EmailPatternException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Formatação do campo email invalida - %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de validação",
                    er.getMessage()
            ));
        } catch (ClienteExistenteException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Cliente já existente no banco de dados!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao adicionar cliente",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUpdateCliente(@RequestParam(value = "id") Long id,
                                                   @RequestBody PutUpdateClienteReq request) {
        ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);
        Cliente cliente = mapper.UpdateClienteParaCliente(request);
        try {
            this._service.updateCliente(cliente, id);

            ClienteDTO clienteDTO = mapper.ClienteParaClienteDTO(cliente);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PutUpdateClienteResp(formattedDate, clienteDTO));
        } catch (NumberFormatException er) {

            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (ClienteNotFoundException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao atualizar dados",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("find")
    public ResponseEntity<Object> getClienteById(@RequestParam(value = "id") Long id) {

        try{
            Cliente cliente = this._service.getClienteById(id);
            ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);
            ClienteDTO clienteDTO = mapper.ClienteParaClienteDTO(cliente);

            return ResponseEntity.ok().body(new GetClienteByIdResponse("Busca por cliente",clienteDTO));
        } catch (NumberFormatException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch(Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }

    }


}
