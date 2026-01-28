package com.crazydream.application.goal;

import com.crazydream.application.goal.dto.CreateGoalCommand;
import com.crazydream.application.goal.dto.GoalDTO;
import com.crazydream.application.goal.dto.UpdateGoalCommand;
import com.crazydream.application.goal.service.GoalApplicationService;
import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.GoalId;
import com.crazydream.domain.goal.repository.GoalRepository;
import com.crazydream.domain.shared.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Goal 应用服务单元测试
 * 使用 Mock 测试应用层逻辑
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalApplicationServiceTest {
    
    @Mock
    private GoalRepository goalRepository;
    
    @InjectMocks
    private GoalApplicationService goalApplicationService;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateGoal() {
        // Given
        CreateGoalCommand command = new CreateGoalCommand();
        command.setUserId(1L);
        command.setTitle("学习DDD");
        command.setDescription("深入理解领域驱动设计");
        command.setPriority("high");
        
        // Mock repository
        when(goalRepository.save(any(Goal.class))).thenAnswer(invocation -> {
            Goal goal = invocation.getArgument(0);
            goal.setId(GoalId.of(1L));
            return goal;
        });
        
        // When
        GoalDTO result = goalApplicationService.createGoal(command);
        
        // Then
        assertNotNull(result);
        assertEquals("学习DDD", result.getTitle());
        assertEquals(1L, result.getUserId());
        verify(goalRepository, times(1)).save(any(Goal.class));
    }
    
    @Test
    public void testUpdateGoal() {
        // Given
        UpdateGoalCommand command = new UpdateGoalCommand();
        command.setId(1L);
        command.setUserId(1L);
        command.setTitle("更新后的标题");
        command.setDescription("更新后的描述");
        command.setPriority("high");
        
        Goal existingGoal = Goal.create(
                UserId.of(1L),
                com.crazydream.domain.goal.model.valueobject.GoalTitle.of("原标题"),
                "原描述",
                null,
                com.crazydream.domain.goal.model.valueobject.Priority.MEDIUM,
                null
        );
        existingGoal.setId(GoalId.of(1L));
        
        when(goalRepository.findById(any(GoalId.class))).thenReturn(Optional.of(existingGoal));
        when(goalRepository.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        GoalDTO result = goalApplicationService.updateGoal(command);
        
        // Then
        assertNotNull(result);
        assertEquals("更新后的标题", result.getTitle());
        verify(goalRepository, times(1)).findById(any(GoalId.class));
        verify(goalRepository, times(1)).save(any(Goal.class));
    }
    
    @Test
    public void testUpdateProgressToComplete() {
        // Given
        Long goalId = 1L;
        Long userId = 1L;
        
        Goal goal = Goal.create(
                UserId.of(userId),
                com.crazydream.domain.goal.model.valueobject.GoalTitle.of("测试目标"),
                "测试",
                null,
                null,
                null
        );
        goal.setId(GoalId.of(goalId));
        goal.start();
        
        when(goalRepository.findById(any(GoalId.class))).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        GoalDTO result = goalApplicationService.updateProgress(goalId, userId, 100);
        
        // Then
        assertEquals(100, result.getProgress());
        assertEquals("completed", result.getStatus());
    }
    
    @Test
    public void testDeleteGoalNotFound() {
        // Given
        when(goalRepository.findById(any(GoalId.class))).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            goalApplicationService.deleteGoal(1L, 1L);
        });
    }
    
    @Test
    public void testDeleteGoalUnauthorized() {
        // Given
        Goal goal = Goal.create(
                UserId.of(1L),
                com.crazydream.domain.goal.model.valueobject.GoalTitle.of("Test"),
                "",
                null,
                null,
                null
        );
        goal.setId(GoalId.of(1L));
        
        when(goalRepository.findById(any(GoalId.class))).thenReturn(Optional.of(goal));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            goalApplicationService.deleteGoal(1L, 999L); // 不同的用户ID
        });
    }
}
