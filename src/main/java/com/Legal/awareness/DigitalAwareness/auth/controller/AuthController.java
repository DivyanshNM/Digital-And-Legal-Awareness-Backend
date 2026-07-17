package com.Legal.awareness.DigitalAwareness.auth.controller;

import com.Legal.awareness.DigitalAwareness.auth.dto.LoginDto;
import com.Legal.awareness.DigitalAwareness.auth.dto.LoginResponse;
import com.Legal.awareness.DigitalAwareness.auth.dto.RegisterUser;
import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;



@Tag(name = "Auth APIs" , description = "Authentication operations")
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(
            summary = "Register User",
            description = "Register a new User"
    )
    @ApiResponse(responseCode = "201", description = "User Registered Successful")
    @ApiResponse(responseCode = "400" , description = "Invalid Input")
    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUser registerUser) throws IOException {
        UserResponse user = authService.createUser(registerUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token"
    )
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto loginDto) {

        log.info("Login Request  : {}", loginDto);

        LoginResponse login = authService.login(loginDto);

        log.info("Login Response  : {}", loginDto);

        return new ResponseEntity<>(login, HttpStatus.OK);
    }

}
