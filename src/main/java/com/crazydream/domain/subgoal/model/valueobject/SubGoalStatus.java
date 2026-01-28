package com.crazydream.domain.subgoal.model.valueobject;

/**
 * 子目标状态枚举
 */
public enum SubGoalStatus {
    NOT_STARTED("not_started", "未开始"),
    IN_PROGRESS("in_progress", "进行中"),
    COMPLETED("completed", "已完成");
    
    private final String code;
    private final String description;
    
    SubGoalStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static SubGoalStatus fromCode(String code) {
        if (code == null) {
            return NOT_STARTED;
        }
        for (SubGoalStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的子目标状态: " + code);
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
