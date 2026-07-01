package com.Legal.awareness.DigitalAwareness.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    @Email(message = "Invalid Email")
    public String email;

    @NotBlank
    @Size(min = 8, message = "Password must contain at least 8 characters")
    public String password;

}
