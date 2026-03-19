package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}