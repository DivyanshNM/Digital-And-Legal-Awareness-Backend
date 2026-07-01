package com.Legal.awareness.DigitalAwareness.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUser {

    @NotBlank(message = "Name is Required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @Size(max = 1000)
    private String bio;


    // We will not take profile photo at the time of Registration
    // it will go in user so that user can change and modity at any time
//    private MultipartFile profileImage;
}
