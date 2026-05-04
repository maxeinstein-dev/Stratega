package br.com.maxsueleinstein.stratega.infrastructure.persistence.entity;

import br.com.maxsueleinstein.stratega.domain.model.CategoryType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    private UUID id;
    private String name;
    
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    
    private UUID userId;
}
