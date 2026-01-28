package com.crazydream.application.subgoal.assembler;

import com.crazydream.application.subgoal.dto.*;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.subgoal.model.aggregate.SubGoal;
import com.crazydream.domain.subgoal.model.valueobject.SubGoalTitle;

import java.util.List;
import java.util.stream.Collectors;

public class SubGoalAssembler {
    
    public static SubGoal toDomain(CreateSubGoalCommand command) {
        return SubGoal.create(
            GoalId.of(command.getGoalId()),
            SubGoalTitle.of(command.getTitle()),
            command.getDescription()
        );
    }
    
    public static SubGoalDTO toDTO(SubGoal subGoal) {
        if (subGoal == null) return null;
        
        SubGoalDTO dto = new SubGoalDTO();
        if (subGoal.getId() != null) {
            dto.setId(subGoal.getId().getValue());
        }
        dto.setGoalId(subGoal.getGoalId().getValue());
        dto.setTitle(subGoal.getTitle().getValue());
        dto.setDescription(subGoal.getDescription());
        dto.setProgress(subGoal.getProgress().getValue());
        dto.setStatus(subGoal.getStatus().getCode());
        dto.setCreateTime(subGoal.getCreateTime());
        dto.setUpdateTime(subGoal.getUpdateTime());
        return dto;
    }
    
    public static List<SubGoalDTO> toDTOList(List<SubGoal> subGoals) {
        return subGoals.stream()
                .map(SubGoalAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
