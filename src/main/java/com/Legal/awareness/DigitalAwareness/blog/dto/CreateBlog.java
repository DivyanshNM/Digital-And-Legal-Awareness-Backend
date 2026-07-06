package com.Legal.awareness.DigitalAwareness.blog.dto;

import com.Legal.awareness.DigitalAwareness.blog.entity.BlogStatus;
import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateBlog {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Content Should Be Not Blank")
    private String content;

    @NotNull(message = "Category is Required ")
    private Long categoryId;

}
