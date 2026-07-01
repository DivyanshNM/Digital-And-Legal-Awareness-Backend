package com.Legal.awareness.DigitalAwareness.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    public String name;
    public String email;
    public String profileImage;
    public String bio;
    public LocalDateTime created_At;
}
