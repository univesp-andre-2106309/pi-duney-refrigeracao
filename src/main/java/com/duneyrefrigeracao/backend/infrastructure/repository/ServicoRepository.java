package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.enums.StatusServico;
import com.duneyrefrigeracao.backend.domain.model.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;

public interface ServicoRepository extends JpaRepository<Servico, Long> {



    Long countByDtCriacaoBetween(Date dtInicial, Date dtFinal);

    Long countByDtCriacaoBetweenAndStatusServico(Date dtInicial, Date dtFinal, StatusServico statusServico);

    @Query("SELECT s " +
            "FROM Servico s " +
            "WHERE s.dtCriacao >= :dtInicial " +
            "AND s.dtCriacao <= :dtFinal " +
            "ORDER BY s.id ASC")
    Page<Servico> searchAscend(Date dtInicial, Date dtFinal, Pageable pageable);

    @Query("SELECT s " +
            "FROM Servico s " +
            "WHERE s.dtCriacao >= :dtInicial " +
            "AND s.dtCriacao <= :dtFinal " +
            "ORDER BY s.id DESC")
    Page<Servico> searchDescend(Date dtInicial, Date dtFinal, Pageable pageable);
}
