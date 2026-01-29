package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.achievement.model.aggregate.Achievement;
import com.crazydream.domain.achievement.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.AchievementPO;

public class AchievementConverter {
    
    public static Achievement toDomain(AchievementPO po) {
        if (po == null) return null;
        
        Achievement achievement = Achievement.rebuild(
            AchievementId.of(po.getId()),
            UserId.of(po.getUserId()),
            AchievementType.fromCode(po.getType()),
            po.getUnlocked() != null && po.getUnlocked(),
            po.getUnlockedTime(),
            po.getCreateTime()
        );
        
        // 设置其他字段（如果数据库中有值，优先使用）
        if (po.getProgress() != null) {
            achievement.updateProgress(po.getProgress(), 
                                      po.getTarget() != null ? po.getTarget() : achievement.getType().getTarget());
        }
        
        return achievement;
    }
    
    public static AchievementPO toPO(Achievement achievement) {
        if (achievement == null) return null;
        
        AchievementPO po = new AchievementPO();
        if (achievement.getId() != null) {
            po.setId(achievement.getId().getValue());
        }
        po.setUserId(achievement.getUserId().getValue());
        po.setType(achievement.getType().getCode());
        po.setTitle(achievement.getType().getName());
        po.setDescription(achievement.getType().getDescription());
        po.setBadgeUrl(null);
        po.setUnlockCondition(null);
        po.setUnlocked(achievement.isUnlocked());
        po.setIsUnlocked(achievement.isUnlocked());
        po.setUnlockedTime(achievement.getUnlockedTime());
        po.setUnlockedAt(achievement.getUnlockedTime());
        
        // 新字段
        po.setProgress(achievement.getProgress());
        po.setTarget(achievement.getTarget());
        po.setCategory(achievement.getCategory());
        po.setTier(achievement.getTier());
        po.setIcon(achievement.getIcon());
        po.setSortOrder(achievement.getType().getSortOrder());
        
        po.setCreateTime(achievement.getCreateTime());
        po.setUpdateTime(java.time.LocalDateTime.now());
        return po;
    }
}
