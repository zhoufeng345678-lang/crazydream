package com.crazydream.domain.diary.model.valueobject;

/**
 * 日记分类枚举值对象
 * 定义日记的分类类型
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public enum DiaryCategory {
    MOOD("mood", "心情日记", "😊"),
    WORK("work", "工作日记", "💼"),
    STUDY("study", "学习日记", "📚"),
    GENERAL("general", "日常日记", "📝");
    
    private final String code;
    private final String description;
    private final String icon;
    
    DiaryCategory(String code, String description, String icon) {
        this.code = code;
        this.description = description;
        this.icon = icon;
    }
    
    public static DiaryCategory fromCode(String code) {
        if (code == null) {
            return GENERAL;
        }
        for (DiaryCategory category : values()) {
            if (category.code.equalsIgnoreCase(code)) {
                return category;
            }
        }
        return GENERAL;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIcon() {
        return icon;
    }
}
