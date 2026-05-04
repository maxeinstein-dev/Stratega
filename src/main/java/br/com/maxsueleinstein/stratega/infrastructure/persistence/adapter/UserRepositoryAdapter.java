package br.com.maxsueleinstein.stratega.infrastructure.persistence.adapter;

import br.com.maxsueleinstein.stratega.domain.model.User;
import br.com.maxsueleinstein.stratega.domain.repository.UserRepository;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.UserEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.mapper.UserMapper;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = repository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDomain);
    }
}
