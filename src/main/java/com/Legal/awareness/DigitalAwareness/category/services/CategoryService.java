package com.Legal.awareness.DigitalAwareness.category.services;

import com.Legal.awareness.DigitalAwareness.category.dto.CategoryResponse;
import com.Legal.awareness.DigitalAwareness.category.dto.CreateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.dto.DeleteMessage;
import com.Legal.awareness.DigitalAwareness.category.dto.UpdateCategoryRequest;
import com.Legal.awareness.DigitalAwareness.category.entity.Category;
import com.Legal.awareness.DigitalAwareness.category.repository.CategoryRepository;
import com.Legal.awareness.DigitalAwareness.mapper.GlobalMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final GlobalMapper globalMapper;


    public CategoryService(CategoryRepository categoryRepository
            , GlobalMapper globalMapper) {
        this.categoryRepository = categoryRepository;
        this.globalMapper = globalMapper;
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        if (categoryRepository.existsByNameIgnoreCaseAndActiveTrue(
                createCategoryRequest.getCategoryName())) {

            throw new RuntimeException("Category already exists");
        }

        Category inActiveCategory = categoryRepository.findByNameIgnoreCaseAndActiveFalse(
                createCategoryRequest.getCategoryName()
        );

        if (inActiveCategory != null) {
            inActiveCategory.setActive(true);
            inActiveCategory.setDescription(createCategoryRequest.getCategoryDescription());

            return globalMapper.toCategoryResponse(inActiveCategory);
        }


        Category category = Category.builder()
                .name(createCategoryRequest.getCategoryName())
                .description(createCategoryRequest.getCategoryDescription())
                .active(true)
                .build();
        log.info("Creating new category {} , Active : {}", category.getName(), category.isActive());
        Category save = categoryRepository.save(category);
        return globalMapper.toCategoryResponse(save);
    }

    public List<CategoryResponse> getCategories() {
        List<Category> all = categoryRepository.findAllByActiveTrue();
        return globalMapper.getAllCategories(all);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        return globalMapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest updateCategoryRequest) {

        Category existing = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));


        if (updateCategoryRequest.getName() != null && !updateCategoryRequest.getName().isEmpty()) {
            if (categoryRepository.existsByNameIgnoreCaseAndActiveTrue(
                    updateCategoryRequest.getName())) {

                throw new RuntimeException("Category name already exists");
            }

            existing.setName(updateCategoryRequest.getName());
        }


        if (updateCategoryRequest.getDescription() != null && !updateCategoryRequest.getDescription().isEmpty()) {
            existing.setDescription(updateCategoryRequest.getDescription());
        }


        Category save = categoryRepository.save(existing);
        return globalMapper.toCategoryResponse(save);
    }

    @Transactional
    public DeleteMessage deleteCategoryById(Long id) {
        Category category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        category.setActive(false);
        categoryRepository.save(category);

        return DeleteMessage.builder()
                .message("Category deactivated successfully !!!! ")
                .build();
    }

}
