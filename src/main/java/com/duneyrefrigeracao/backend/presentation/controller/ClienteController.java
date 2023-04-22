package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PostBuscarClientesReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.cliente.PostAdicionarClienteReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.PostAdicionarClienteResp;
import com.duneyrefrigeracao.backend.application.dataobject.response.cliente.PostBuscarClientesResp;
import com.duneyrefrigeracao.backend.application.mapper.ClienteMapper;
import com.duneyrefrigeracao.backend.application.service.ClienteService;
import com.duneyrefrigeracao.backend.application.service.IClienteService;
import com.duneyrefrigeracao.backend.domain.exception.ClienteExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.EmailPatternException;
import com.duneyrefrigeracao.backend.domain.model.Cliente;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {


    private final IClienteService _service;

    public ClienteController(IClienteService _service) {
        this._service = _service;
    }

    @PostMapping("/busca")
    public ResponseEntity<Object> postBuscarClientes(@RequestBody PostBuscarClientesReq request,
                                                     @RequestParam(value = "index", defaultValue = "0") String indexValue) {

        try {
            Tuple<Long, Collection<Cliente>> result =
                    _service.getClientesByParams(request.nome(), request.documento(), Integer.parseInt(indexValue));

            return ResponseEntity.ok().body(new PostBuscarClientesResp(
                    result.getFirstValue(),
                    result.getSecondValue()
            ));
        } catch (Exception er) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Object> postAdicionarClientes(@RequestBody PostAdicionarClienteReq request) {
        try {
            ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);
            Cliente cliente = mapper.AddClienteParaCliente(request);
            _service.saveCliente(cliente);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);


            return ResponseEntity.ok().body(new PostAdicionarClienteResp(
                    "Adicionado com sucessso!",
                    formattedDate
            ));
        } catch (EmailPatternException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de validação",
                    er.getMessage()
            ));
        } catch (ClienteExistenteException er) {
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao dicionar cliente",
                    er.getMessage()
            ));
        } catch (Exception er) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
