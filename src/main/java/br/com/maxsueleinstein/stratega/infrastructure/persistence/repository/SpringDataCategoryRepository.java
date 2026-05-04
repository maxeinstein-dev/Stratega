package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    @Query("SELECT c FROM CategoryEntity c WHERE c.userId = :userId OR c.userId IS NULL")
    List<CategoryEntity> findByUserIdOrGlobal(@Param("userId") UUID userId);
}
