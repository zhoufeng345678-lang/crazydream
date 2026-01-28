package com.crazydream.domain.category.repository;

import com.crazydream.domain.category.model.aggregate.Category;
import com.crazydream.domain.shared.model.CategoryId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(CategoryId id);
    List<Category> findAll();
    List<Category> findEnabled();
    boolean delete(CategoryId id);
}
