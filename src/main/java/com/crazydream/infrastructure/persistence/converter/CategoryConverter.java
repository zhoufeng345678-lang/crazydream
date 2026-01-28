package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.category.model.aggregate.Category;
import com.crazydream.domain.category.model.valueobject.CategoryName;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.infrastructure.persistence.po.CategoryPO;

public class CategoryConverter {
    
    public static Category toDomain(CategoryPO po) {
        if (po == null) return null;
        
        return Category.rebuild(
            CategoryId.of(po.getId()),
            CategoryName.of(po.getName()),
            po.getIcon(),
            po.getColor(),
            po.getSort(),
            po.getStatus() != null && po.getStatus() == 1
        );
    }
    
    public static CategoryPO toPO(Category category) {
        if (category == null) return null;
        
        CategoryPO po = new CategoryPO();
        if (category.getId() != null) {
            po.setId(category.getId().getValue());
        }
        po.setName(category.getName().getValue());
        po.setIcon(category.getIcon());
        po.setColor(category.getColor());
        po.setSort(category.getSort());
        po.setStatus(category.isEnabled() ? 1 : 0);
        return po;
    }
}
