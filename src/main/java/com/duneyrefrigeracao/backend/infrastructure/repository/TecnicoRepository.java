package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Tecnico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    @Query("SELECT t FROM Tecnico t WHERE t.nome LIKE %:nome% AND t.cpf LIKE %:cpf% AND t.isActive = TRUE")
    Page<Tecnico> findByParams(String nome, String cpf, Pageable pageable);
    @Query("SELECT COUNT(t.id) FROM Tecnico t WHERE t.nome LIKE %:nome% AND t.cpf LIKE %:cpf% AND t.isActive = TRUE")
    Long countByParams(String nome, String cpf);
    Long countByEmailIgnoreCase(String email);

}
