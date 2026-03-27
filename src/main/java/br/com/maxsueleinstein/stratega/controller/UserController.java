package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.dto.CreateUserDTO;
import br.com.maxsueleinstein.stratega.domain.dto.UserResponseDTO;
import br.com.maxsueleinstein.stratega.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUsers(@RequestBody @Valid CreateUserDTO userDTO) {
        UserResponseDTO saved = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }
}
