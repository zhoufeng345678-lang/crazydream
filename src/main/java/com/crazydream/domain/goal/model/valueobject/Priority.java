package com.crazydream.domain.goal.model.valueobject;

/**
 * 优先级枚举值对象
 * 定义目标的优先级等级
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public enum Priority {
    LOW("low", "低优先级", 1),
    MEDIUM("medium", "中优先级", 2),
    HIGH("high", "高优先级", 3);
    
    private final String code;
    private final String description;
    private final int level;
    
    Priority(String code, String description, int level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }
    
    public static Priority fromCode(String code) {
        if (code == null) {
            return MEDIUM; // 默认中优先级
        }
        for (Priority priority : values()) {
            if (priority.code.equalsIgnoreCase(code)) {
                return priority;
            }
        }
        // 对于非法值，返回默认值而不是抛出异常，防止旧数据导致系统崩溃
        return MEDIUM;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isHigherThan(Priority other) {
        return this.level > other.level;
    }
    
    public boolean isLowerThan(Priority other) {
        return this.level < other.level;
    }
}
