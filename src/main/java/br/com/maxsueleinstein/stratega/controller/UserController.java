package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.dto.CreateUserDTO;
import br.com.maxsueleinstein.stratega.domain.dto.UserResponseDTO;
import br.com.maxsueleinstein.stratega.domain.dto.WalletResponseDTO;
import br.com.maxsueleinstein.stratega.service.UserService;
import br.com.maxsueleinstein.stratega.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final WalletService walletService;

    public UserController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUsers(@RequestBody @Valid CreateUserDTO userDTO) {
        UserResponseDTO saved = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO saved = userService.findUserById(id);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}/wallets")
    public ResponseEntity<List<WalletResponseDTO>> getWalletByUser(@PathVariable Long id) {
        if (!userService.existById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            List<WalletResponseDTO> wallets = walletService.findWalletsByUserId(id);
            return ResponseEntity.ok(wallets);
        }
    }
}
