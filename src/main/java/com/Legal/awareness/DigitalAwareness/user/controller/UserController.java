package com.Legal.awareness.DigitalAwareness.user.controller;


import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.ChangePassword;
import com.Legal.awareness.DigitalAwareness.user.dto.PublicUserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.UpdateProfileRequest;
import com.Legal.awareness.DigitalAwareness.user.service.UserServices;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(){
        return ResponseEntity.ok(userServices.getCurrentUser());
    }

    @GetMapping("/public/{username}")
    public ResponseEntity<PublicUserResponse> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userServices.getUserByUsername(username));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile( @Valid @RequestBody UpdateProfileRequest updateProfileRequest){
        return  ResponseEntity.ok(userServices.updateProfile(updateProfileRequest));
    }

    @PostMapping(
            value ="/upload-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @RequestParam("image") MultipartFile profilePicture
    )throws IOException {

        log.info("Uploading profile picture {} " , profilePicture.getOriginalFilename());

        return ResponseEntity.ok(userServices.uploadProfileImage(profilePicture));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePassword changePassword){
        userServices.changePassword(changePassword);
        return ResponseEntity.ok("Change password");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(){
        userServices.deleteAccount();
        return ResponseEntity.ok("Account deleted");
    }

}
