package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Tecnico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    Page<Tecnico> findByNomeLikeIgnoreCaseAndCpfLikeIgnoreCase(String nome, String cpf, Pageable pageable);
    Long countByNomeLikeIgnoreCaseAndCpfLikeIgnoreCase(String nome, String cpf);
    Long countByEmailIgnoreCase(String email);

}
