package com.crazydream.service;

import com.crazydream.entity.Goal;
import com.crazydream.mapper.GoalMapper;
import com.crazydream.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalServiceTest {
    
    @Mock
    private GoalMapper goalMapper;
    
    @InjectMocks
    private GoalServiceImpl goalService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetGoalsByUserId() {
        // 准备测试数据
        List<Goal> goals = new ArrayList<>();
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setUserId(100L);
        goal1.setTitle("学习Java");
        goals.add(goal1);
        
        // 模拟Mapper行为
        when(goalMapper.getGoalsByUserId(100L)).thenReturn(goals);
        
        // 执行测试
        List<Goal> result = goalService.getGoalsByUserId(100L);
        
        // 验证结果
        assertEquals(1, result.size());
        assertEquals("学习Java", result.get(0).getTitle());
        verify(goalMapper, times(1)).getGoalsByUserId(100L);
    }
    
    @Test
    void testGetGoalStatistics() {
        // 准备测试数据
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("inProgress", 2);
        statistics.put("completed", 1);
        statistics.put("total", 3);
        
        // 模拟Mapper行为
        when(goalMapper.getGoalStatistics(100L)).thenReturn(statistics);
        
        // 执行测试
        Map<String, Integer> result = goalService.getGoalStatistics(100L);
        
        // 验证结果
        assertEquals(2, result.get("inProgress"));
        assertEquals(1, result.get("completed"));
        assertEquals(3, result.get("total"));
        verify(goalMapper, times(1)).getGoalStatistics(100L);
    }
    
    @Test
    void testGetGoalsByCategoryId() {
        // 准备测试数据
        List<Goal> goals = new ArrayList<>();
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setUserId(100L);
        goal1.setCategoryId(1L);
        goal1.setTitle("学习Java");
        goals.add(goal1);
        
        // 模拟Mapper行为
        when(goalMapper.getGoalsByCategoryId(1L, 100L)).thenReturn(goals);
        
        // 执行测试
        List<Goal> result = goalService.getGoalsByCategoryId(1L, 100L);
        
        // 验证结果
        assertEquals(1, result.size());
        assertEquals("学习Java", result.get(0).getTitle());
        verify(goalMapper, times(1)).getGoalsByCategoryId(1L, 100L);
    }
    
    @Test
    void testGetGoalById() {
        // 准备测试数据
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setUserId(100L);
        goal.setTitle("学习Java");
        
        // 模拟Mapper行为
        when(goalMapper.getGoalById(1L, 100L)).thenReturn(goal);
        
        // 执行测试
        Goal result = goalService.getGoalById(1L, 100L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("学习Java", result.getTitle());
        verify(goalMapper, times(1)).getGoalById(1L, 100L);
    }
    
    @Test
    void testCreateGoal() {
        // 准备测试数据
        Goal goal = new Goal();
        goal.setUserId(100L);
        goal.setTitle("学习Java");
        goal.setId(1L);
        
        // 模拟Mapper行为
        when(goalMapper.insertGoal(goal)).thenReturn(1);
        
        // 执行测试
        Goal result = goalService.createGoal(goal);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("学习Java", result.getTitle());
        assertEquals(0, result.getProgress());
        assertEquals("in_progress", result.getStatus());
        verify(goalMapper, times(1)).insertGoal(goal);
    }
    
    @Test
    void testCreateGoalWithEmptyTitle() {
        // 准备测试数据
        Goal goal = new Goal();
        goal.setUserId(100L);
        goal.setTitle("");
        
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            goalService.createGoal(goal);
        });
    }
    
    @Test
    void testUpdateGoal() {
        // 准备测试数据
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setUserId(100L);
        goal.setTitle("更新的目标");
        goal.setProgress(50);
        goal.setStatus("in_progress");
        
        // 模拟Mapper行为
        when(goalMapper.updateGoal(goal)).thenReturn(1);
        when(goalMapper.getGoalById(1L, 100L)).thenReturn(goal);
        
        // 执行测试
        Goal result = goalService.updateGoal(goal);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("更新的目标", result.getTitle());
        assertEquals(50, result.getProgress());
        verify(goalMapper, times(1)).updateGoal(goal);
    }
    
    @Test
    void testDeleteGoal() {
        // 模拟Mapper行为
        when(goalMapper.deleteGoal(1L, 100L)).thenReturn(1);
        
        // 执行测试
        boolean result = goalService.deleteGoal(1L, 100L);
        
        // 验证结果
        assertTrue(result);
        verify(goalMapper, times(1)).deleteGoal(1L, 100L);
    }
    
    @Test
    void testGetRecentGoals() {
        // 准备测试数据
        List<Goal> goals = new ArrayList<>();
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setUserId(100L);
        goal1.setTitle("最新目标");
        goals.add(goal1);
        
        // 模拟Mapper行为
        when(goalMapper.getRecentGoals(100L, 5)).thenReturn(goals);
        
        // 执行测试
        List<Goal> result = goalService.getRecentGoals(100L, 5);
        
        // 验证结果
        assertEquals(1, result.size());
        assertEquals("最新目标", result.get(0).getTitle());
        verify(goalMapper, times(1)).getRecentGoals(100L, 5);
    }
    
    @Test
    void testGetTodayReminderGoals() {
        // 准备测试数据
        List<Goal> goals = new ArrayList<>();
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setUserId(100L);
        goal1.setTitle("今日目标");
        goals.add(goal1);
        
        // 模拟Mapper行为
        when(goalMapper.getTodayReminderGoals(100L, "2024-01-01")).thenReturn(goals);
        
        // 执行测试
        List<Goal> result = goalService.getTodayReminderGoals(100L, "2024-01-01");
        
        // 验证结果
        assertEquals(1, result.size());
        assertEquals("今日目标", result.get(0).getTitle());
        verify(goalMapper, times(1)).getTodayReminderGoals(100L, "2024-01-01");
    }
}