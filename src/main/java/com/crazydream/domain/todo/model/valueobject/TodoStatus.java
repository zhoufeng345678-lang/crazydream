package com.crazydream.domain.todo.model.valueobject;

/**
 * 待办状态枚举值对象
 * 定义待办事项的状态
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
public enum TodoStatus {
    PENDING("pending", "待处理"),
    IN_PROGRESS("in_progress", "进行中"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");
    
    private final String code;
    private final String description;
    
    TodoStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public static TodoStatus fromCode(String code) {
        if (code == null) {
            return PENDING;
        }
        for (TodoStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return PENDING;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isCompleted() {
        return this == COMPLETED;
    }
    
    public boolean canTransitionTo(TodoStatus newStatus) {
        // 定义状态转换规则
        switch (this) {
            case PENDING:
                return newStatus == IN_PROGRESS || newStatus == COMPLETED || newStatus == CANCELLED;
            case IN_PROGRESS:
                return newStatus == COMPLETED || newStatus == PENDING || newStatus == CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // 已完成或已取消的待办不能转换状态
            default:
                return false;
        }
    }
}
