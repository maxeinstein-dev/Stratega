package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
