package com.crazydream.domain.subgoal.repository;

import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.subgoal.model.aggregate.SubGoal;
import com.crazydream.domain.subgoal.model.valueobject.SubGoalId;

import java.util.List;
import java.util.Optional;

public interface SubGoalRepository {
    SubGoal save(SubGoal subGoal);
    Optional<SubGoal> findById(SubGoalId id);
    List<SubGoal> findByGoalId(GoalId goalId);
    boolean delete(SubGoalId id);
    int batchDelete(List<SubGoalId> ids);
}
