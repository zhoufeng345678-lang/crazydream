package com.crazydream.infrastructure.persistence.mapper;

import com.crazydream.infrastructure.persistence.po.AchievementPO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AchievementPersistenceMapper {
    int insert(AchievementPO achievement);
    int update(AchievementPO achievement);
    AchievementPO selectById(Long id);
    List<AchievementPO> selectByUserId(Long userId);
    List<AchievementPO> selectUnlockedByUserId(Long userId);
}
