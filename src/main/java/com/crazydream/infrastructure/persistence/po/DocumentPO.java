package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 资料文档持久化对象
 */
@Data
public class DocumentPO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String fileName;
    private String originalName;
    private String fileType;
    private String fileExtension;
    private Long fileSize;
    private String filePath;
    private String fileUrl;
    private String mimeType;
    private String description;
    private String tags;
    private Integer downloadCount;
    private Integer viewCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
