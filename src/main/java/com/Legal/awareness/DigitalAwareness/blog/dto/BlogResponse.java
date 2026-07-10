package com.Legal.awareness.DigitalAwareness.blog.dto;


import com.Legal.awareness.DigitalAwareness.blog.entity.BlogStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlogResponse {

    private Long id;

    private String title;

    private String content;

    private String category;

    private String author;

    private BlogStatus status;

    private Long views;

    private LocalDateTime publishedAt;
}
