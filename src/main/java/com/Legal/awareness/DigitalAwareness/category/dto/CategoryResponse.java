package com.Legal.awareness.DigitalAwareness.category.dto;


import lombok.*;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private boolean active;
}