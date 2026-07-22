package com.Legal.awareness.DigitalAwareness.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationRequest {
    private String title;
    private String content;
}
