package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE (c.nome) LIKE %:nome% AND (c.documento) LIKE %:documento% AND (c.isEnabled) = true")
    Page<Cliente> findByNomeAndDocumento(@Param("nome") String nome,@Param("documento") String documento, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Cliente c WHERE (c.nome) LIKE %:nome% AND (c.documento) LIKE %:documento% AND (c.isEnabled) = true")
    Long findByNomeAndDocumentoCount(@Param("nome") String nome, @Param("documento") String documento);

    Long countByEmail(String email);

}
