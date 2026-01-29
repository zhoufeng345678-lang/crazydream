package com.crazydream.application.achievement.assembler;

import com.crazydream.application.achievement.dto.AchievementDTO;
import com.crazydream.domain.achievement.model.aggregate.Achievement;

import java.util.List;
import java.util.stream.Collectors;

public class AchievementAssembler {
    
    public static AchievementDTO toDTO(Achievement achievement) {
        if (achievement == null) return null;
        
        AchievementDTO dto = new AchievementDTO();
        if (achievement.getId() != null) {
            dto.setId(achievement.getId().getValue());
        }
        dto.setUserId(achievement.getUserId().getValue());
        dto.setType(achievement.getType().getCode());
        dto.setName(achievement.getType().getName());          // 映射name
        dto.setTypeName(achievement.getType().getName());
        dto.setDescription(achievement.getType().getDescription());
        dto.setIconUrl(achievement.getIcon());                 // 映射iconUrl
        dto.setCategory(achievement.getCategory());
        dto.setTier(achievement.getTier());
        dto.setProgress(achievement.getProgress());
        dto.setTarget(achievement.getTarget());
        dto.setUnlocked(achievement.isUnlocked());
        dto.setUnlockedTime(achievement.getUnlockedTime());
        dto.setCreateTime(achievement.getCreateTime());
        return dto;
    }
    
    public static List<AchievementDTO> toDTOList(List<Achievement> achievements) {
        return achievements.stream()
                .map(AchievementAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
