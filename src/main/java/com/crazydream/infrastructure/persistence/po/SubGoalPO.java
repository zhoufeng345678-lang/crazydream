package com.crazydream.infrastructure.persistence.po;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubGoalPO {
    private Long id;
    private Long goalId;
    private String title;
    private String description;
    private Integer progress;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
