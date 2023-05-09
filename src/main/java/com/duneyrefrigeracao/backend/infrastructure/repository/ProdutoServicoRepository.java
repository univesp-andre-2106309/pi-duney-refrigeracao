package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.ProdutoServico;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProdutoServicoRepository extends JpaRepository<ProdutoServico, Long> {

    List<ProdutoServico> findAllByServicoIdAndAndDeletedIsFalse(Long id);

    void deleteByIdIn(List<Integer> list);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoServico ps SET ps.deleted = true WHERE ps.servico.id = :servicoId AND ps.id NOT IN :listId")
    void deleteByServicoIdAndIdNotIn(@Param("servicoId") Long id, @Param("listId") Collection<Long> preserveIdsList);
}
