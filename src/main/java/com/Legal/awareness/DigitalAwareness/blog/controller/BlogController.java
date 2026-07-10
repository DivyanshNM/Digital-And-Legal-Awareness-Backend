package com.Legal.awareness.DigitalAwareness.blog.controller;


import com.Legal.awareness.DigitalAwareness.blog.dto.BlogResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.CreateBlog;
import com.Legal.awareness.DigitalAwareness.blog.dto.DeleteResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.UpdateBlogRequest;
import com.Legal.awareness.DigitalAwareness.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/blogs")
@Slf4j
public class BlogController {


    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody CreateBlog createBlog) {
        log.info("Request to create blog: {}", createBlog);
        BlogResponse blogResponse = blogService.saveBlog(createBlog);

        log.info("blogResponse: {}", blogResponse);
        return new ResponseEntity<>(blogResponse, HttpStatus.CREATED);
    }
    @PatchMapping("/{blogId}/publish")
    public ResponseEntity<BlogResponse> publishBlog(@PathVariable Long blogId) {
        BlogResponse blogResponse = blogService.publishBlog(blogId);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<List<BlogResponse>> getMyBlogs() {
        List<BlogResponse> allBlog = blogService.getMyBlogs();
        return ResponseEntity.ok(allBlog);
    }

    @GetMapping
    public ResponseEntity<Page<BlogResponse>> getAllPublishedBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<BlogResponse> allPublishedBlog = blogService.getAllPublishedBlog(page, size);
        return ResponseEntity.ok(allPublishedBlog);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<BlogResponse> getBlogBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(blogService.getBlogBySlug(slug));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlogResponse>> searchBlogs(
            @RequestParam("keyword") String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ){
        Page<BlogResponse> blogResponses = blogService.searchBlog(keyword, page, size);
        return ResponseEntity.ok(blogResponses);
    }

    @GetMapping("/{id}/blogs")
    public ResponseEntity<List<BlogResponse>> getBlogByAuthor(@PathVariable Long id){
        return ResponseEntity.ok(blogService.getBlogByAuthor(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<BlogResponse>> getBlogByCategoryId(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(blogService.getBlogByCategoryId(categoryId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BlogResponse> editBlog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlogRequest updateBlogRequest) {
        BlogResponse blogResponse = blogService.editBlog(id, updateBlogRequest);
        return ResponseEntity.ok(blogResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteBlog(@PathVariable Long id) {
        DeleteResponse deleteResponse = blogService.deleteBlog(id);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }





}
