package com.crazydream.domain.achievement.repository;

import com.crazydream.domain.achievement.model.aggregate.Achievement;
import com.crazydream.domain.achievement.model.valueobject.*;
import com.crazydream.domain.shared.model.UserId;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository {
    Achievement save(Achievement achievement);
    Optional<Achievement> findById(AchievementId id);
    List<Achievement> findByUserId(UserId userId);
    List<Achievement> findUnlockedByUserId(UserId userId);
}
