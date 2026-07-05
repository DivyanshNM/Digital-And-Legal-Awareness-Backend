package com.Legal.awareness.DigitalAwareness.category.repository;

import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndActiveTrue(Long id);

    List<Category> findAllByActiveTrue();

    boolean existsByNameIgnoreCaseAndActiveTrue(@NotBlank String categoryName);

    Category findByNameIgnoreCaseAndActiveFalse(String name);

}
