package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.dto.CreateUserDTO;
import br.com.maxsueleinstein.stratega.domain.dto.UserResponseDTO;
import br.com.maxsueleinstein.stratega.domain.entity.User;
import br.com.maxsueleinstein.stratega.mapper.UserMapper;
import br.com.maxsueleinstein.stratega.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(CreateUserDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = UserMapper.toEntity(dto, encodedPassword);

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }

    public UserResponseDTO findUserById(Long id) {
        User saved = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toResponse(saved);
    }

    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }
}
