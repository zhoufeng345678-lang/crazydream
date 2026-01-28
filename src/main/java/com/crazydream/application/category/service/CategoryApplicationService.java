package com.crazydream.application.category.service;

import com.crazydream.application.category.assembler.CategoryAssembler;
import com.crazydream.application.category.dto.*;
import com.crazydream.domain.category.model.aggregate.Category;
import com.crazydream.domain.category.repository.CategoryRepository;
import com.crazydream.domain.shared.model.CategoryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryApplicationService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional
    public CategoryDTO createCategory(CreateCategoryCommand command) {
        Category category = CategoryAssembler.toDomain(command);
        category = categoryRepository.save(category);
        return CategoryAssembler.toDTO(category);
    }
    
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryAssembler.toDTOList(categories);
    }
    
    public List<CategoryDTO> getEnabledCategories() {
        List<Category> categories = categoryRepository.findEnabled();
        return CategoryAssembler.toDTOList(categories);
    }
    
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(CategoryId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("分类不存在"));
        return CategoryAssembler.toDTO(category);
    }
    
    @Transactional
    public boolean deleteCategory(Long id) {
        return categoryRepository.delete(CategoryId.of(id));
    }
}
