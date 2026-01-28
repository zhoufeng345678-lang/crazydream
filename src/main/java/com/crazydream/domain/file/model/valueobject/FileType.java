package com.crazydream.domain.file.model.valueobject;

public enum FileType {
    IMAGE("image", "图片"),
    VIDEO("video", "视频"),
    DOCUMENT("document", "文档"),
    AUDIO("audio", "音频"),
    OTHER("other", "其他");
    
    private final String code;
    private final String description;
    
    FileType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static FileType fromCode(String code) {
        if (code == null) {
            return OTHER;
        }
        for (FileType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return OTHER;
    }
    
    public static FileType fromFileName(String fileName) {
        if (fileName == null) return OTHER;
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "webp":
                return IMAGE;
            case "mp4":
            case "avi":
            case "mov":
                return VIDEO;
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
                return DOCUMENT;
            case "mp3":
            case "wav":
                return AUDIO;
            default:
                return OTHER;
        }
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
}
