package com.crazydream.domain.diary.model.valueobject;

/**
 * 心情枚举值对象
 * 定义日记的心情状态
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public enum DiaryMood {
    HAPPY("happy", "开心", "😊"),
    SAD("sad", "难过", "😢"),
    CALM("calm", "平静", "😌"),
    EXCITED("excited", "兴奋", "🤩");
    
    private final String code;
    private final String description;
    private final String emoji;
    
    DiaryMood(String code, String description, String emoji) {
        this.code = code;
        this.description = description;
        this.emoji = emoji;
    }
    
    public static DiaryMood fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (DiaryMood mood : values()) {
            if (mood.code.equalsIgnoreCase(code)) {
                return mood;
            }
        }
        return null;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getEmoji() {
        return emoji;
    }
}
