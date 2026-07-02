package com.Legal.awareness.DigitalAwareness.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ChangePassword {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8)
    private String newPassword;
}
