package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.FornecedorServico;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FornecedorServicoRepository extends JpaRepository<FornecedorServico, Long> {

    List<FornecedorServico> findAllByServicoIdAndAndDeletedIsFalse(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE FornecedorServico ps SET ps.deleted = true WHERE ps.servico.id = :servicoId AND ps.id NOT IN :listId")
    void deleteByServicoIdAndIdNotIn(@Param("servicoId") Long id,@Param("listId") Collection<Long> preserveIdsList);
}
