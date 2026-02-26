package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.document.model.aggregate.Document;
import com.crazydream.domain.document.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.DocumentPO;

/**
 * 文档转换器
 */
public class DocumentConverter {
    
    public static Document toDomain(DocumentPO po) {
        if (po == null) {
            return null;
        }
        
        return Document.rebuild(
            DocumentId.of(po.getId()),
            UserId.of(po.getUserId()),
            DocumentCategoryId.of(po.getCategoryId()),
            po.getFileName(),
            po.getOriginalName(),
            FileType.valueOf(po.getFileType()),
            po.getFileExtension(),
            po.getFileSize(),
            po.getFilePath(),
            po.getFileUrl(),
            po.getMimeType(),
            po.getDescription(),
            po.getTags(),
            po.getDownloadCount(),
            po.getViewCount(),
            po.getStatus(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    public static DocumentPO toPO(Document document) {
        if (document == null) {
            return null;
        }
        
        DocumentPO po = new DocumentPO();
        if (document.getId() != null) {
            po.setId(document.getId().getValue());
        }
        po.setUserId(document.getUserId().getValue());
        po.setCategoryId(document.getCategoryId().getValue());
        po.setFileName(document.getFileName());
        po.setOriginalName(document.getOriginalName());
        po.setFileType(document.getFileType().name());
        po.setFileExtension(document.getFileExtension());
        po.setFileSize(document.getFileSize());
        po.setFilePath(document.getFilePath());
        po.setFileUrl(document.getFileUrl());
        po.setMimeType(document.getMimeType());
        po.setDescription(document.getDescription());
        po.setTags(document.getTags());
        po.setDownloadCount(document.getDownloadCount());
        po.setViewCount(document.getViewCount());
        po.setStatus(document.getStatus());
        po.setCreateTime(document.getCreateTime());
        po.setUpdateTime(document.getUpdateTime());
        return po;
    }
}
