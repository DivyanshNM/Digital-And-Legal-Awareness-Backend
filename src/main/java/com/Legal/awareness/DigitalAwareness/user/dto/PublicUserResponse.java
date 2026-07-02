package com.Legal.awareness.DigitalAwareness.user.dto;

import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.user.entity.Blog;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PublicUserResponse {

    private String username;
    private String name;
    private String bio;
    private String profilePicture;
    private List<Blog> blogs;
}
