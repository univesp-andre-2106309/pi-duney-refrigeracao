package com.duneyrefrigeracao.backend.application.dataobject.request.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;

import java.util.Date;

public record PutUpdateClienteReq(
        @Column(nullable = false) String nome,
        @Column(nullable = false) String documento,
        @Column(nullable = false) String cidade,
        @Column(nullable = false) String estado,
        @Column(nullable = false) String uf,
        @Column(nullable = false) String rua,
        @Column(nullable = false) String numResidencia,
        @Column(nullable = false) String bairro,
        @Column(nullable = false) String cep,
        @Column(nullable = false) String numTel,
        @Column(nullable = false) String numCel,
        @Column(nullable = false) String email,
        @Column(nullable = false) String info,
        @Column(nullable = false) @JsonFormat(pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East") Date dtNascimento
) {
}
