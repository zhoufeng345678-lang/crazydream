package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.goal.model.valueobject.GoalProgress;
import com.crazydream.domain.subgoal.model.aggregate.SubGoal;
import com.crazydream.domain.subgoal.model.valueobject.*;
import com.crazydream.infrastructure.persistence.po.SubGoalPO;

public class SubGoalConverter {
    
    public static SubGoal toDomain(SubGoalPO po) {
        if (po == null) return null;
        
        return SubGoal.rebuild(
            SubGoalId.of(po.getId()),
            GoalId.of(po.getGoalId()),
            SubGoalTitle.of(po.getTitle()),
            po.getDescription(),
            GoalProgress.of(po.getProgress()),
            SubGoalStatus.fromCode(po.getStatus()),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    public static SubGoalPO toPO(SubGoal subGoal) {
        if (subGoal == null) return null;
        
        SubGoalPO po = new SubGoalPO();
        if (subGoal.getId() != null) {
            po.setId(subGoal.getId().getValue());
        }
        po.setGoalId(subGoal.getGoalId().getValue());
        po.setTitle(subGoal.getTitle().getValue());
        po.setDescription(subGoal.getDescription());
        po.setProgress(subGoal.getProgress().getValue());
        po.setStatus(subGoal.getStatus().getCode());
        po.setCreateTime(subGoal.getCreateTime());
        po.setUpdateTime(subGoal.getUpdateTime());
        return po;
    }
}
