package com.Legal.awareness.DigitalAwareness.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @NotBlank
    private String name;

    @Size(max = 500)
    private String bio;
}