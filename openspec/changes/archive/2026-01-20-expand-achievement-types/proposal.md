# Proposal: expand-achievement-types

## Status
**DRAFT** - Awaiting review

## Context

当前成就系统只包含6种基础成就类型，主要集中在目标完成数量和等级提升方面。为了提升用户参与度和激励效果，需要扩展更多维度的成就类型，包括：

- 时间维度成就（连续打卡、早起完成等）
- 分类专注成就（专注于特定分类）
- 效率成就（快速完成、提前完成等）
- 社交维度成就（分享、协作等，预留扩展）
- 里程碑成就（特殊节日、重要时刻等）

**现有成就类型**：
1. FIRST_GOAL - 首个目标
2. GOAL_COMPLETED_10 - 完成10个目标
3. GOAL_COMPLETED_50 - 完成50个目标
4. GOAL_COMPLETED_100 - 完成100个目标
5. CONSECUTIVE_7 - 连续7天完成目标（已定义但未实现）
6. LEVEL_UP - 等级提升（已定义但未实现）

## Why

1. **增强用户粘性**：更多元化的成就可以激励不同类型的用户
2. **提升完成率**：多维度的成就目标能够引导用户更好地使用系统
3. **增加趣味性**：丰富的成就系统使产品更有吸引力
4. **数据积累**：为后续用户行为分析提供更多维度的数据

## Goals

1. 新增至少10种不同维度的成就类型
2. 实现成就解锁条件的自动检查逻辑
3. 支持成就的徽章图标和描述
4. 保持系统扩展性，便于未来新增更多成就

## Non-Goals

1. 不涉及成就的社交分享功能（后续单独实现）
2. 不涉及成就的积分兑换系统
3. 不涉及成就的推送通知（后续单独实现）
4. 不涉及前端UI展示逻辑

## What Changes

### 新增成就类型

#### 1. 目标完成数量系列（补充）
- **GOAL_COMPLETED_30** - 初露锋芒（完成30个目标）
- **GOAL_COMPLETED_200** - 目标大师（完成200个目标）

#### 2. 连续打卡系列
- **CONSECUTIVE_3** - 三日坚持（连续3天完成目标）
- **CONSECUTIVE_14** - 两周习惯（连续14天完成目标）
- **CONSECUTIVE_30** - 月度冠军（连续30天完成目标）
- **CONSECUTIVE_100** - 百日传奇（连续100天完成目标）

#### 3. 分类专注系列
- **CATEGORY_MASTER_10** - 分类达人（在单个分类完成10个目标）
- **CATEGORY_MASTER_30** - 分类专家（在单个分类完成30个目标）
- **ALL_CATEGORY_EXPLORER** - 全能选手（在所有分类都至少完成1个目标）

#### 4. 效率提升系列
- **EARLY_BIRD** - 早起鸟（早上6-8点完成5个目标）
- **NIGHT_OWL** - 夜猫子（晚上22-24点完成5个目标）
- **SPEED_MASTER** - 效率达人（创建目标后24小时内完成，累计10次）
- **DEADLINE_KEEPER** - 守时之星（提前完成有截止日期的目标，累计20次）

#### 5. 里程碑系列
- **FIRST_WEEK** - 初入殿堂（使用系统满7天）
- **FIRST_MONTH** - 月度会员（使用系统满30天）
- **ONE_YEAR** - 年度坚持（使用系统满365天）
- **HIGH_COMPLETION_RATE** - 完美主义者（目标完成率达到90%，且完成目标数>=20）

### 技术实现方案

#### 1. 领域模型扩展
- 扩展 `AchievementType` 枚举，添加新的成就类型
- 重构 `Achievement.canUnlock()` 方法，支持更复杂的解锁条件判断
- 新增专门的成就条件检查器（AchievementConditionChecker）

#### 2. 数据统计支持
- 在 GoalRepository 中新增统计方法：
  - `countConsecutiveDays(userId)` - 统计连续完成天数
  - `countByCategoryId(userId, categoryId)` - 统计分类目标数
  - `countCompletedCategoriesCount(userId)` - 统计已完成目标的分类数
  - `countEarlyCompletedGoals(userId)` - 统计提前完成的目标数
  - `countTimeRangeCompletedGoals(userId, startHour, endHour)` - 统计时间段内完成的目标数
  - `getUserRegistrationDays(userId)` - 统计用户注册天数
  - `getCompletionRate(userId)` - 计算目标完成率

#### 3. 成就检查触发点
- 目标完成时（已实现）
- 目标创建时（已实现）
- 每日定时任务（检查连续打卡、使用天数等）
- 用户登录时（检查使用天数里程碑）

### 数据库变更

无需变更数据库表结构，现有的 `achievement` 表已支持所有必要字段。

### API变更

无需新增API接口，现有的成就接口已足够：
- `GET /api/v2/achievements` - 获取用户所有成就
- `GET /api/v2/achievements/unlocked` - 获取已解锁成就

## Alternatives Considered

### 方案A：基于规则引擎的成就系统
**优点**：
- 高度灵活，可通过配置文件动态添加成就
- 支持复杂的条件组合

**缺点**：
- 增加系统复杂度
- 性能开销较大
- 当前阶段过度设计

**决策**：不采用，当前阶段硬编码的方式更简单直接

### 方案B：成就分级系统（青铜、白银、黄金）
**优点**：
- 增加成就的层次感
- 提升用户挑战性

**缺点**：
- UI展示复杂度增加
- 数据库结构需要调整

**决策**：后续迭代考虑

## Risks

1. **性能风险**：频繁的统计查询可能影响性能
   - **缓解措施**：引入Redis缓存统计数据，定时更新

2. **连续打卡计算复杂度**：需要准确计算连续天数
   - **缓解措施**：维护用户的最后完成日期和连续天数字段

3. **时区问题**：不同时区用户的"一天"定义不同
   - **缓解措施**：使用用户本地时间，存储时转换为UTC

## Dependencies

- 依赖现有的 Goal 领域模型
- 依赖 GoalRepository 的统计功能扩展
- 依赖 AchievementApplicationService 的检查逻辑

## Timeline

- **Phase 1** (2天): 扩展成就类型定义，完善领域模型
- **Phase 2** (3天): 实现Repository统计方法和SQL查询
- **Phase 3** (2天): 实现成就解锁条件检查逻辑
- **Phase 4** (1天): 单元测试和集成测试
- **Phase 5** (1天): 数据初始化和验证

**总计**: 约9个工作日

## Success Metrics

1. 成功新增至少15种成就类型
2. 成就解锁准确率达到100%（通过单元测试验证）
3. 成就检查平均响应时间 < 100ms
4. 代码测试覆盖率 >= 80%

## Open Questions

1. **Q: 连续打卡成就是否需要考虑补签机制？**
   - A: 第一版不支持补签，严格按照连续天数计算

2. **Q: 是否需要为成就添加经验值或积分奖励？**
   - A: 当前版本不涉及，预留扩展接口

3. **Q: 成就解锁后是否需要通知用户？**
   - A: 通知功能在后续版本实现，当前只记录解锁状态

4. **Q: 分类相关成就如何处理分类被删除的情况？**
   - A: 已完成的目标计数不受分类删除影响，仍然计入统计

## Approval

- [ ] Tech Lead Review
- [ ] Product Owner Approval
- [ ] Architecture Review
