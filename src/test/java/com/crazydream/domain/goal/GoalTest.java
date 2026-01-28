package com.crazydream.domain.goal;

import com.crazydream.domain.goal.model.aggregate.Goal;
import com.crazydream.domain.goal.model.valueobject.*;
import com.crazydream.domain.shared.model.CategoryId;
import com.crazydream.domain.shared.model.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Goal 领域模型单元测试
 * 测试充血模型的业务行为
 * 
 * @author CrazyDream Team
 * @since 2026-01-12
 */
public class GoalTest {
    
    @Test
    public void testCreateGoal() {
        // Given
        UserId userId = UserId.of(1L);
        GoalTitle title = GoalTitle.of("学习Java");
        String description = "掌握Java核心技术";
        
        // When
        Goal goal = Goal.create(userId, title, description, null, Priority.HIGH, null);
        
        // Then
        assertNotNull(goal);
        assertEquals("学习Java", goal.getTitle().getValue());
        assertEquals(GoalStatus.NOT_STARTED, goal.getStatus());
        assertEquals(0, goal.getProgress().getValue());
        assertEquals(Priority.HIGH, goal.getPriority());
    }
    
    @Test
    public void testStartGoal() {
        // Given
        Goal goal = createTestGoal();
        
        // When
        goal.start();
        
        // Then
        assertEquals(GoalStatus.IN_PROGRESS, goal.getStatus());
    }
    
    @Test
    public void testUpdateProgress() {
        // Given
        Goal goal = createTestGoal();
        goal.start();
        
        // When
        goal.updateProgress(50);
        
        // Then
        assertEquals(50, goal.getProgress().getValue());
    }
    
    @Test
    public void testUpdateProgressAutoComplete() {
        // Given
        Goal goal = createTestGoal();
        goal.start();
        
        // When - 进度达到100%应自动完成
        goal.updateProgress(100);
        
        // Then
        assertEquals(100, goal.getProgress().getValue());
        assertEquals(GoalStatus.COMPLETED, goal.getStatus());
    }
    
    @Test
    public void testCompleteGoal() {
        // Given
        Goal goal = createTestGoal();
        goal.start();
        
        // When
        goal.complete();
        
        // Then
        assertEquals(GoalStatus.COMPLETED, goal.getStatus());
        assertEquals(100, goal.getProgress().getValue());
    }
    
    @Test
    public void testAbandonGoal() {
        // Given
        Goal goal = createTestGoal();
        goal.start();
        
        // When
        goal.abandon();
        
        // Then
        assertEquals(GoalStatus.ABANDONED, goal.getStatus());
    }
    
    @Test
    public void testCannotUpdateTerminalGoal() {
        // Given
        Goal goal = createTestGoal();
        goal.start();
        goal.complete();
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            goal.updateProgress(80);
        });
    }
    
    @Test
    public void testBelongsTo() {
        // Given
        UserId userId = UserId.of(1L);
        Goal goal = Goal.create(userId, GoalTitle.of("Test"), "", null, null, null);
        
        // Then
        assertTrue(goal.belongsTo(userId));
        assertFalse(goal.belongsTo(UserId.of(2L)));
    }
    
    @Test
    public void testBelongsToCategory() {
        // Given
        CategoryId categoryId = CategoryId.of(1L);
        Goal goal = Goal.create(
                UserId.of(1L),
                GoalTitle.of("Test"),
                "",
                categoryId,
                null,
                null
        );
        
        // Then
        assertTrue(goal.belongsToCategory(categoryId));
        assertFalse(goal.belongsToCategory(CategoryId.of(2L)));
    }
    
    // Helper method
    private Goal createTestGoal() {
        return Goal.create(
                UserId.of(1L),
                GoalTitle.of("测试目标"),
                "这是一个测试目标",
                CategoryId.of(1L),
                Priority.MEDIUM,
                null
        );
    }
}
