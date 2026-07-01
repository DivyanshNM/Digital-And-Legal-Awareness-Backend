package com.Legal.awareness.DigitalAwareness.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    public String token;
    public String name;
    public String email;
    public String bio;
}
