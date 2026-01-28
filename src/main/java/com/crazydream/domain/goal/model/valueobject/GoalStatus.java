package com.crazydream.domain.goal.model.valueobject;

/**
 * 目标状态枚举值对象
 * 定义目标的生命周期状态
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public enum GoalStatus {
    NOT_STARTED("not_started", "未开始"),
    IN_PROGRESS("in_progress", "进行中"),
    COMPLETED("completed", "已完成"),
    ABANDONED("abandoned", "已放弃");
    
    private final String code;
    private final String description;
    
    GoalStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static GoalStatus fromCode(String code) {
        if (code == null) {
            return NOT_STARTED; // 默认未开始
        }
        for (GoalStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        // 对于非法值，返回默认值而不是抛出异常，防止旧数据导致系统崩溃
        return NOT_STARTED;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canTransitionTo(GoalStatus target) {
        // 定义状态转换规则
        switch (this) {
            case NOT_STARTED:
                return target == IN_PROGRESS || target == ABANDONED;
            case IN_PROGRESS:
                return target == COMPLETED || target == ABANDONED;
            case COMPLETED:
            case ABANDONED:
                return false; // 终态不可再转换
            default:
                return false;
        }
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == ABANDONED;
    }
}
