package com.crazydream.domain.user.model.valueobject;

/**
 * 用户等级枚举
 */
public enum UserLevel {
    BEGINNER(1, "初学者", 0),
    INTERMEDIATE(2, "进阶者", 10),
    ADVANCED(3, "高级者", 30),
    EXPERT(4, "专家", 50);
    
    private final Integer level;
    private final String description;
    private final Integer requiredGoals;  // 升级所需完成目标数
    
    UserLevel(Integer level, String description, Integer requiredGoals) {
        this.level = level;
        this.description = description;
        this.requiredGoals = requiredGoals;
    }
    
    public static UserLevel fromLevel(Integer level) {
        if (level == null) {
            return BEGINNER;
        }
        for (UserLevel userLevel : values()) {
            if (userLevel.level.equals(level)) {
                return userLevel;
            }
        }
        return BEGINNER;
    }
    
    public UserLevel nextLevel() {
        switch (this) {
            case BEGINNER:
                return INTERMEDIATE;
            case INTERMEDIATE:
                return ADVANCED;
            case ADVANCED:
                return EXPERT;
            case EXPERT:
                return EXPERT;
            default:
                return BEGINNER;
        }
    }
    
    public boolean canUpgrade(int completedGoals) {
        UserLevel next = nextLevel();
        return next != this && completedGoals >= next.requiredGoals;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Integer getRequiredGoals() {
        return requiredGoals;
    }
}
