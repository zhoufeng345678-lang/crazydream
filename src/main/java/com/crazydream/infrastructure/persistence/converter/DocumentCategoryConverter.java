package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.document.model.aggregate.DocumentCategory;
import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;
import com.crazydream.infrastructure.persistence.po.DocumentCategoryPO;

/**
 * 资料分类转换器
 */
public class DocumentCategoryConverter {
    
    public static DocumentCategory toDomain(DocumentCategoryPO po) {
        if (po == null) {
            return null;
        }
        
        return DocumentCategory.rebuild(
            DocumentCategoryId.of(po.getId()),
            po.getName(),
            po.getDescription(),
            po.getIcon(),
            po.getColor(),
            po.getSortOrder(),
            po.getStatus(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    public static DocumentCategoryPO toPO(DocumentCategory category) {
        if (category == null) {
            return null;
        }
        
        DocumentCategoryPO po = new DocumentCategoryPO();
        if (category.getId() != null) {
            po.setId(category.getId().getValue());
        }
        po.setName(category.getName());
        po.setDescription(category.getDescription());
        po.setIcon(category.getIcon());
        po.setColor(category.getColor());
        po.setSortOrder(category.getSortOrder());
        po.setStatus(category.getStatus());
        po.setCreateTime(category.getCreateTime());
        po.setUpdateTime(category.getUpdateTime());
        return po;
    }
}
