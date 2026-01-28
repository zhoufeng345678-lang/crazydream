package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.goal.repository.GoalRepository;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.domain.shared.model.UserId;
import com.crazydream.infrastructure.persistence.converter.GoalConverter;
import com.crazydream.infrastructure.persistence.mapper.GoalPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.GoalPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 目标仓储实现（Infrastructure 层）
 * 桥接领域模型与持久化
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
@Repository
public class GoalRepositoryImpl implements GoalRepository {
    
    @Autowired
    private GoalPersistenceMapper mapper;
    
    @Override
    public Goal save(Goal goal) {
        GoalPO po = GoalConverter.toPO(goal);
        
        if (goal.getId() == null) {
            // 新增
            mapper.insert(po);
            // 设置生成的ID回到领域对象
            goal.setId(GoalId.of(po.getId()));
        } else {
            // 更新
            mapper.update(po);
        }
        
        return goal;
    }
    
    @Override
    public Optional<Goal> findById(GoalId id) {
        GoalPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(GoalConverter.toDomain(po));
    }
    
    @Override
    public List<Goal> findByUserId(UserId userId) {
        List<GoalPO> poList = mapper.selectByUserId(userId.getValue());
        return poList.stream()
                .map(GoalConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Goal> findByCategoryIdAndUserId(CategoryId categoryId, UserId userId) {
        List<GoalPO> poList = mapper.selectByCategoryIdAndUserId(
                categoryId.getValue(), 
                userId.getValue()
        );
        return poList.stream()
                .map(GoalConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(GoalId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
    
    @Override
    public int batchDelete(List<GoalId> ids) {
        List<Long> idValues = ids.stream()
                .map(GoalId::getValue)
                .collect(Collectors.toList());
        return mapper.batchDelete(idValues);
    }
    
    @Override
    public List<Goal> findRecentByUserId(UserId userId, int limit) {
        List<GoalPO> poList = mapper.selectRecentByUserId(userId.getValue(), limit);
        return poList.stream()
                .map(GoalConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Goal> findTodayReminderGoals(UserId userId, String date) {
        List<GoalPO> poList = mapper.selectTodayReminderGoals(userId.getValue(), date);
        return poList.stream()
                .map(GoalConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public int countCompletedByUserId(UserId userId) {
        return mapper.countCompletedByUserId(userId.getValue());
    }
    
    @Override
    public int countActiveByUserId(UserId userId) {
        return mapper.countActiveByUserId(userId.getValue());
    }
}
