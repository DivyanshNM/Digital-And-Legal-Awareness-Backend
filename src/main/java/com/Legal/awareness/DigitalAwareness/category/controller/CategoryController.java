package com.Legal.awareness.DigitalAwareness.category.controller;

import com.Legal.awareness.DigitalAwareness.category.dto.CategoryResponse;
import com.Legal.awareness.DigitalAwareness.category.dto.CreateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.dto.DeleteMessage;
import com.Legal.awareness.DigitalAwareness.category.dto.UpdateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories APIs" , description = "Categories related Apis to manage category By ADMIN")
@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        CategoryResponse category = categoryService.createCategory(createCategoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest createCategoryRequest
    ) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, createCategoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteMessage> deleteCategoryById(@PathVariable Long id) {
        DeleteMessage deleteMessage = categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(deleteMessage, HttpStatus.OK);
    }

}
