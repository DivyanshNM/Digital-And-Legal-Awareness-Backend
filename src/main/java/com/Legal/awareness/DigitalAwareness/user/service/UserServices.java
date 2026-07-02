package com.Legal.awareness.DigitalAwareness.user.service;


import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.mapper.GlobalMapper;
import com.Legal.awareness.DigitalAwareness.user.dto.ChangePassword;
import com.Legal.awareness.DigitalAwareness.user.dto.PublicUserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.UpdateProfileRequest;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import com.Legal.awareness.DigitalAwareness.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/***
 * @Author Ankit Vishwakarma
 */


@Service
public class UserServices {

    private final UserRepository userRepository;
    private final GlobalMapper globalMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServices(
            UserRepository userRepository,
            GlobalMapper globalMapper,

            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.globalMapper = globalMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse uploadProfileImage(MultipartFile profileImage) throws IOException {

        User loggedInUser = getLoggedInUser();

        String imageUrl = makUrlOfImage(profileImage);

        loggedInUser.setProfileImage(imageUrl);
        userRepository.save(loggedInUser);

        return globalMapper.toUserResponse(loggedInUser);
    }


    public String makUrlOfImage(MultipartFile imageFile) throws IOException {

        String imageUrl = "";
        if (imageFile != null && !imageFile.isEmpty()) {

            Path uploadPath = Paths.get("uploads/profiles");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            assert originalFilename != null;
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String uniqueFileName = "img_" + UUID.randomUUID() + extension;


            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            imageUrl = "/uploads/profiles/" + uniqueFileName;
        }

        return imageUrl;
    }



    // Get Current User

    public UserResponse getCurrentUser() {
        return globalMapper.toUserResponse(getLoggedInUser());
    }

    // Get User By Username

    public PublicUserResponse getUserByUsername(String username) {

        User byUsername = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return globalMapper.toPublicUserResponse(byUsername);
    }

    // Update User Profile ex : name , bio
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest updateProfileRequest) {

        User loggedInUser = getLoggedInUser();

        if (loggedInUser.getName() != null && !loggedInUser.getName().isEmpty()) {
            loggedInUser.setName(updateProfileRequest.getName());

        }
        if (loggedInUser.getBio() != null && !loggedInUser.getBio().isEmpty()) {

            loggedInUser.setBio(updateProfileRequest.getBio());
        }

        userRepository.save(loggedInUser);

        return globalMapper.toUserResponse(loggedInUser);
    }


    public void changePassword(ChangePassword changePassword) {

        User loggedInUser = getLoggedInUser();

        if(!passwordEncoder.matches(changePassword.getOldPassword(), loggedInUser.getPassword())){
            throw new IllegalArgumentException("Old passwords don't match");
        }

        loggedInUser.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));

        userRepository.save(loggedInUser);
    }


    @Transactional
    public void deleteAccount(){
        User loggedInUser = getLoggedInUser();

        loggedInUser.setDeleted(true);
        loggedInUser.setDeletedAt(LocalDateTime.now());

        userRepository.save(loggedInUser);
    }


    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        return (User) authentication.getPrincipal();
    }



}
