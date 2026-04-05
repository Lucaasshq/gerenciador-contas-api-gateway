package com.lucas.ms_conta.repository;

import com.lucas.ms_conta.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
