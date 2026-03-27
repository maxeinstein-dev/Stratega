package br.com.maxsueleinstein.stratega.mapper;

import br.com.maxsueleinstein.stratega.domain.dto.CreateUserDTO;
import br.com.maxsueleinstein.stratega.domain.dto.UserResponseDTO;
import br.com.maxsueleinstein.stratega.domain.entity.User;

public class UserMapper {

    public static User toEntity(CreateUserDTO dto, String encodedPassword) {
        return new User(
                dto.name(), dto.email(), encodedPassword
        );
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(), user.getName(), user.getEmail()
        );
    }
}
