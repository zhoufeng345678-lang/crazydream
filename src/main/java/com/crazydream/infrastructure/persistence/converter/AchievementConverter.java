package com.crazydream.infrastructure.persistence.converter;

import com.crazydream.domain.achievement.model.aggregate.Achievement;
import com.crazydream.domain.achievement.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.po.AchievementPO;

public class AchievementConverter {
    
    public static Achievement toDomain(AchievementPO po) {
        if (po == null) return null;
        
        return Achievement.rebuild(
            AchievementId.of(po.getId()),
            UserId.of(po.getUserId()),
            AchievementType.fromCode(po.getType()),
            po.getUnlocked() != null && po.getUnlocked(),
            po.getUnlockedTime(),
            po.getCreateTime()
        );
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
        po.setCreateTime(achievement.getCreateTime());
        po.setUpdateTime(java.time.LocalDateTime.now());
        return po;
    }
}
