package com.duneyrefrigeracao.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private BigDecimal preco;
    @Column(nullable = false)
    private String estoque;

    @Column(nullable = false)
    private boolean isActive;
    @Column(nullable = false)
    private Date dtCadastro;
}
