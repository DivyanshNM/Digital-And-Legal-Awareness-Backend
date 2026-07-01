package com.Legal.awareness.DigitalAwareness.auth.service;

import com.Legal.awareness.DigitalAwareness.auth.dto.LoginDto;
import com.Legal.awareness.DigitalAwareness.auth.dto.LoginResponse;
import com.Legal.awareness.DigitalAwareness.auth.dto.RegisterUser;
import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.auth.utilites.Role;
import com.Legal.awareness.DigitalAwareness.mapper.GlobalMapper;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import com.Legal.awareness.DigitalAwareness.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authentication;
    private final GlobalMapper globalMapper;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository ,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authentication,
                       GlobalMapper globalMapper,
                       JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authentication = authentication;
        this.globalMapper = globalMapper;
        this.jwtService = jwtService;
    }

    public UserResponse createUser(RegisterUser registerUser) throws IOException {
        Optional<User> byEmail = userRepository.findByEmail(registerUser.getEmail());

        if(byEmail.isEmpty()){
            User user = User.builder()
                    .name(registerUser.getName())
                    .email(registerUser.getEmail())
                    .password(passwordEncoder.encode(registerUser.getPassword()))
                    .bio(registerUser.getBio())
                    .role(Role.USER)
                    .blogs(new ArrayList<>())
                    .isVerified(true)
            .build();

            User save = userRepository.save(user);
            return globalMapper.toUserResponse(save);
        }

        return globalMapper.toUserResponse(byEmail.get());
    }

    public LoginResponse login(LoginDto loginDto){

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


//        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid email or password");
//        }

        log.info("Login Request Service   : {}", loginDto);
        String token = jwtService.generateToken(user);

        log.info("Authentication Success {}" , token);

        return globalMapper.toLoginResponse(user,token);
    }


}
