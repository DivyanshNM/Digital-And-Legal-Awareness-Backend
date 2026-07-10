package com.Legal.awareness.DigitalAwareness.blog.repository;

import com.Legal.awareness.DigitalAwareness.blog.entity.Blog;
import com.Legal.awareness.DigitalAwareness.blog.entity.BlogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByIdAndActiveTrue(Long id);

    boolean existsBySlug(String slug);

    Page<Blog> findByStatusAndActiveTrue(BlogStatus blogStatus, Pageable pageable);

    List<Blog> findByAuthorIdAndActiveTrue(Long authorId );

    Optional<Blog> findBySlugAndStatusAndActiveTrue(String slug, BlogStatus status);

    Optional<Blog> findByIdAndAuthorId(Long blogId, Long authorId);

    List<Blog> findByAuthorIdAndStatusAndActiveTrue(Long authorId, BlogStatus blogStatus);

   List<Blog> findByCategoryIdAndStatusAndActiveTrue(Long categoryId, BlogStatus blogStatus);

    Page<Blog> findByTitleContainingIgnoreCaseAndStatusAndActiveTrue(String keyword, BlogStatus status, Pageable pageable);

}
