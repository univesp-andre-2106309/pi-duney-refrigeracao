package com.duneyrefrigeracao.backend.domain.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @Column(nullable = false)
    private BigDecimal precoProduto;

}
