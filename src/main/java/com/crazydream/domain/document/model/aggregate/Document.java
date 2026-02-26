package com.crazydream.domain.document.model.aggregate;

import com.crazydream.domain.document.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import java.time.LocalDateTime;

/**
 * 文档聚合根
 */
public class Document {
    
    private DocumentId id;
    private UserId userId;
    private DocumentCategoryId categoryId;
    private String fileName;
    private String originalName;
    private FileType fileType;
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
    
    private Document() {
    }
    
    /**
     * 创建文档
     */
    public static Document create(UserId userId, DocumentCategoryId categoryId, 
                                  String originalName, FileType fileType, String fileExtension,
                                  Long fileSize, String filePath, String fileUrl, String mimeType) {
        Document document = new Document();
        document.userId = userId;
        document.categoryId = categoryId;
        document.fileName = generateFileName(originalName);
        document.originalName = originalName;
        document.fileType = fileType;
        document.fileExtension = fileExtension;
        document.fileSize = fileSize;
        document.filePath = filePath;
        document.fileUrl = fileUrl;
        document.mimeType = mimeType;
        document.downloadCount = 0;
        document.viewCount = 0;
        document.status = 1;
        document.createTime = LocalDateTime.now();
        document.updateTime = LocalDateTime.now();
        return document;
    }
    
    /**
     * 重建文档（从数据库加载）
     */
    public static Document rebuild(DocumentId id, UserId userId, DocumentCategoryId categoryId,
                                    String fileName, String originalName, FileType fileType,
                                    String fileExtension, Long fileSize, String filePath, String fileUrl,
                                    String mimeType, String description, String tags,
                                    Integer downloadCount, Integer viewCount, Integer status,
                                    LocalDateTime createTime, LocalDateTime updateTime) {
        Document document = new Document();
        document.id = id;
        document.userId = userId;
        document.categoryId = categoryId;
        document.fileName = fileName;
        document.originalName = originalName;
        document.fileType = fileType;
        document.fileExtension = fileExtension;
        document.fileSize = fileSize;
        document.filePath = filePath;
        document.fileUrl = fileUrl;
        document.mimeType = mimeType;
        document.description = description;
        document.tags = tags;
        document.downloadCount = downloadCount;
        document.viewCount = viewCount;
        document.status = status;
        document.createTime = createTime;
        document.updateTime = updateTime;
        return document;
    }
    
    private static String generateFileName(String originalName) {
        return System.currentTimeMillis() + "_" + originalName;
    }
    
    /**
     * 增加下载次数
     */
    public void incrementDownloadCount() {
        this.downloadCount++;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 增加查看次数
     */
    public void incrementViewCount() {
        this.viewCount++;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新信息
     */
    public void update(DocumentCategoryId categoryId, String description, String tags) {
        if (categoryId != null) {
            this.categoryId = categoryId;
        }
        this.description = description;
        this.tags = tags;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 删除文档
     */
    public void delete() {
        this.status = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public DocumentId getId() {
        return id;
    }
    
    public void setId(DocumentId id) {
        this.id = id;
    }
    
    public UserId getUserId() {
        return userId;
    }
    
    public DocumentCategoryId getCategoryId() {
        return categoryId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getOriginalName() {
        return originalName;
    }
    
    public FileType getFileType() {
        return fileType;
    }
    
    public String getFileExtension() {
        return fileExtension;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTags() {
        return tags;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
}
