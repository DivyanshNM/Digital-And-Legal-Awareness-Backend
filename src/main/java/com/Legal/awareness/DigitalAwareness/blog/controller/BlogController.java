package com.Legal.awareness.DigitalAwareness.blog.controller;


import com.Legal.awareness.DigitalAwareness.blog.dto.BlogResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.CreateBlog;
import com.Legal.awareness.DigitalAwareness.blog.dto.DeleteResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.UpdateBlogRequest;
import com.Legal.awareness.DigitalAwareness.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Blog APIs",
        description = "APIs for creating, publishing, updating, searching, and managing blogs"
)
@RestController
@RequestMapping("api/v1/blogs")
@Slf4j
public class BlogController {


    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @Operation(
            summary = "Create Blog",
            description = "Creates a new blog in draft mode."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Blog created successfully",
            content = @Content(schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody CreateBlog createBlog) {
        log.info("Request to create blog: {}", createBlog);
        BlogResponse blogResponse = blogService.saveBlog(createBlog);

        log.info("blogResponse: {}", blogResponse);
        return new ResponseEntity<>(blogResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Publish Blog",
            description = "Publishes an existing draft blog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Blog published successfully",
            content = @Content(schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Blog not found")
    @PatchMapping("/{blogId}/publish")
    public ResponseEntity<BlogResponse> publishBlog(@PathVariable Long blogId) {
        BlogResponse blogResponse = blogService.publishBlog(blogId);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }


    @Operation(
            summary = "Get My Blogs",
            description = "Returns all blogs created by the authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Blogs fetched successfully")
    @GetMapping("/me")
    public ResponseEntity<List<BlogResponse>> getMyBlogs() {
        List<BlogResponse> allBlog = blogService.getMyBlogs();
        return ResponseEntity.ok(allBlog);
    }

    @Operation(
            summary = "Get Published Blogs",
            description = "Returns paginated published blogs."
    )
    @ApiResponse(responseCode = "200", description = "Published blogs fetched successfully")
    @GetMapping
    public ResponseEntity<Page<BlogResponse>> getAllPublishedBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> allPublishedBlog = blogService.getAllPublishedBlog(page, size);
        return ResponseEntity.ok(allPublishedBlog);
    }

    @Operation(
            summary = "Get Blog By Slug",
            description = "Returns a published blog using its unique slug."
    )
    @ApiResponse(responseCode = "200", description = "Blog fetched successfully")
    @ApiResponse(responseCode = "404", description = "Blog not found")
    @GetMapping("/{slug}")
    public ResponseEntity<BlogResponse> getBlogBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(blogService.getBlogBySlug(slug));
    }

    @Operation(
            summary = "Search Blogs",
            description = "Search published blogs by title or content."
    )
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<Page<BlogResponse>> searchBlogs(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<BlogResponse> blogResponses = blogService.searchBlog(keyword, page, size);
        return ResponseEntity.ok(blogResponses);
    }

    @Operation(
            summary = "Get Blogs By Author",
            description = "Returns all blogs written by a specific author."
    )
    @ApiResponse(responseCode = "200", description = "Blogs fetched successfully")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @GetMapping("/{id}/blogs")
    public ResponseEntity<List<BlogResponse>> getBlogByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogByAuthor(id));
    }

    @Operation(
            summary = "Get Blogs By Category",
            description = "Returns all blogs belonging to a category."
    )
    @ApiResponse(responseCode = "200", description = "Blogs fetched successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BlogResponse>> getBlogByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(blogService.getBlogByCategoryId(categoryId));
    }

    @Operation(
            summary = "Update Blog",
            description = "Updates an existing blog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Blog updated successfully",
            content = @Content(schema = @Schema(implementation = BlogResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Blog not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PatchMapping("/{id}")
    public ResponseEntity<BlogResponse> editBlog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlogRequest updateBlogRequest) {
        BlogResponse blogResponse = blogService.editBlog(id, updateBlogRequest);
        return ResponseEntity.ok(blogResponse);
    }


    @Operation(
            summary = "Delete Blog",
            description = "Soft deletes a blog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Blog deleted successfully",
            content = @Content(schema = @Schema(implementation = DeleteResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "Blog not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteBlog(@PathVariable Long id) {
        DeleteResponse deleteResponse = blogService.deleteBlog(id);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }


}
