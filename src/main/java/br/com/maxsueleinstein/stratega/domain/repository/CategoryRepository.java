package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findByUserIdOrGlobal(UUID userId);
}
