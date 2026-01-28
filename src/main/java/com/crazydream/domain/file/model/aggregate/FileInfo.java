package com.crazydream.domain.file.model.aggregate;

import com.crazydream.domain.file.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件聚合根（充血模型）
 */
public class FileInfo {
    private FileId id;
    private UserId userId;
    private String fileName;
    private String fileUrl;
    private FileType fileType;
    private Long fileSize;
    private LocalDateTime uploadTime;
    
    private FileInfo() {}
    
    public static FileInfo create(UserId userId, String fileName, String fileUrl, Long fileSize) {
        FileInfo file = new FileInfo();
        file.userId = userId;
        file.fileName = fileName;
        file.fileUrl = fileUrl;
        file.fileType = FileType.fromFileName(fileName);
        file.fileSize = fileSize;
        file.uploadTime = LocalDateTime.now();
        return file;
    }
    
    public static FileInfo rebuild(FileId id, UserId userId, String fileName, String fileUrl,
                                   FileType fileType, Long fileSize, LocalDateTime uploadTime) {
        FileInfo file = new FileInfo();
        file.id = id;
        file.userId = userId;
        file.fileName = fileName;
        file.fileUrl = fileUrl;
        file.fileType = fileType;
        file.fileSize = fileSize;
        file.uploadTime = uploadTime;
        return file;
    }
    
    // ==================== 业务行为 ====================
    
    public boolean belongsTo(UserId userId) {
        return this.userId.equals(userId);
    }
    
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    public boolean isImage() {
        return fileType == FileType.IMAGE;
    }
    
    // ==================== Getters ====================
    
    public FileId getId() { return id; }
    public UserId getUserId() { return userId; }
    public String getFileName() { return fileName; }
    public String getFileUrl() { return fileUrl; }
    public FileType getFileType() { return fileType; }
    public Long getFileSize() { return fileSize; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    
    public void setId(FileId id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(id, fileInfo.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
