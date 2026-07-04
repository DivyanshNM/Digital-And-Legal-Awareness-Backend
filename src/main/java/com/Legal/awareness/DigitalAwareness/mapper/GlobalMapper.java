package com.Legal.awareness.DigitalAwareness.mapper;


import com.Legal.awareness.DigitalAwareness.auth.dto.LoginResponse;
import com.Legal.awareness.DigitalAwareness.auth.dto.UserResponse;
import com.Legal.awareness.DigitalAwareness.category.dto.CategoryResponse;
import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import com.Legal.awareness.DigitalAwareness.user.dto.PublicUserResponse;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .build();
    }

    public List<CategoryResponse> getAllCategories(List<Category> categories){
        // we'll get all categories from db in the form of Category, and we need to convert each category into categoryResponse

        List<CategoryResponse> convertIntoCategoryResponse = categories.stream()
                .map(category -> (
                        CategoryResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .description(category.getDescription())
                                .active(category.isActive())
                                .build()
                ))
                .collect(Collectors.toList());

        return convertIntoCategoryResponse;
    }
}
