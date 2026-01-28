package com.crazydream.application.goal.assembler;

import com.crazydream.application.goal.dto.CreateGoalCommand;
import com.crazydream.application.goal.dto.GoalDTO;
import com.crazydream.application.goal.dto.UpdateGoalCommand;
import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.*;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Goal 领域模型与 DTO 装配器
 * 负责 Domain 层与 Application 层的对象转换
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalAssembler {
    
    /**
     * 创建命令 -> 领域对象
     */
    public static Goal toDomain(CreateGoalCommand command) {
        return Goal.create(
                UserId.of(command.getUserId()),
                GoalTitle.of(command.getTitle()),
                command.getDescription(),
                command.getCategoryId() != null ? CategoryId.of(command.getCategoryId()) : null,
                Priority.fromCode(command.getPriority()),
                command.getDeadline()
        );
    }
    
    /**
     * 领域对象 -> DTO
     */
    public static GoalDTO toDTO(Goal goal) {
        if (goal == null) {
            return null;
        }
        
        GoalDTO dto = new GoalDTO();
        if (goal.getId() != null) {
            dto.setId(goal.getId().getValue());
        }
        dto.setUserId(goal.getUserId().getValue());
        dto.setTitle(goal.getTitle().getValue());
        dto.setDescription(goal.getDescription());
        dto.setCategoryId(goal.getCategoryId() != null ? goal.getCategoryId().getValue() : null);
        dto.setPriority(goal.getPriority().getCode());
        dto.setDeadline(goal.getDeadline());
        dto.setProgress(goal.getProgress().getValue());
        dto.setStatus(goal.getStatus().getCode());
        dto.setImageUrl(goal.getImageUrl());
        dto.setCreateTime(goal.getCreateTime());
        dto.setUpdateTime(goal.getUpdateTime());
        
        return dto;
    }
    
    /**
     * 领域对象列表 -> DTO列表
     */
    public static List<GoalDTO> toDTOList(List<Goal> goals) {
        return goals.stream()
                .map(GoalAssembler::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新命令应用到领域对象
     */
    public static void applyUpdateCommand(Goal goal, UpdateGoalCommand command) {
        goal.update(
                GoalTitle.of(command.getTitle()),
                command.getDescription(),
                command.getCategoryId() != null ? CategoryId.of(command.getCategoryId()) : null,
                Priority.fromCode(command.getPriority()),
                command.getDeadline(),
                command.getImageUrl()
        );
    }
}
