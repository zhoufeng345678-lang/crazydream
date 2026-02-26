package com.crazydream.domain.document.model.valueobject;

/**
 * 文件类型枚举
 */
public enum FileType {
    
    PDF("PDF", "application/pdf", ".pdf"),
    DOC("DOC", "application/msword", ".doc"),
    DOCX("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"),
    XLS("XLS", "application/vnd.ms-excel", ".xls"),
    XLSX("XLSX", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    PPT("PPT", "application/vnd.ms-powerpoint", ".ppt"),
    PPTX("PPTX", "application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"),
    TXT("TXT", "text/plain", ".txt"),
    IMAGE("IMAGE", "image/*", ".jpg,.png,.gif,.jpeg,.bmp,.webp");
    
    private final String code;
    private final String mimeType;
    private final String extensions;
    
    FileType(String code, String mimeType, String extensions) {
        this.code = code;
        this.mimeType = mimeType;
        this.extensions = extensions;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public String getExtensions() {
        return extensions;
    }
    
    /**
     * 根据文件扩展名获取文件类型
     */
    public static FileType fromExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("文件扩展名不能为空");
        }
        
        String ext = extension.toLowerCase();
        if (!ext.startsWith(".")) {
            ext = "." + ext;
        }
        
        for (FileType type : values()) {
            if (type.extensions.contains(ext)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("不支持的文件类型: " + extension);
    }
    
    /**
     * 检查是否为图片类型
     */
    public boolean isImage() {
        return this == IMAGE;
    }
}
