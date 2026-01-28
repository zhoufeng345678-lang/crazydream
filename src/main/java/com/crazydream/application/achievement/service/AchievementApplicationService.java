package com.crazydream.application.achievement.service;

import com.crazydream.application.achievement.assembler.AchievementAssembler;
import com.crazydream.application.achievement.dto.AchievementDTO;
import com.crazydream.domain.achievement.model.aggregate.Achievement;
import com.crazydream.domain.achievement.model.valueobject.AchievementStatistics;
import com.crazydream.domain.achievement.model.valueobject.AchievementType;
import com.crazydream.domain.achievement.repository.AchievementRepository;
import com.crazydream.domain.shared.model.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AchievementApplicationService {
    
    @Autowired
    private AchievementRepository achievementRepository;
    
    @Autowired
    private AchievementStatisticsService statisticsService;
    
    private List<Achievement> ensureAllAchievementTypesExist(Long userId) {
        UserId uid = UserId.of(userId);
        List<Achievement> achievements = achievementRepository.findByUserId(uid);
        java.util.Set<AchievementType> existingTypes = achievements.stream()
                .map(Achievement::getType)
                .collect(java.util.stream.Collectors.toSet());
        for (AchievementType type : AchievementType.values()) {
            if (!existingTypes.contains(type)) {
                Achievement achievement = Achievement.create(uid, type);
                achievement = achievementRepository.save(achievement);
                achievements.add(achievement);
            }
        }
        return achievements;
    }
    
    public List<AchievementDTO> getUserAchievements(Long userId) {
        List<Achievement> achievements = ensureAllAchievementTypesExist(userId);
        return AchievementAssembler.toDTOList(achievements);
    }
    
    public List<AchievementDTO> getUnlockedAchievements(Long userId) {
        List<Achievement> achievements = achievementRepository.findUnlockedByUserId(UserId.of(userId));
        return AchievementAssembler.toDTOList(achievements);
    }
    
    @Transactional
    public void checkAndUnlock(Long userId) {
        List<Achievement> achievements = achievementRepository.findByUserId(UserId.of(userId));
        AchievementStatistics statistics = statisticsService.collectStatistics(userId);
        
        for (Achievement achievement : achievements) {
            if (!achievement.isUnlocked() && achievement.canUnlock(statistics)) {
                achievement.unlock();
                achievementRepository.save(achievement);
            }
        }
    }
    
    /**
     * 兼容旧签名，内部委托给新实现
     * @deprecated 使用 {@link #checkAndUnlock(Long)}
     */
    @Deprecated
    @Transactional
    public void checkAndUnlock(Long userId, int goalCount, int userLevel) {
        checkAndUnlock(userId);
    }
    
    @Transactional
    public AchievementDTO unlockAchievement(Long userId, Long achievementId) {
        Achievement achievement = achievementRepository.findById(com.crazydream.domain.achievement.model.valueobject.AchievementId.of(achievementId))
                .orElseThrow(() -> new IllegalArgumentException("成就不存在: " + achievementId));
        
        // 验证权限
        if (!achievement.belongsTo(UserId.of(userId))) {
            throw new IllegalArgumentException("无权限操作此成就");
        }
        
        // 解锁
        achievement.unlock();
        achievement = achievementRepository.save(achievement);
        
        return AchievementAssembler.toDTO(achievement);
    }
}
