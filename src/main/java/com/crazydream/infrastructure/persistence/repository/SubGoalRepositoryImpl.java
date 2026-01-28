package com.crazydream.infrastructure.persistence.repository;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.subgoal.model.aggregate.SubGoal;
import com.crazydream.domain.subgoal.model.valueobject.SubGoalId;
import com.crazydream.domain.subgoal.repository.SubGoalRepository;
import com.crazydream.infrastructure.persistence.converter.SubGoalConverter;
import com.crazydream.infrastructure.persistence.mapper.SubGoalPersistenceMapper;
import com.crazydream.infrastructure.persistence.po.SubGoalPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SubGoalRepositoryImpl implements SubGoalRepository {
    
    @Autowired
    private SubGoalPersistenceMapper mapper;
    
    @Override
    public SubGoal save(SubGoal subGoal) {
        SubGoalPO po = SubGoalConverter.toPO(subGoal);
        if (subGoal.getId() == null) {
            mapper.insert(po);
            subGoal.setId(SubGoalId.of(po.getId()));
        } else {
            mapper.update(po);
        }
        return subGoal;
    }
    
    @Override
    public Optional<SubGoal> findById(SubGoalId id) {
        SubGoalPO po = mapper.selectById(id.getValue());
        return Optional.ofNullable(SubGoalConverter.toDomain(po));
    }
    
    @Override
    public List<SubGoal> findByGoalId(GoalId goalId) {
        List<SubGoalPO> poList = mapper.selectByGoalId(goalId.getValue());
        return poList.stream()
                .map(SubGoalConverter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean delete(SubGoalId id) {
        return mapper.deleteById(id.getValue()) > 0;
    }
    
    @Override
    public int batchDelete(List<SubGoalId> ids) {
        List<Long> idValues = ids.stream()
                .map(SubGoalId::getValue)
                .collect(Collectors.toList());
        return mapper.batchDelete(idValues);
    }
}
