package com.crazydream.application.document.assembler;

import com.crazydream.application.document.dto.DocumentCategoryDTO;
import com.crazydream.application.document.dto.DocumentDTO;
import com.crazydream.domain.document.model.aggregate.Document;
import com.crazydream.domain.document.model.aggregate.DocumentCategory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档装配器
 */
public class DocumentAssembler {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static DocumentCategoryDTO toCategoryDTO(DocumentCategory category) {
        if (category == null) {
            return null;
        }
        
        DocumentCategoryDTO dto = new DocumentCategoryDTO();
        if (category.getId() != null) {
            dto.setId(category.getId().getValue());
        }
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setSortOrder(category.getSortOrder());
        return dto;
    }
    
    public static List<DocumentCategoryDTO> toCategoryDTOList(List<DocumentCategory> categories) {
        return categories.stream()
                .map(DocumentAssembler::toCategoryDTO)
                .collect(Collectors.toList());
    }
    
    public static DocumentDTO toDTO(Document document) {
        if (document == null) {
            return null;
        }
        
        DocumentDTO dto = new DocumentDTO();
        if (document.getId() != null) {
            dto.setId(document.getId().getValue());
        }
        dto.setUserId(document.getUserId().getValue());
        dto.setCategoryId(document.getCategoryId().getValue());
        dto.setFileName(document.getFileName());
        dto.setOriginalName(document.getOriginalName());
        dto.setFileType(document.getFileType().name());
        dto.setFileExtension(document.getFileExtension());
        dto.setFileSize(document.getFileSize());
        dto.setFileUrl(document.getFileUrl());
        dto.setMimeType(document.getMimeType());
        dto.setDescription(document.getDescription());
        dto.setTags(document.getTags());
        dto.setDownloadCount(document.getDownloadCount());
        dto.setViewCount(document.getViewCount());
        if (document.getCreateTime() != null) {
            dto.setCreateTime(document.getCreateTime().format(FORMATTER));
        }
        return dto;
    }
    
    public static List<DocumentDTO> toDTOList(List<Document> documents) {
        return documents.stream()
                .map(DocumentAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
