package com.crazydream.application.subgoal.dto;

import lombok.Data;

@Data
public class CreateSubGoalCommand {
    private Long goalId;
    private String title;
    private String description;
}
