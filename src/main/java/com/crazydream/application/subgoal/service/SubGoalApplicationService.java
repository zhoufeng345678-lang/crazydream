package com.crazydream.application.subgoal.service;

import com.crazydream.application.subgoal.assembler.SubGoalAssembler;
import com.crazydream.application.subgoal.dto.*;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.subgoal.model.aggregate.SubGoal;
import com.crazydream.domain.subgoal.model.valueobject.SubGoalId;
import com.crazydream.domain.subgoal.repository.SubGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubGoalApplicationService {
    
    @Autowired
    private SubGoalRepository subGoalRepository;
    
    @Transactional
    public SubGoalDTO createSubGoal(CreateSubGoalCommand command) {
        SubGoal subGoal = SubGoalAssembler.toDomain(command);
        subGoal = subGoalRepository.save(subGoal);
        return SubGoalAssembler.toDTO(subGoal);
    }
    
    public List<SubGoalDTO> getSubGoalsByGoalId(Long goalId) {
        List<SubGoal> subGoals = subGoalRepository.findByGoalId(GoalId.of(goalId));
        return SubGoalAssembler.toDTOList(subGoals);
    }
    
    public SubGoalDTO getSubGoalById(Long id) {
        SubGoal subGoal = subGoalRepository.findById(SubGoalId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("子目标不存在: " + id));
        return SubGoalAssembler.toDTO(subGoal);
    }
    
    @Transactional
    public boolean deleteSubGoal(Long id) {
        return subGoalRepository.delete(SubGoalId.of(id));
    }
    
    @Transactional
    public int batchDeleteSubGoals(List<Long> ids) {
        List<SubGoalId> subGoalIds = ids.stream()
                .map(SubGoalId::of)
                .collect(java.util.stream.Collectors.toList());
        return subGoalRepository.batchDelete(subGoalIds);
    }
    
    @Transactional
    public SubGoalDTO updateProgress(Long id, int progress) {
        SubGoal subGoal = subGoalRepository.findById(SubGoalId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("子目标不存在: " + id));
        subGoal.updateProgress(progress);
        subGoal = subGoalRepository.save(subGoal);
        return SubGoalAssembler.toDTO(subGoal);
    }
}
