package com.crazydream.domain.document.repository;

import com.crazydream.domain.document.model.aggregate.DocumentCategory;
import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;

import java.util.List;
import java.util.Optional;

/**
 * 资料分类仓储接口
 */
public interface DocumentCategoryRepository {
    
    /**
     * 保存分类
     */
    DocumentCategory save(DocumentCategory category);
    
    /**
     * 根据ID查找分类
     */
    Optional<DocumentCategory> findById(DocumentCategoryId id);
    
    /**
     * 查找所有启用的分类
     */
    List<DocumentCategory> findAllEnabled();
    
    /**
     * 查找所有分类
     */
    List<DocumentCategory> findAll();
}
