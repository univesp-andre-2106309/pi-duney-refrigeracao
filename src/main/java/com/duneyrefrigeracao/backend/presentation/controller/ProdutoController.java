package com.duneyrefrigeracao.backend.presentation.controller;

import com.duneyrefrigeracao.backend.application.dataobject.generic.ExceptionResponse;
import com.duneyrefrigeracao.backend.application.dataobject.modelresponse.ProdutoDTO;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PatchProdutoEstoqueReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PatchProdutoPrecoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PostCreateProdutoReq;
import com.duneyrefrigeracao.backend.application.dataobject.request.produto.PutUpdateProdutoReq;
import com.duneyrefrigeracao.backend.application.dataobject.response.produto.*;
import com.duneyrefrigeracao.backend.application.mapper.ProdutoMapper;
import com.duneyrefrigeracao.backend.application.service.IProdutoService;
import com.duneyrefrigeracao.backend.domain.enums.LogLevel;
import com.duneyrefrigeracao.backend.domain.enums.OrderByEnum;
import com.duneyrefrigeracao.backend.domain.exception.ProdutoExistenteException;
import com.duneyrefrigeracao.backend.domain.exception.ProdutoNotFoundException;
import com.duneyrefrigeracao.backend.domain.exception.ValorMenorQueZeroException;
import com.duneyrefrigeracao.backend.domain.model.Produto;
import com.duneyrefrigeracao.backend.domain.valueobject.Tuple;
import com.duneyrefrigeracao.backend.infrastructure.logging.ILogging;
import com.duneyrefrigeracao.backend.infrastructure.logging.Logging;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private final IProdutoService _service;
    private final ILogging _logging;

    public ProdutoController(IProdutoService service) {
        this._service = service;
        this._logging = new Logging(ProdutoController.class);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> postCreateProduto(@RequestBody PostCreateProdutoReq request) {
        try {
            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            Produto produto = mapper.AddProdutoParaProduto(request);
            this._service.saveProduto(produto);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PostAdicionarProdutoResp(
                    "Adicionado com sucesso",
                    formattedDate
            ));
        } catch (ProdutoExistenteException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Produto já existente no banco de dados!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao adicionar produto",
                    er.getMessage()
            ));
        } catch (ValorMenorQueZeroException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Valor do preço é menor que zero!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro ao adicionar produto",
                    er.getMessage()
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("find")
    public ResponseEntity<Object> getProdutoById(@RequestParam(name = "id") int id) {
        try {
            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            Produto produto = this._service.getProdutoById(Long.valueOf(id));
            ProdutoDTO produtoDTO = mapper.ProdutoParaProdutoDTO(produto);

            return ResponseEntity.ok().body(new GetProdutoByIdResp(
                    "Busca por produto",
                    produtoDTO
            ));
        } catch (NumberFormatException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Requisição foi feita com uma id de tipo invalido!"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (ProdutoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Produto não encontrado"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Produto"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUpdateProduto(@RequestParam(value = "id") int id,
                                                   @RequestBody PutUpdateProdutoReq request) {
        try {
            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            Produto produto = mapper.UpdateProdutoParaProduto(request);

            this._service.updateProduto(produto, Long.valueOf(id));

            ProdutoDTO produtoDTO = mapper.ProdutoParaProdutoDTO(produto);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PutUpdateProdutoResp(
                    formattedDate,
                    produtoDTO
            ));
        } catch (NumberFormatException er) {

            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    "Valor de ID não valido!"
            ));
        } catch (ProdutoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Produto não encontrado"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Produto"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/search")
    public ResponseEntity<Object> getBuscarProdutos(@RequestParam(value = "nome", defaultValue = "") String nomeValue,
                                                    @RequestParam(value = "precoMin", required = false) BigDecimal precoMinValue,
                                                    @RequestParam(value = "precoMax", required = false) BigDecimal precoMaxValue,
                                                    @RequestParam(value = "order", defaultValue = "DESC") OrderByEnum orderBy,
                                                    @RequestParam(value = "index", defaultValue = "0") int indexValue) {
        try {
            Tuple<Long, Collection<Produto>> result =
                    this._service.getProdutoByParams(nomeValue, precoMinValue, precoMaxValue, orderBy, indexValue);
            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            List<ProdutoDTO> lista = mapper.ListaProdutoParaListaProdutoDTO(result.getSecondValue().stream().toList());


            return ResponseEntity.ok().body(new GetBuscarProdutosResp(
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

    @PatchMapping("/update-estoque")
    public ResponseEntity<Object> patchEstoque(@RequestParam(value = "id") Long id,
                                               @RequestBody PatchProdutoEstoqueReq request) {
        try {
            Produto produto = this._service.updateProdutoEstoque(request.estoque(), id);

            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            ProdutoDTO produtoDTO = mapper.ProdutoParaProdutoDTO(produto);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PatchProdutoEstoqueResp(
                    formattedDate,
                    produtoDTO
            ));

        } catch (ValorMenorQueZeroException er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Valor informado menor que zero -> %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    er.getMessage()
            ));

        } catch (ProdutoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Tecnico não encontrado"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Produto"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/update-preco")
    public ResponseEntity<Object> patchPreco(@RequestParam(value = "id") Long id,
                                             @RequestBody PatchProdutoPrecoReq request) {
        try {
            Produto produto = this._service.updateProdutoPreco(request.preco(), id);

            ProdutoMapper mapper = Mappers.getMapper(ProdutoMapper.class);
            ProdutoDTO produtoDTO = mapper.ProdutoParaProdutoDTO(produto);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
            String formattedDate = formatter.format(date);

            return ResponseEntity.ok().body(new PatchProdutoPrecoResp(
                    formattedDate,
                    produtoDTO
            ));

        } catch (ValorMenorQueZeroException er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Valor informado menor que zero -> %s", er.getMessage()));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Erro de requisição",
                    er.getMessage()
            ));

        } catch (ProdutoNotFoundException er) {
            this._logging.LogMessage(LogLevel.INFO, String.format("Produto não encontrado"));
            return ResponseEntity.badRequest().body(new ExceptionResponse(
                    "Busca vazia",
                    "Não foi possivel encontrar um Produto"
            ));
        } catch (Exception er) {
            this._logging.LogMessage(LogLevel.ERROR, String.format("Erro não tratado -> %s", er.getMessage()));
            return ResponseEntity.internalServerError().build();
        }
    }


}
