package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.*;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.GoalPO;

/**
 * Goal 领域模型与持久化对象转换器
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalConverter {
    
    /**
     * PO -> Domain Entity
     */
    public static Goal toDomain(GoalPO po) {
        if (po == null) {
            return null;
        }
        
        return Goal.rebuild(
            GoalId.of(po.getId()),
            UserId.of(po.getUserId()),
            GoalTitle.of(po.getTitle()),
            po.getDescription(),
            po.getCategoryId() != null ? CategoryId.of(po.getCategoryId()) : null,
            Priority.fromCode(po.getPriority()),
            po.getDeadline(),
            GoalProgress.of(po.getProgress()),
            GoalStatus.fromCode(po.getStatus()),
            po.getImageUrl(),
            po.getCreateTime(),
            po.getUpdateTime()
        );
    }
    
    /**
     * Domain Entity -> PO
     */
    public static GoalPO toPO(Goal goal) {
        if (goal == null) {
            return null;
        }
        
        GoalPO po = new GoalPO();
        if (goal.getId() != null) {
            po.setId(goal.getId().getValue());
        }
        po.setUserId(goal.getUserId().getValue());
        po.setTitle(goal.getTitle().getValue());
        po.setDescription(goal.getDescription());
        po.setCategoryId(goal.getCategoryId() != null ? goal.getCategoryId().getValue() : null);
        po.setPriority(goal.getPriority().getCode());
        po.setDeadline(goal.getDeadline());
        po.setProgress(goal.getProgress().getValue());
        po.setStatus(goal.getStatus().getCode());
        po.setImageUrl(goal.getImageUrl());
        po.setCreateTime(goal.getCreateTime());
        po.setUpdateTime(goal.getUpdateTime());
        
        return po;
    }
}
