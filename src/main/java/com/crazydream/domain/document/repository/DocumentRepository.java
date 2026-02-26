package com.crazydream.domain.document.repository;

import com.crazydream.domain.document.model.aggregate.Document;
import com.crazydream.domain.document.model.valueobject.DocumentCategoryId;
import com.crazydream.domain.document.model.valueobject.DocumentId;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * 文档仓储接口
 */
public interface DocumentRepository {
    
    /**
     * 保存文档
     */
    Document save(Document document);
    
    /**
     * 根据ID查找文档
     */
    Optional<Document> findById(DocumentId id);
    
    /**
     * 根据用户ID和分类ID查找文档列表
     */
    List<Document> findByUserIdAndCategoryId(UserId userId, DocumentCategoryId categoryId);
    
    /**
     * 根据用户ID查找所有文档
     */
    List<Document> findByUserId(UserId userId);
    
    /**
     * 搜索文档（按文件名）
     */
    List<Document> searchByFileName(UserId userId, String keyword);
    
    /**
     * 删除文档
     */
    void delete(DocumentId id);
}
