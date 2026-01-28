package com.crazydream.application.achievement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AchievementDTO {
    private Long id;
    private Long userId;
    private String type;
    private String typeName;
    private String description;
    private Boolean unlocked;
    private LocalDateTime unlockedTime;
    private LocalDateTime createTime;
}
