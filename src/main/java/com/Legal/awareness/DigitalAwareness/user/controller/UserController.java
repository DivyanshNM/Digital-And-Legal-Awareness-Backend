package com.Legal.awareness.DigitalAwareness.user.controller;


import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.user.service.UserServices;
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




}
