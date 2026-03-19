package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}