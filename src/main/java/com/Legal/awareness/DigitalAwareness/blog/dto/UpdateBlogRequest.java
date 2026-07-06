package com.Legal.awareness.DigitalAwareness.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBlogRequest {

    @Size(max = 200)
    private String title;

    private String content;

    private Long categoryId;
}