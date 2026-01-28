package com.crazydream.service;

import com.crazydream.entity.Achievement;
import com.crazydream.mapper.AchievementMapper;
import com.crazydream.mapper.GoalMapper;
import com.crazydream.service.impl.AchievementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AchievementServiceTest {

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private GoalMapper goalMapper;

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnlockAchievement() {
        // 准备测试数据
        Long userId = 100L;
        String achievementName = "FIRST_GOAL";
        Achievement mockAchievement = new Achievement();
        mockAchievement.setId(1L);
        mockAchievement.setName("第一步");
        mockAchievement.setDescription("创建第一个目标");
        mockAchievement.setIconUrl("/images/achievements/first_goal.png");
        mockAchievement.setUserId(userId);
        mockAchievement.setUnlockTime(LocalDateTime.now());
        mockAchievement.setCreateTime(LocalDateTime.now());
        mockAchievement.setUpdateTime(LocalDateTime.now());

        // 模拟Mapper行为
        when(achievementMapper.insertSelective(any(Achievement.class))).thenReturn(1);

        // 执行测试
        Achievement result = achievementService.unlockAchievement(userId, achievementName);

        // 验证结果
        assertNotNull(result);
        assertEquals("第一步", result.getName());
        assertEquals("创建第一个目标", result.getDescription());
        assertEquals(userId, result.getUserId());
        verify(achievementMapper, times(1)).insertSelective(any(Achievement.class));
    }

    @Test
    void testUnlockAchievementWithInvalidName() {
        // 准备测试数据
        Long userId = 100L;
        String invalidAchievementName = "INVALID_ACHIEVEMENT";

        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, () -> {
            achievementService.unlockAchievement(userId, invalidAchievementName);
        });
    }

    @Test
    void testGetUserAchievements() {
        // 准备测试数据
        Long userId = 100L;
        List<Achievement> achievements = new ArrayList<>();
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        achievement1.setName("第一步");
        achievements.add(achievement1);

        // 模拟Mapper行为
        when(achievementMapper.selectByUserId(userId)).thenReturn(achievements);

        // 执行测试
        List<Achievement> result = achievementService.getUserAchievements(userId);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("第一步", result.get(0).getName());
        verify(achievementMapper, times(1)).selectByUserId(userId);
    }

    @Test
    void testCheckAndUnlockGoalAchievements() {
        // 准备测试数据
        Long userId = 100L;

        // 设置目标统计
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("total", 6);
        statistics.put("completed", 4);

        // 设置已解锁成就
        List<Achievement> unlockedAchievements = new ArrayList<>();
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        achievement1.setName("第一步");
        unlockedAchievements.add(achievement1);

        // 模拟Mapper行为
        when(goalMapper.getGoalStatistics(userId)).thenReturn(statistics);
        when(achievementMapper.selectByUserId(userId)).thenReturn(unlockedAchievements);
        when(achievementMapper.insertSelective(any(Achievement.class))).thenReturn(1);

        // 执行测试
        achievementService.checkAndUnlockGoalAchievements(userId);

        // 验证结果（应该解锁5个目标、1个完成目标和3个完成目标的成就，共3个）
        verify(achievementMapper, times(3)).insertSelective(any(Achievement.class));
    }

    @Test
    void testCheckAndUnlockGoalAchievementsWithNoNewAchievements() {
        // 准备测试数据
        Long userId = 100L;

        // 设置目标统计（不满足新成就条件）
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("total", 1);
        statistics.put("completed", 0);

        // 设置已解锁成就（包含所有可能的成就）
        List<Achievement> unlockedAchievements = new ArrayList<>();
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        achievement1.setName("第一步");
        unlockedAchievements.add(achievement1);
        Achievement achievement2 = new Achievement();
        achievement2.setId(2L);
        achievement2.setName("目标达人");
        unlockedAchievements.add(achievement2);
        Achievement achievement3 = new Achievement();
        achievement3.setId(3L);
        achievement3.setName("成功第一步");
        unlockedAchievements.add(achievement3);
        Achievement achievement4 = new Achievement();
        achievement4.setId(4L);
        achievement4.setName("目标完成者");
        unlockedAchievements.add(achievement4);

        // 模拟Mapper行为
        when(goalMapper.getGoalStatistics(userId)).thenReturn(statistics);
        when(achievementMapper.selectByUserId(userId)).thenReturn(unlockedAchievements);

        // 执行测试
        achievementService.checkAndUnlockGoalAchievements(userId);

        // 验证结果（不应该解锁任何新成就）
        verify(achievementMapper, never()).insertSelective(any(Achievement.class));
    }
}