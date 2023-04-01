package com.duneyrefrigeracao.backend.infrastructure.repository;

import com.duneyrefrigeracao.backend.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
