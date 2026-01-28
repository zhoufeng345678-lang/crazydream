package com.crazydream.application.category.assembler;

import com.crazydream.application.category.dto.*;
import com.crazydream.domain.category.model.aggregate.Category;
import com.crazydream.domain.category.model.valueobject.CategoryName;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryAssembler {
    
    public static Category toDomain(CreateCategoryCommand command) {
        return Category.create(
            CategoryName.of(command.getName()),
            command.getIcon(),
            command.getColor(),
            command.getSort()
        );
    }
    
    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        
        CategoryDTO dto = new CategoryDTO();
        if (category.getId() != null) {
            dto.setId(category.getId().getValue());
        }
        dto.setName(category.getName().getValue());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setSort(category.getSort());
        dto.setEnabled(category.isEnabled());
        return dto;
    }
    
    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(CategoryAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
