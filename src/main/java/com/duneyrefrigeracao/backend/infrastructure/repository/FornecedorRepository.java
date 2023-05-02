package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor,Long> {

    Page<Fornecedor> findByNomeLikeIgnoreCaseAndCnpjLikeIgnoreCase(String nome, String cnpj, Pageable pageable);

    Long countByNomeLikeIgnoreCaseAndCnpjLikeIgnoreCase(String nome, String cnpj);

    Long countByEmailIgnoreCase(String email);
}
