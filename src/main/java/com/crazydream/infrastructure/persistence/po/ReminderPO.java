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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
