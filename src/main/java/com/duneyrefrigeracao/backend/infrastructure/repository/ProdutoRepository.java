package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.preco >= :precoMin AND p.preco <= :precoMax and p.nome like %:nome% AND p.isActive = true ORDER BY p.id ASC ")
    Page<Produto> findByPriceBetweenAscend(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax, @Param("nome") String nome, Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.preco >= :precoMin AND p.preco <= :precoMax and p.nome like %:nome% AND p.isActive = true ORDER BY p.id DESC")
    Page<Produto> findByPriceBetweenDescend(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax, @Param("nome") String nome, Pageable pageable);

    @Query("SELECT COUNT(p.id) FROM Produto p WHERE p.preco >= :precoMin AND p.preco <= :precoMax and p.nome like %:nome% AND p.isActive = true")
    Long findAndCountByPriceBetween(@Param("precoMin") BigDecimal precoMin,@Param("precoMax") BigDecimal precoMax,@Param("nome") String nome);
    Long countByNomeLikeIgnoreCase(String nome);
}
