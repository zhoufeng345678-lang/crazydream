# Design: expand-achievement-types

## Architecture Overview

### Current State

当前成就系统采用简单的枚举+硬编码判断逻辑，架构如下：

```
┌─────────────────────────────────────────────────────────────┐
│                     Interface Layer                         │
│  AchievementController: GET /achievements, /unlocked        │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                   Application Layer                         │
│  AchievementApplicationService:                             │
│  - getUserAchievements()                                    │
│  - checkAndUnlock(userId, goalCount, userLevel)             │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Domain Layer                             │
│  Achievement (Aggregate):                                   │
│  - canUnlock(goalCount, level): boolean                     │
│  - unlock(): void                                           │
│                                                             │
│  AchievementType (Enum):                                    │
│  - FIRST_GOAL, GOAL_10, GOAL_50, GOAL_100, ...             │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                 Infrastructure Layer                        │
│  AchievementRepository: findByUserId, save                  │
└─────────────────────────────────────────────────────────────┘
```

**局限性**：
1. `canUnlock()` 方法参数固定，难以扩展
2. 统计数据分散在不同地方查询，性能差
3. 缺少统一的统计数据收集机制
4. 没有定时任务支持连续打卡等时间相关成就

### Target State

改进后的架构增加统计服务层和定时任务：

```
┌─────────────────────────────────────────────────────────────┐
│                     Interface Layer                         │
│  AchievementController: GET /achievements, /unlocked        │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                   Application Layer                         │
│  AchievementApplicationService:                             │
│  - getUserAchievements()                                    │
│  - checkAndUnlock(userId): void  ←── 简化接口                │
│                                                             │
│  AchievementStatisticsService: ←── 新增                      │
│  - collectStatistics(userId): AchievementStatistics         │
│                                                             │
│  AchievementScheduledTask: ←── 新增                          │
│  - checkDailyAchievements(): void                           │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    Domain Layer                             │
│  Achievement (Aggregate):                                   │
│  - canUnlock(statistics): boolean  ←── 参数改为统计对象       │
│  - unlock(): void                                           │
│                                                             │
│  AchievementType (Enum): ←── 扩展                            │
│  - 15+ achievement types                                    │
│                                                             │
│  AchievementStatistics (VO): ←── 新增                        │
│  - goalCount, consecutiveDays, categoryStats, etc.          │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                 Infrastructure Layer                        │
│  AchievementRepository: findByUserId, save                  │
│  GoalRepository: ←── 扩展统计方法                             │
│  - countConsecutiveDays()                                   │
│  - countByCategoryId()                                      │
│  - countTimeRangeCompletedGoals()                           │
│  - getCompletionRate()                                      │
│  UserRepository: ←── 扩展                                     │
│  - getRegistrationDays()                                    │
└─────────────────────────────────────────────────────────────┘
```

## Key Design Decisions

### 1. 统计数据对象 (AchievementStatistics)

**决策**: 创建专门的值对象封装所有成就检查需要的统计数据

**理由**:
- 减少数据库查询次数（一次性查询所有数据）
- 解耦成就检查逻辑和数据查询逻辑
- 便于测试（可以Mock统计数据）
- 提升性能（批量查询比多次查询更高效）

**设计**:
```java
public class AchievementStatistics {
    // 目标统计
    private final int totalGoals;
    private final int completedGoals;
    
    // 连续打卡统计
    private final int consecutiveDays;
    private final int maxConsecutiveDays;
    
    // 分类统计
    private final Map<Long, Integer> categoryGoalCounts;  // categoryId -> count
    private final int completedCategoriesCount;
    
    // 时间维度统计
    private final int earlyBirdCount;      // 早上6-8点完成的目标数
    private final int nightOwlCount;       // 晚上22-24点完成的目标数
    private final int speedMasterCount;    // 24小时内完成的目标数
    private final int deadlineKeeperCount; // 提前完成的目标数
    
    // 里程碑统计
    private final int registrationDays;
    private final double completionRate;
    
    // 构造函数和Getter方法...
}
```

### 2. 成就检查触发机制

**决策**: 采用混合触发机制

**触发点**:
1. **实时触发**: 目标创建、目标完成时立即检查
   - 优点: 及时反馈，用户体验好
   - 适用: 目标数量相关成就、分类成就

2. **定时触发**: 每日凌晨检查连续打卡和里程碑成就
   - 优点: 减少实时计算压力，统一处理
   - 适用: 连续天数成就、注册天数成就

3. **登录触发**: 用户登录时检查使用天数里程碑
   - 优点: 及时提示，增强用户粘性
   - 适用: 里程碑成就

**定时任务设计**:
```java
@Scheduled(cron = "0 0 0 * * ?")  // 每日凌晨0点
public void checkDailyAchievements() {
    // 1. 分页查询活跃用户
    // 2. 批量检查连续打卡成就
    // 3. 批量检查里程碑成就
    // 4. 记录日志
}
```

### 3. 连续打卡计算策略

**挑战**: 如何高效计算用户的连续完成天数？

**方案A - 实时计算**（已选择）:
- 每次查询时，从数据库查询完成目标的日期序列
- 算法计算最长连续天数
- 优点: 实现简单，数据准确
- 缺点: 每次都需要查询和计算

**方案B - 冗余字段**:
- 在User表增加 `consecutive_days` 字段
- 每次完成目标时更新该字段
- 优点: 查询性能高
- 缺点: 数据可能不一致，需要补偿机制

**决策**: 第一版使用方案A，后续优化可改为方案B

**SQL设计**:
```sql
SELECT
  completion_date,
  ROW_NUMBER() OVER (ORDER BY completion_date) - 
  DATEDIFF(completion_date, (SELECT MIN(update_time) FROM goal WHERE user_id = ? AND status = 'completed')) AS grp
FROM (
  SELECT DISTINCT DATE(update_time) AS completion_date
  FROM goal
  WHERE user_id = ? AND status = 'completed'
  ORDER BY completion_date
) AS dates
GROUP BY grp
ORDER BY COUNT(*) DESC
LIMIT 1;
```

### 4. 分类成就实现

**场景**: 用户在"学习成长"分类完成30个目标，解锁"分类专家"成就

**设计**:
1. 统计每个分类的完成目标数
2. 检查是否有任一分类达到阈值
3. 记录具体是哪个分类达成

**数据结构**:
```java
// 统计数据
Map<Long, Integer> categoryGoalCounts;

// 检查逻辑
public boolean canUnlock(AchievementStatistics stats) {
    if (type == CATEGORY_MASTER_30) {
        return stats.getCategoryGoalCounts().values().stream()
            .anyMatch(count -> count >= 30);
    }
    // ...
}
```

**扩展性考虑**: 未来可能需要记录具体是哪个分类达成，可以在Achievement实体中增加 `metadata` JSON字段存储额外信息。

### 5. 时间范围成就实现

**场景**: 早起鸟 - 在早上6-8点完成5个目标

**实现方式**:
```java
// Repository方法
int countTimeRangeCompletedGoals(UserId userId, int startHour, int endHour);

// SQL
SELECT COUNT(*)
FROM goal
WHERE user_id = ?
  AND status = 'completed'
  AND HOUR(update_time) BETWEEN ? AND ?
```

**时区处理**: 
- 存储时使用UTC时间
- 查询时根据用户时区转换
- 未来可在User表增加 `timezone` 字段

### 6. 快速完成成就（24小时内完成）

**实现**:
```sql
SELECT COUNT(*)
FROM goal
WHERE user_id = ?
  AND status = 'completed'
  AND TIMESTAMPDIFF(HOUR, create_time, update_time) <= 24
```

## Data Flow

### 成就检查完整流程

```
┌──────────────┐
│ Goal完成事件  │
└──────┬───────┘
       │
       ▼
┌──────────────────────────────┐
│ GoalApplicationService       │
│ - completeGoal()             │
│ - 触发成就检查                │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ AchievementStatisticsService │
│ - collectStatistics(userId)  │
│ - 查询所有统计数据             │
└──────┬───────────────────────┘
       │
       ├──→ GoalRepository.countCompletedByUserId()
       ├──→ GoalRepository.countConsecutiveDays()
       ├──→ GoalRepository.countByCategoryId()
       ├──→ GoalRepository.countTimeRangeCompletedGoals()
       ├──→ GoalRepository.getCompletionRate()
       └──→ UserRepository.getRegistrationDays()
       │
       ▼
┌──────────────────────────────┐
│ AchievementStatistics        │
│ - 封装所有统计数据             │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ AchievementApplicationService│
│ - checkAndUnlock(userId)     │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ Achievement.canUnlock()      │
│ - 逐个检查成就解锁条件         │
└──────┬───────────────────────┘
       │ (满足条件)
       ▼
┌──────────────────────────────┐
│ Achievement.unlock()         │
│ - 更新解锁状态                │
└──────┬───────────────────────┘
       │
       ▼
┌──────────────────────────────┐
│ AchievementRepository.save() │
│ - 持久化到数据库               │
└──────────────────────────────┘
```

## Performance Considerations

### 1. 批量统计查询优化

**问题**: 一次成就检查需要执行多个SQL查询

**优化方案**:
- 合并部分查询，减少数据库往返
- 使用Redis缓存统计结果（TTL: 5分钟）
- 异步执行非关键成就检查

**缓存策略**:
```
Key: achievement:stats:{userId}
Value: AchievementStatistics JSON
TTL: 5分钟
```

### 2. 定时任务性能

**问题**: 每日凌晨批量检查所有用户可能造成性能峰值

**优化方案**:
- 分页查询用户（每次100个）
- 错峰执行（不同用户组在不同时间）
- 设置超时机制，避免长时间阻塞

### 3. 数据库索引

**新增索引**:
```sql
-- 连续天数查询
CREATE INDEX idx_goal_user_status_date 
ON goal(user_id, status, DATE(update_time));

-- 时间范围查询
CREATE INDEX idx_goal_user_status_hour 
ON goal(user_id, status, HOUR(update_time));

-- 快速完成查询
CREATE INDEX idx_goal_user_times 
ON goal(user_id, status, create_time, update_time);

-- 分类统计
CREATE INDEX idx_goal_user_category_status 
ON goal(user_id, category_id, status);
```

## Testing Strategy

### 1. 单元测试

**Achievement领域模型测试**:
```java
@Test
public void testCanUnlock_ConsecutiveDays() {
    Achievement achievement = Achievement.create(userId, CONSECUTIVE_7);
    AchievementStatistics stats = new AchievementStatistics.Builder()
        .consecutiveDays(7)
        .build();
    
    assertTrue(achievement.canUnlock(stats));
}

@Test
public void testCanUnlock_AlmostConsecutiveDays() {
    Achievement achievement = Achievement.create(userId, CONSECUTIVE_7);
    AchievementStatistics stats = new AchievementStatistics.Builder()
        .consecutiveDays(6)
        .build();
    
    assertFalse(achievement.canUnlock(stats));
}
```

### 2. 集成测试

**完整流程测试**:
```java
@Test
@Transactional
public void testAchievementUnlock_WhenGoalCompleted() {
    // 1. 创建用户和10个目标
    User user = createTestUser();
    List<Goal> goals = createTestGoals(user, 10);
    
    // 2. 完成10个目标
    goals.forEach(goal -> goalService.completeGoal(goal.getId(), user.getId()));
    
    // 3. 验证"小有成就"成就已解锁
    List<Achievement> achievements = achievementService.getUserAchievements(user.getId());
    assertTrue(achievements.stream()
        .filter(a -> a.getType() == GOAL_COMPLETED_10)
        .findFirst()
        .map(Achievement::isUnlocked)
        .orElse(false));
}
```

### 3. 性能测试

**统计查询性能**:
- 目标: 单次统计查询 < 50ms
- 目标: 完整成就检查 < 100ms
- 场景: 1000个目标的用户

## Rollout Plan

### Phase 1: 灰度发布
- 选择10%用户开启新成就功能
- 监控性能指标和错误日志
- 收集用户反馈

### Phase 2: 逐步扩大
- 扩大到50%用户
- 观察3天稳定性

### Phase 3: 全量发布
- 开放给所有用户
- 执行数据补偿脚本

## Monitoring

**关键指标**:
1. 成就检查平均耗时
2. 成就解锁数量（按类型统计）
3. 数据库查询耗时
4. 定时任务执行时长
5. 错误率

**告警阈值**:
- 成就检查耗时 > 200ms
- 错误率 > 1%
- 定时任务执行超时 > 10分钟

## Future Enhancements

1. **成就分级系统**: 青铜、白银、黄金
2. **成就推送通知**: 解锁时推送给用户
3. **成就分享**: 社交媒体分享
4. **成就排行榜**: 展示用户成就数量排名
5. **自定义成就**: 用户可以创建个人成就
