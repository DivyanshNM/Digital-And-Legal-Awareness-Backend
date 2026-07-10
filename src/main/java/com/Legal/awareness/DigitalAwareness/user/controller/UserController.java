package com.Legal.awareness.DigitalAwareness.user.controller;


import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.ChangePassword;
import com.Legal.awareness.DigitalAwareness.user.dto.PublicUserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.UpdateProfileRequest;
import com.Legal.awareness.DigitalAwareness.user.service.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Tag(name = "User Api", description = "User Related Operation")
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @Operation(
            summary = "API Health Check",
            description = "To check this service is operational "
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy and fully operational")
    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }


    @Operation(
            summary = "Get Current Authenticated User",
            description = "To check which user is currently loggedIn User"
    )
    @ApiResponse(responseCode = "200",
            description = "Successfully retrieved profile details.",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Missing, invalid, or expired security token.",
            content = @Content)
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userServices.getCurrentUser());
    }


    @Operation(
            summary = "Get Public User Profile",
            description = "Get any User Profile By Their unique username "
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved public profile.",
            content = @Content(schema = @Schema(implementation = PublicUserResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not Found - username not found .", content = @Content)
    @GetMapping("/public/{username}")
    public ResponseEntity<PublicUserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userServices.getUserByUsername(username));
    }

    @Operation(
            summary = "Update User Profile Details",
            description = "Modifies user profile details such as display name or bio."
    )
    @ApiResponse(responseCode = "200", description = "Profile details updated successfully.",
            content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request - Request body contains invalid or missing validation.", content = @Content)
    @ApiResponse(responseCode = "401", description = "Unauthorized - User Has not loggedIn.", content = @Content)
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(userServices.updateProfile(updateProfileRequest));
    }


    @Operation(
            summary = "Upload Profile Picture",
            description = "Accepts binary file multipart uploads to update the current user's avatar image."
    )
    @ApiResponse(responseCode = "200", description = "Profile image uploaded successfully ")
    @ApiResponse(responseCode = "400", description = "Bad Request - File is missing or invalid formate")
    @ApiResponse(responseCode = "401", description = "Unauthorized - User credential verification failed")
    @PostMapping(
            value = "/upload-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponse> uploadProfilePicture(
            @RequestParam("image") MultipartFile profilePicture
    ) throws IOException {

        log.info("Uploading profile picture {} ", profilePicture.getOriginalFilename());

        return ResponseEntity.ok(userServices.uploadProfileImage(profilePicture));
    }

    @Operation(
            summary = "Change User Password",
            description = "Validates the user's existing credentials and updates to a new password."
    )
    @ApiResponse(responseCode = "200", description = "Password changed successfully.")
    @ApiResponse(responseCode = "400", description = "Bad Request - Password must contains 8 length .", content = @Content)
    @ApiResponse(responseCode = "401", description = "Unauthorized - User credential verification failed.", content = @Content)
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        userServices.changePassword(changePassword);
        return ResponseEntity.ok("Change password");
    }

    @Operation(
            summary = "Permanently Delete Account",
            description = "Delete of the authenticated user profile."
    )
    @ApiResponse(responseCode = "200", description = "User Successfully deleted for database")
    @ApiResponse(responseCode = "401", description = "Unauthorized - User credentials missing or expired.", content = @Content)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount() {
        userServices.deleteAccount();
        return ResponseEntity.ok("Account deleted");
    }

}
