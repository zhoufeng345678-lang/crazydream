package com.crazydream.application.document.dto;

import lombok.Data;

/**
 * 文档DTO
 */
@Data
public class DocumentDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private String fileName;
    private String originalName;
    private String fileType;
    private String fileExtension;
    private Long fileSize;
    private String fileUrl;
    private String mimeType;
    private String description;
    private String tags;
    private Integer downloadCount;
    private Integer viewCount;
    private String createTime;
}
