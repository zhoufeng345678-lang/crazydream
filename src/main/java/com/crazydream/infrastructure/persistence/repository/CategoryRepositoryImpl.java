package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.category.model.aggregate.Category;
import com.crazydream.domain.category.repository.CategoryRepository;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.infrastructure.persistence.converter.CategoryConverter;
import com.crazydream.infrastructure.persistence.mapper.CategoryPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.CategoryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    
    @Autowired
    private CategoryPersistenceMapper mapper;
    
    @Override
    public Category save(Category category) {
        CategoryPO po = CategoryConverter.toPO(category);
        if (category.getId() == null) {
            mapper.insert(po);
            category.setId(CategoryId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return category;
    }
    
    @Override
    public Optional<Category> findById(CategoryId id) {
        CategoryPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(CategoryConverter.toDomain(po));
    }
    
    @Override
    public List<Category> findAll() {
        return mapper.selectAll().stream()
                .map(CategoryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Category> findEnabled() {
        return mapper.selectByStatus(1).stream()
                .map(CategoryConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(CategoryId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
}
