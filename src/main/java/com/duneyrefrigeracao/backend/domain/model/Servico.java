package com.duneyrefrigeracao.backend.domain.model;

import com.duneyrefrigeracao.backend.domain.enums.StatusServico;
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
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private StatusServico statusServico;

    @Column(nullable = false)
    private Date dtCriacao;

    @Column(nullable = false)
    private Date dtInicial;

    @Column
    private Date dtFinalizacao;
}
