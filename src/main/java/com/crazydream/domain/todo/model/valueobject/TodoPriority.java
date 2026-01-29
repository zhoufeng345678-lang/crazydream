package com.crazydream.domain.todo.model.valueobject;

/**
 * 待办优先级枚举值对象
 * 定义待办事项的优先级等级
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public enum TodoPriority {
    LOW("low", "低", 1),
    MEDIUM("medium", "中", 2),
    HIGH("high", "高", 3),
    URGENT("urgent", "紧急", 4);
    
    private final String code;
    private final String description;
    private final int level;
    
    TodoPriority(String code, String description, int level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }
    
    public static TodoPriority fromCode(String code) {
        if (code == null) {
            return MEDIUM;
        }
        for (TodoPriority priority : values()) {
            if (priority.code.equalsIgnoreCase(code)) {
                return priority;
            }
        }
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
    
    public boolean isHigherThan(TodoPriority other) {
        return this.level > other.level;
    }
    
    public boolean isLowerThan(TodoPriority other) {
        return this.level < other.level;
    }
}
