package com.Legal.awareness.DigitalAwareness.blog.service;


import com.Legal.awareness.DigitalAwareness.blog.dto.BlogResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.CreateBlog;
import com.Legal.awareness.DigitalAwareness.blog.dto.DeleteResponse;
import com.Legal.awareness.DigitalAwareness.blog.dto.UpdateBlogRequest;
import com.Legal.awareness.DigitalAwareness.blog.entity.Blog;
import com.Legal.awareness.DigitalAwareness.blog.entity.BlogStatus;
import com.Legal.awareness.DigitalAwareness.blog.repository.BlogRepository;
import com.Legal.awareness.DigitalAwareness.blog.slung.services.SlugService;
import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import com.Legal.awareness.DigitalAwareness.category.repository.CategoryRepository;
import com.Legal.awareness.DigitalAwareness.mapper.GlobalMapper;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BlogService {


    private final BlogRepository blogRepository;
    private final SlugService slugService;
    private final CategoryRepository categoryRepository;
    private final GlobalMapper globalMapper;
    private final BlogClassificationService blogClassificationService;

    public BlogService(
            BlogRepository blogRepository,
            SlugService slugService,
            CategoryRepository categoryRepository,
            GlobalMapper globalMapper,
            BlogClassificationService blogClassificationService
    ) {
        this.blogRepository = blogRepository;
        this.slugService = slugService;
        this.categoryRepository = categoryRepository;
        this.globalMapper = globalMapper;
        this.blogClassificationService=blogClassificationService;
    }

    @Transactional
    public BlogResponse saveBlog(CreateBlog createBlog) {
        boolean legal=blogClassificationService.classify(createBlog);
        if(!legal){
            throw new RuntimeException("Only legal blogs are allowed.");
        }
        String generatedSlugTitle = slugService.generateSlugTitle(createBlog.getTitle());
        User user=getCurrentUser();
        String finalSlug = user.getId() + generatedSlugTitle;

        Category category = categoryRepository.findByIdAndActiveTrue(createBlog.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        User author = getCurrentUser();

        Blog blog = Blog.builder()
                .title(createBlog.getTitle())
                .content(createBlog.getContent())
                .slug(finalSlug)
                .category(category)
                .author(author)
                .build();

        Blog save = blogRepository.save(blog);

        return globalMapper.toBlogResponse(save);
    }

    @Transactional
    public BlogResponse publishBlog(Long blogId) {
        User loggedInUser = getCurrentUser();

        Blog blog = blogRepository.findByIdAndActiveTrue(blogId)
                .orElseThrow(() -> new RuntimeException("Blog NOt Found"));

        if (!blog.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not allowed to publish this blog");
        }

        if (blog.getStatus() == BlogStatus.PUBLISHED) {
            throw new RuntimeException("Blog is already published");
        }

        if (blog.getContent().isBlank()) {
            throw new RuntimeException("Content cannot be empty");
        }

        blog.setStatus(BlogStatus.PUBLISHED);
        blog.setPublishedAt(LocalDateTime.now());
        return globalMapper.toBlogResponse(blog);
    }


    // loggedIn user Publish + draft
    public List<BlogResponse> getMyBlogs() {

        User loggedInUser = getCurrentUser();
        List<Blog> byAuthorIdAndActiveTrue = blogRepository.findByAuthorIdAndActiveTrue(loggedInUser.getId());

        return globalMapper.toBlogResponses(byAuthorIdAndActiveTrue);
    }

    // Get all  published blog
    public Page<BlogResponse> getAllPublishedBlog(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());

        Page<Blog> blogs = blogRepository.findByStatusAndActiveTrue(BlogStatus.PUBLISHED, pageable);
        return blogs.map(globalMapper::toBlogResponse);
    }

    //This is for reading
    @Transactional
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlugAndStatusAndActiveTrue(slug, BlogStatus.PUBLISHED)
                .orElseThrow(() -> new RuntimeException("Blog not found"));


        blog.setViewCount(blog.getViewCount() + 1);

        return globalMapper.toBlogResponse(blog);
    }

    @Transactional
    public BlogResponse editBlog(Long blogId, UpdateBlogRequest updateBlogRequest) {
        User currentUser = getCurrentUser();
        Blog byId = blogRepository.findByIdAndAuthorId(blogId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Blog NOT Found"));

        if (byId.getStatus() == BlogStatus.PUBLISHED) {
            throw new RuntimeException("Published blogs cannot be edited.");
        }

        if (updateBlogRequest.getTitle() != null) {
            byId.setTitle(updateBlogRequest.getTitle());
        }

        if (updateBlogRequest.getContent() != null) {
            byId.setContent(updateBlogRequest.getContent());
        }

        if (updateBlogRequest.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndActiveTrue(updateBlogRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            byId.setCategory(category);
        }

        return globalMapper.toBlogResponse(byId);
    }

    public Page<BlogResponse> searchBlog(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());

        Page<Blog> blogs = blogRepository.findByTitleContainingIgnoreCaseAndStatusAndActiveTrue(keyword, BlogStatus.PUBLISHED, pageable);
        return blogs.map(globalMapper::toBlogResponse);
    }

    public List<BlogResponse> getBlogByAuthor(Long authorId) {
        List<Blog> byAuthorIdAndActiveTrue = blogRepository.findByAuthorIdAndStatusAndActiveTrue(authorId, BlogStatus.PUBLISHED);

        return globalMapper.toBlogResponses(byAuthorIdAndActiveTrue);
    }

    public List<BlogResponse> getBlogByCategoryId(Long categoryId) {
        List<Blog> byCategoryId = blogRepository.findByCategoryIdAndStatusAndActiveTrue(categoryId, BlogStatus.PUBLISHED);

        return globalMapper.toBlogResponses(byCategoryId);
    }

    @Transactional
    public DeleteResponse deleteBlog(Long blogId) {
        Blog existingBlog = blogRepository.findByIdAndAuthorId(blogId, getCurrentUser().getId())
                .orElseThrow(() -> new RuntimeException("Blog NOT Found"));

        existingBlog.setActive(false);
        existingBlog.setStatus(BlogStatus.ARCHIVED);
        return DeleteResponse.builder()
                .message("Blog Deleted Successfully")
                .build();
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
