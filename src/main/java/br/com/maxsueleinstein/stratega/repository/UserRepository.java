package br.com.maxsueleinstein.stratega.repository;

import br.com.maxsueleinstein.stratega.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
