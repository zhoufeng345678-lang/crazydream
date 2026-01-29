package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待办事项持久化对象(PO)
 * 仅用于MyBatis数据库映射,不包含业务逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-14
 */
@Data
public class TodoPO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    private LocalDateTime remindTime;
    private Integer remindSent;
    private Long relatedGoalId;
    private LocalDateTime completedTime;
    private Integer sortOrder;
    private String tags;  // JSON字符串
    private Integer estimatedMinutes;
    private Integer actualMinutes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
