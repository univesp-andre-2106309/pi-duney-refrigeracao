package com.duneyrefrigeracao.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String endereco;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String cep;
    @Column(nullable = false)
    private String telefone;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private boolean isActive;
    @Column(nullable = false)
    private Date dtCadastro;

}
