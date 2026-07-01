package com.Legal.awareness.DigitalAwareness.auth.controller;

import com.Legal.awareness.DigitalAwareness.auth.dto.LoginDto;
import com.Legal.awareness.DigitalAwareness.auth.dto.LoginResponse;
import com.Legal.awareness.DigitalAwareness.auth.dto.RegisterUser;
import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUser registerUser) throws IOException {
        UserResponse user = authService.createUser(registerUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto loginDto) {

        log.info("Login Request  : {}", loginDto);

        LoginResponse login = authService.login(loginDto);

        log.info("Login Response  : {}", loginDto);

        return new ResponseEntity<>(login, HttpStatus.OK);
    }

}
