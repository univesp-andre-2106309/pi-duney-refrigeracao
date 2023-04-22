package com.duneyrefrigeracao.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String documento;
    @Column(nullable = false)
    private String cidade;
    @Column(nullable = false)
    private String estado;
    @Column(nullable = false)
    private String uf;
    @Column(nullable = false)
    private String rua;
    @Column(nullable = false)
    private String numResidencia;
    @Column(nullable = false)
    private String bairro;
    @Column
    private String cep;

    @Column
    private String numTel;
    @Column
    private String numCel;
    @Column(nullable = false)
    private String email;

    @Column
    private String info;



    @Column(nullable = false)
    private Date dtNascimento;

    @Column(nullable = false)
    private boolean isEnabled;
}
