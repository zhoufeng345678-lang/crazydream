package com.crazydream.application.subgoal.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubGoalDTO {
    private Long id;
    private Long goalId;
    private String title;
    private String description;
    private Integer progress;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
