package com.Legal.awareness.DigitalAwareness.security.service;


import com.Legal.awareness.DigitalAwareness.user.entity.User;
import com.Legal.awareness.DigitalAwareness.user.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User byEmail = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(byEmail.getPassword())
                .build();
    }
    // custom
}
