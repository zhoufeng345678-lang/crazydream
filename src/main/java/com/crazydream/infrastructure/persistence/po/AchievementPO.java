package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AchievementPO {
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String description;
    private String badgeUrl;
    private String unlockCondition;
    private Boolean unlocked;
    private Boolean isUnlocked;
    private java.time.LocalDateTime unlockedTime;
    private java.time.LocalDateTime unlockedAt;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
}
