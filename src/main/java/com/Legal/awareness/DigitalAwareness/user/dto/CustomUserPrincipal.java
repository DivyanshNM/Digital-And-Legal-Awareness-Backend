package com.Legal.awareness.DigitalAwareness.user.dto;

import com.Legal.awareness.DigitalAwareness.auth.utilites.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomUserPrincipal {

    private Long userId;
    private String email;
    private Role role;
}
