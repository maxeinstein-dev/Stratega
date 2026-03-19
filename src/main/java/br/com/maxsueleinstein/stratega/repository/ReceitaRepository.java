package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}