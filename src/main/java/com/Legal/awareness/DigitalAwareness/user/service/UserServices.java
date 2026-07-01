package com.Legal.awareness.DigitalAwareness.user.service;



import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.mapper.GlobalMapper;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import com.Legal.awareness.DigitalAwareness.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final GlobalMapper globalMapper;

    public UserServices(
            UserRepository userRepository,
            GlobalMapper globalMapper

    ) {
        this.userRepository = userRepository;
        this.globalMapper = globalMapper;
    }

    @Transactional
    public UserResponse uploadProfileImage(MultipartFile profileImage) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));

        String imageUrl = makUrlOfImage(profileImage);

        loggedInUser.setProfileImage(imageUrl);
        userRepository.save(loggedInUser);

        return globalMapper.toUserResponse(loggedInUser);
    }


    public String makUrlOfImage(MultipartFile imageFile) throws IOException {

        String imageUrl = "";
        if (imageFile != null && !imageFile.isEmpty()) {

            Path uploadPath = Paths.get("uploads/profiles");

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String originalFilename = imageFile.getOriginalFilename();
            assert originalFilename != null;
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String uniqueFileName = "img_" + UUID.randomUUID() + extension;


            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            imageUrl = "/uploads/profile/" + uniqueFileName;
        }

        return imageUrl;
    }


}
