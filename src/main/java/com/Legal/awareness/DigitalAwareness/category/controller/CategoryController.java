package com.Legal.awareness.DigitalAwareness.category.controller;

import com.Legal.awareness.DigitalAwareness.category.dto.CategoryResponse;
import com.Legal.awareness.DigitalAwareness.category.dto.CreateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.dto.DeleteMessage;
import com.Legal.awareness.DigitalAwareness.category.dto.UpdateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(
            summary = "Create Category By Admin",
            description = "Create Category ex : Java , Spring , Spring Boot"
    )
    @ApiResponse(responseCode = "201" , description = "Category Created Successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    @ApiResponse(responseCode = "403" , description = "You are not Authorized to access it only ADMIN can access")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        CategoryResponse category = categoryService.createCategory(createCategoryRequest);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get All Categories",
            description = "Returns all active categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categories fetched successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(
            summary = "Get Category By Id",
            description = "Returns a category using its ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category fetched successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(
            summary = "Update Category",
            description = "Updates an existing category. Only ADMIN can perform this operation."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category updated successfully",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(responseCode = "403", description = "Only ADMIN can update categories")
    @ApiResponse(responseCode = "404", description = "Category not found")

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest createCategoryRequest
    ) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, createCategoryRequest);
        return ResponseEntity.ok(categoryResponse);
    }

    @Operation(
            summary = "Delete Category",
            description = "Soft deletes a category. Only ADMIN can perform this operation."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category deleted successfully",
            content = @Content(schema = @Schema(implementation = DeleteMessage.class))
    )
    @ApiResponse(responseCode = "403", description = "Only ADMIN can delete categories")
    @ApiResponse(responseCode = "404", description = "Category not found")

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteMessage> deleteCategoryById(@PathVariable Long id) {
        DeleteMessage deleteMessage = categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(deleteMessage, HttpStatus.OK);
    }

}
