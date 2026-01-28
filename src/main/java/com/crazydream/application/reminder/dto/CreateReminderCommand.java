package com.crazydream.application.reminder.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateReminderCommand {
    private Long goalId;
    private String title;
    private LocalDateTime remindTime;
}
