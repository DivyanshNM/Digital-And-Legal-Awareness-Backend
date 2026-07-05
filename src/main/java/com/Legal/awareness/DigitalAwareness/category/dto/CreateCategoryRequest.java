package com.Legal.awareness.DigitalAwareness.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank
    private String categoryName;

    @Size(max = 200)
    private String categoryDescription;

}
