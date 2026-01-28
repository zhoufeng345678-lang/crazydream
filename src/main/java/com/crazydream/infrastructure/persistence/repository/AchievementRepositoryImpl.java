package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.achievement.model.aggregate.Achievement;
import com.crazydream.domain.achievement.model.valueobject.AchievementId;
import com.crazydream.domain.achievement.repository.AchievementRepository;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.AchievementConverter;
import com.crazydream.infrastructure.persistence.mapper.AchievementPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.AchievementPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AchievementRepositoryImpl implements AchievementRepository {
    
    @Autowired
    private AchievementPersistenceMapper mapper;
    
    @Override
    public Achievement save(Achievement achievement) {
        AchievementPO po = AchievementConverter.toPO(achievement);
        if (achievement.getId() == null) {
            mapper.insert(po);
            achievement.setId(AchievementId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return achievement;
    }
    
    @Override
    public Optional<Achievement> findById(AchievementId id) {
        AchievementPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(AchievementConverter.toDomain(po));
    }
    
    @Override
    public List<Achievement> findByUserId(UserId userId) {
        return mapper.selectByUserId(userId.getValue()).stream()
                .map(AchievementConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Achievement> findUnlockedByUserId(UserId userId) {
        return mapper.selectUnlockedByUserId(userId.getValue()).stream()
                .map(AchievementConverter::toDomain)
                .collect(Collectors.toList());
    }
}
