package br.com.maxsueleinstein.stratega.infrastructure.persistence.repository;

import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataExchangeRateRepository extends JpaRepository<ExchangeRateEntity, String> {
}
