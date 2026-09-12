package br.com.maxsueleinstein.stratega.presentation.controller;

import br.com.maxsueleinstein.stratega.application.dto.AuthenticateUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.RegisterUserRequest;
import br.com.maxsueleinstein.stratega.application.dto.TokenResponse;
import br.com.maxsueleinstein.stratega.application.dto.UserResponse;
import br.com.maxsueleinstein.stratega.application.usecase.AuthenticateUserUseCase;
import br.com.maxsueleinstein.stratega.application.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Users", description = "Account registration and login endpoints.")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, AuthenticateUserUseCase authenticateUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user account")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Create a demo reviewer account",
                            value = """
                                    {
                                      "name": "Demo Reviewer",
                                      "email": "demo.reviewer@example.com",
                                      "password": "DemoPass123!"
                                    }
                                    """
                    )
            )
    )
    public UserResponse register(@RequestBody RegisterUserRequest request) {
        return registerUserUseCase.execute(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Log in and receive a JWT")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Log in as the demo reviewer",
                            value = """
                                    {
                                      "email": "demo.reviewer@example.com",
                                      "password": "DemoPass123!"
                                    }
                                    """
                    )
            )
    )
    public TokenResponse login(@RequestBody AuthenticateUserRequest request) {
        return authenticateUserUseCase.execute(request);
    }
}
