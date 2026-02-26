package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReminderPO {
    private Long id;
    private Long userId;
    private Long goalId;
    private String title;
    private LocalDateTime remindTime;
    private Boolean isRead;
    // 新增字段：支持多种实体类型的提醒
    private String relatedEntityType;  // 关联实体类型：GOAL, FAMILY_MEMBER, HEALTH_CHECKUP
    private Long relatedEntityId;      // 关联实体ID
    private String reminderType;       // 提醒类型：GOAL_DEADLINE, BIRTHDAY, HEALTH_CHECKUP
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
