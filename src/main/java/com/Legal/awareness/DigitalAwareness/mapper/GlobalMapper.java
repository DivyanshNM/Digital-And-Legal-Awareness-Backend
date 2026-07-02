package com.Legal.awareness.DigitalAwareness.mapper;


import com.Legal.awareness.DigitalAwareness.auth.dto.LoginResponse;
import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.user.dto.PublicUserResponse;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GlobalMapper {


    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .username(user.displayUsername())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .created_At(user.getCreatedAt())
                .profileImage(user.getProfileImage())
                .build();
    }

    public LoginResponse toLoginResponse(User user , String token){
        return LoginResponse.builder()
                .token(token)
                .username(user.displayUsername())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .build();
    }

    public PublicUserResponse  toPublicUserResponse(User user){
        return PublicUserResponse.builder()
                .username(user.displayUsername())
                .name(user.getName())
                .profilePicture(user.getProfileImage())
                .blogs(user.getBlogs())
                .bio(user.getBio())
                .build();
    }

}
