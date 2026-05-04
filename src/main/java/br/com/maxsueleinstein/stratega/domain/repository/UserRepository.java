package br.com.maxsueleinstein.stratega.domain.repository;

import br.com.maxsueleinstein.stratega.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
}
