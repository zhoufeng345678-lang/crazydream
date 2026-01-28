package com.crazydream.application.reminder.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReminderDTO {
    private Long id;
    private Long userId;
    private Long goalId;
    private String title;
    private LocalDateTime remindTime;
    private Boolean read;
    private Boolean overdue;
    private LocalDateTime createTime;
}
