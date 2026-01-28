package com.crazydream.service;

import com.crazydream.entity.SubGoal;
import com.crazydream.mapper.SubGoalMapper;
import com.crazydream.service.impl.SubGoalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubGoalServiceTest {

    @Mock
    private SubGoalMapper subGoalMapper;

    @InjectMocks
    private SubGoalServiceImpl subGoalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubGoal() {
        // 准备测试数据
        SubGoal subGoal = new SubGoal();
        subGoal.setTitle("学习Spring Boot");
        subGoal.setGoalId(1L);
        subGoal.setId(1L);

        // 模拟Mapper行为
        when(subGoalMapper.insertSelective(subGoal)).thenReturn(1);
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(subGoal);

        // 执行测试
        SubGoal result = subGoalService.createSubGoal(subGoal);

        // 验证结果
        assertNotNull(result);
        assertEquals("学习Spring Boot", result.getTitle());
        assertEquals(1L, result.getGoalId());
        verify(subGoalMapper, times(1)).insertSelective(subGoal);
        verify(subGoalMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    void testCreateSubGoalWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.createSubGoal(null);
        });
    }

    @Test
    void testGetSubGoalById() {
        // 准备测试数据
        SubGoal subGoal = new SubGoal();
        subGoal.setId(1L);
        subGoal.setTitle("学习Spring Boot");

        // 模拟Mapper行为
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(subGoal);

        // 执行测试
        SubGoal result = subGoalService.getSubGoalById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("学习Spring Boot", result.getTitle());
        verify(subGoalMapper, times(1)).selectByPrimaryKey(1L);
    }

    @Test
    void testGetSubGoalByIdWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.getSubGoalById(null);
        });
    }

    @Test
    void testUpdateSubGoal() {
        // 准备测试数据
        SubGoal existingSubGoal = new SubGoal();
        existingSubGoal.setId(1L);
        existingSubGoal.setTitle("学习Spring Boot");

        SubGoal updatedSubGoal = new SubGoal();
        updatedSubGoal.setId(1L);
        updatedSubGoal.setTitle("学习Spring Boot 3");
        updatedSubGoal.setProgress(50);

        // 模拟Mapper行为
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(existingSubGoal);
        when(subGoalMapper.updateByPrimaryKeySelective(updatedSubGoal)).thenReturn(1);
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(updatedSubGoal);

        // 执行测试
        SubGoal result = subGoalService.updateSubGoal(updatedSubGoal);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("学习Spring Boot 3", result.getTitle());
        assertEquals(50, result.getProgress());
        verify(subGoalMapper, times(2)).selectByPrimaryKey(1L);
        verify(subGoalMapper, times(1)).updateByPrimaryKeySelective(updatedSubGoal);
    }

    @Test
    void testUpdateSubGoalWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.updateSubGoal(null);
        });

        // 测试更新子目标ID为空
        SubGoal subGoal = new SubGoal();
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.updateSubGoal(subGoal);
        });
    }

    @Test
    void testUpdateSubGoalWithNonExistent() {
        // 准备测试数据
        SubGoal nonExistentSubGoal = new SubGoal();
        nonExistentSubGoal.setId(1L);
        nonExistentSubGoal.setTitle("不存在的子目标");

        // 模拟Mapper行为
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(null);

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.updateSubGoal(nonExistentSubGoal);
        });
    }

    @Test
    void testDeleteSubGoal() {
        // 准备测试数据
        SubGoal existingSubGoal = new SubGoal();
        existingSubGoal.setId(1L);
        existingSubGoal.setTitle("学习Spring Boot");

        // 模拟Mapper行为
        when(subGoalMapper.selectByPrimaryKey(1L)).thenReturn(existingSubGoal);
        when(subGoalMapper.deleteByPrimaryKey(1L)).thenReturn(1);

        // 执行测试
        boolean result = subGoalService.deleteSubGoal(1L);

        // 验证结果
        assertTrue(result);
        verify(subGoalMapper, times(1)).selectByPrimaryKey(1L);
        verify(subGoalMapper, times(1)).deleteByPrimaryKey(1L);
    }

    @Test
    void testDeleteSubGoalWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.deleteSubGoal(null);
        });
    }

    @Test
    void testDeleteSubGoalWithNonExistent() {
        // 准备测试数据
        Long nonExistentId = 1L;

        // 模拟Mapper行为
        when(subGoalMapper.selectByPrimaryKey(nonExistentId)).thenReturn(null);

        // 执行测试
        boolean result = subGoalService.deleteSubGoal(nonExistentId);

        // 验证结果
        assertFalse(result);
        verify(subGoalMapper, times(1)).selectByPrimaryKey(nonExistentId);
        verify(subGoalMapper, never()).deleteByPrimaryKey(nonExistentId);
    }

    @Test
    void testGetSubGoalsByGoalId() {
        // 准备测试数据
        Long goalId = 1L;
        List<SubGoal> subGoals = new ArrayList<>();
        SubGoal subGoal1 = new SubGoal();
        subGoal1.setId(1L);
        subGoal1.setTitle("学习Spring Boot");
        subGoal1.setGoalId(goalId);
        subGoals.add(subGoal1);

        // 模拟Mapper行为
        when(subGoalMapper.selectByGoalId(goalId)).thenReturn(subGoals);

        // 执行测试
        List<SubGoal> result = subGoalService.getSubGoalsByGoalId(goalId);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals(goalId, result.get(0).getGoalId());
        verify(subGoalMapper, times(1)).selectByGoalId(goalId);
    }

    @Test
    void testGetSubGoalsByGoalIdWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.getSubGoalsByGoalId(null);
        });
    }

    @Test
    void testBatchDeleteSubGoals() {
        // 准备测试数据
        List<Long> subGoalIds = List.of(1L, 2L, 3L);

        // 模拟Mapper行为
        when(subGoalMapper.batchDelete(subGoalIds)).thenReturn(3);

        // 执行测试
        int result = subGoalService.batchDeleteSubGoals(subGoalIds);

        // 验证结果
        assertEquals(3, result);
        verify(subGoalMapper, times(1)).batchDelete(subGoalIds);
    }

    @Test
    void testBatchDeleteSubGoalsWithNull() {
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.batchDeleteSubGoals(null);
        });

        // 测试批量删除空子目标列表
        assertThrows(IllegalArgumentException.class, () -> {
            subGoalService.batchDeleteSubGoals(new ArrayList<>());
        });
    }
}