package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FornecedorRepository extends JpaRepository<Fornecedor,Long> {

    @Query("SELECT f FROM Fornecedor f WHERE f.nome LIKE %:nome% AND f.cnpj LIKE %:cnpj% AND f.isActive = TRUE")
    Page<Fornecedor> findByParams(String nome, String cnpj, Pageable pageable);
    @Query("SELECT COUNT(f.id) FROM Fornecedor f WHERE f.nome LIKE %:nome% AND f.cnpj LIKE %:cnpj% AND f.isActive = TRUE")
    Long countByParams(String nome, String cnpj);
    Long countByEmailIgnoreCase(String email);
}
