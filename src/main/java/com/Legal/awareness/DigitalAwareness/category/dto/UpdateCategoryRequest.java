package com.Legal.awareness.DigitalAwareness.category.dto;

import lombok.Data;

@Data
public class UpdateCategoryRequest {

    private String name;

    private String description;

    private Boolean active;

}
