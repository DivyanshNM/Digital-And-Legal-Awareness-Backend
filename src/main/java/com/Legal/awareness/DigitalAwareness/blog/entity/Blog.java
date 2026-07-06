package com.Legal.awareness.DigitalAwareness.blog.entity;

import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import com.Legal.awareness.DigitalAwareness.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BlogStatus status = BlogStatus.DRAFT;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private boolean active = true;

    private LocalDateTime publishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
