# Spec: Achievement System Enhancement

## Meta
- **Capability**: achievement-system-enhancement
- **Related Changes**: expand-achievement-types
- **Owner**: Backend Team
- **Status**: DRAFT

## Overview

扩展成就系统，从当前的6种基础成就类型扩展到20+种，涵盖目标完成、连续打卡、分类专注、效率提升和里程碑等多个维度，提升用户参与度和产品粘性。

## ADDED Requirements

### Requirement: ASE-001 目标完成数量成就扩展
**Priority**: P0  
**Category**: Feature

系统应支持更细粒度的目标完成数量成就，激励用户持续完成目标。 This requirement SHALL be enforced.

#### Scenario: 用户完成30个目标后解锁"初露锋芒"成就

**Given** 用户已完成29个目标  
**And** 用户的"初露锋芒"成就尚未解锁  
**When** 用户完成第30个目标  
**Then** 系统应自动解锁"初露锋芒"成就  
**And** 成就的 `unlocked` 字段应为 `true`  
**And** `unlocked_time` 应记录当前时间

#### Scenario: 用户完成200个目标后解锁"目标大师"成就

**Given** 用户已完成199个目标  
**When** 用户完成第200个目标  
**Then** 系统应自动解锁"目标大师"成就

---

### Requirement: ASE-002 连续打卡成就系列
**Priority**: P0  
**Category**: Feature

系统应支持连续打卡成就，激励用户养成每日完成目标的习惯。 This requirement SHALL be enforced.

#### Scenario: 用户连续3天完成目标解锁"三日坚持"成就

**Given** 用户在过去2天每天至少完成1个目标  
**When** 用户在第3天完成至少1个目标  
**Then** 系统应自动解锁"三日坚持"成就

#### Scenario: 用户中断连续打卡后重新计数

**Given** 用户已连续5天完成目标  
**When** 用户第6天未完成任何目标  
**And** 用户第7天完成1个目标  
**Then** 连续天数应从1重新开始计数  
**And** 之前的连续记录不应影响新的连续计数

#### Scenario: 用户连续100天完成目标解锁"百日传奇"成就

**Given** 用户已连续99天每天至少完成1个目标  
**When** 用户在第100天完成至少1个目标  
**Then** 系统应自动解锁"百日传奇"成就

---

### Requirement: ASE-003 分类专注成就系列
**Priority**: P1  
**Category**: Feature

系统应支持分类维度的成就，鼓励用户专注于特定领域的目标。 This requirement SHALL be enforced.

#### Scenario: 用户在单个分类完成10个目标解锁"分类达人"成就

**Given** 用户在"学习成长"分类已完成9个目标  
**When** 用户在"学习成长"分类完成第10个目标  
**Then** 系统应自动解锁"分类达人"成就

#### Scenario: 用户在所有分类都完成至少1个目标解锁"全能选手"成就

**Given** 系统有6个默认分类  
**And** 用户在5个分类中各完成了至少1个目标  
**When** 用户在最后1个分类中完成目标  
**Then** 系统应自动解锁"全能选手"成就

#### Scenario: 分类被删除不影响已完成目标计数

**Given** 用户在"学习成长"分类已完成8个目标  
**When** 管理员删除"学习成长"分类  
**Then** 用户在该分类的完成目标数应保持为8  
**And** 分类相关成就检查应忽略已删除的分类

---

### Requirement: ASE-004 时间维度效率成就
**Priority**: P1  
**Category**: Feature

系统应支持基于完成时间的成就，激励用户提升效率。 This requirement SHALL be enforced.

#### Scenario: 用户在早上6-8点完成5个目标解锁"早起鸟"成就

**Given** 用户在早上6:00-8:00之间已完成4个目标  
**When** 用户在早上7:30完成第5个目标  
**Then** 系统应自动解锁"早起鸟"成就

#### Scenario: 用户创建目标后24小时内完成累计10次解锁"效率达人"成就

**Given** 用户已有9次在创建目标后24小时内完成的记录  
**When** 用户创建新目标并在20小时后完成  
**Then** 系统应自动解锁"效率达人"成就

#### Scenario: 用户提前完成有截止日期的目标累计20次解锁"守时之星"成就

**Given** 用户已有19次提前完成有截止日期目标的记录  
**When** 用户提前3天完成一个有截止日期的目标  
**Then** 系统应自动解锁"守时之星"成就

---

### Requirement: ASE-005 使用时长里程碑成就
**Priority**: P2  
**Category**: Feature

系统应支持基于使用时长的里程碑成就，增强用户忠诚度。 This requirement SHALL be enforced.

#### Scenario: 用户使用系统满7天解锁"初入殿堂"成就

**Given** 用户注册日期为7天前  
**When** 系统执行每日成就检查任务  
**Or** 用户登录系统  
**Then** 系统应自动解锁"初入殿堂"成就

#### Scenario: 用户使用系统满365天解锁"年度坚持"成就

**Given** 用户注册日期为365天前  
**When** 系统执行每日成就检查任务  
**Then** 系统应自动解锁"年度坚持"成就

---

### Requirement: ASE-006 完成率成就
**Priority**: P2  
**Category**: Feature

系统应支持基于目标完成率的成就，激励用户提高目标完成质量。 This requirement SHALL be enforced.

#### Scenario: 用户完成率达到90%且完成20个目标解锁"完美主义者"成就

**Given** 用户总共创建了25个目标  
**And** 用户已完成22个目标（完成率88%）  
**And** 用户还有1个进行中的目标  
**When** 用户完成该进行中的目标（完成23个，完成率92%）  
**Then** 系统应自动解锁"完美主义者"成就

#### Scenario: 完成率计算不包括已放弃的目标

**Given** 用户创建了30个目标  
**And** 用户完成了20个目标  
**And** 用户放弃了5个目标  
**When** 系统计算完成率  
**Then** 完成率应为 20 / 25 = 80%  
**And** 已放弃的目标不应计入总目标数

---

### Requirement: ASE-007 成就统计数据收集
**Priority**: P0  
**Category**: Technical

系统应提供统一的成就统计数据收集服务，提升性能和可维护性。 This requirement SHALL be enforced.

#### Scenario: 一次性收集所有成就检查所需的统计数据

**Given** 用户ID为1  
**When** 系统调用 `AchievementStatisticsService.collectStatistics(userId)`  
**Then** 系统应返回包含以下数据的 `AchievementStatistics` 对象：
  - 总目标数
  - 已完成目标数
  - 连续完成天数
  - 各分类的完成目标数
  - 早起完成次数
  - 快速完成次数
  - 提前完成次数
  - 注册天数
  - 目标完成率
**And** 所有数据应通过批量查询获取，避免多次数据库往返

---

### Requirement: ASE-008 定时任务检查连续打卡成就
**Priority**: P1  
**Category**: Technical

系统应提供定时任务，每日自动检查所有用户的连续打卡和里程碑成就。 This requirement SHALL be enforced.

#### Scenario: 每日凌晨执行成就检查任务

**Given** 系统配置定时任务在每日00:00执行  
**When** 到达执行时间  
**Then** 系统应：
  - 分页查询所有活跃用户（每批100个）
  - 为每个用户检查连续打卡成就
  - 为每个用户检查使用天数里程碑成就
  - 记录执行日志
**And** 任务执行应有超时限制（最多10分钟）

#### Scenario: 定时任务执行失败应记录错误日志

**Given** 定时任务正在执行  
**When** 某个用户的成就检查抛出异常  
**Then** 系统应记录错误日志  
**And** 继续处理下一个用户  
**And** 不应中断整个任务

---

## ADDED Requirements

### Requirement: ASE-009 Achievement.canUnlock() 方法参数扩展
**Priority**: P0  
**Category**: Breaking Change

系统 SHALL 将 Achievement.canUnlock 的方法签名从旧形式调整为新的统计对象形式。

**Before**:
```java
public boolean canUnlock(int goalCount, int level)
```

**After**:
```java
public boolean canUnlock(AchievementStatistics statistics)
```

**Reason**: 现有的参数无法支持新增的复杂成就类型判断逻辑，需要传入完整的统计对象。 This requirement SHALL govern the new method signature.

#### Scenario: 使用统计对象进行成就解锁判断

**Given** 成就类型为"连续7天完成目标"  
**When** 调用 `achievement.canUnlock(statistics)`  
**And** `statistics.getConsecutiveDays()` 返回 7  
**Then** 方法应返回 `true`

---

### Requirement: ASE-010 AchievementApplicationService.checkAndUnlock() 简化
**Priority**: P0  
**Category**: API Change

系统 SHALL 将 AchievementApplicationService.checkAndUnlock 的方法签名从旧形式简化为仅基于 userId 的调用。

**Before**:
```java
public void checkAndUnlock(Long userId, int goalCount, int userLevel)
```

**After**:
```java
public void checkAndUnlock(Long userId)
```

**Reason**: 统计数据应由服务内部自动收集，无需调用方传入。 This requirement SHALL govern the new API contract.

#### Scenario: 简化的成就检查接口

**Given** 用户ID为1  
**When** 调用 `achievementService.checkAndUnlock(1L)`  
**Then** 系统应：
  - 自动收集用户的统计数据
  - 检查所有成就类型的解锁条件
  - 解锁符合条件的成就
  - 持久化到数据库

---

## REMOVED Requirements

无

---

## Non-Functional Requirements

### NFR-001: 成就检查性能
**Priority**: P0

单次完整的成就检查（包括统计数据收集和所有成就类型判断）应在100ms内完成。 This requirement SHALL be enforced.

### NFR-002: 数据库查询优化
**Priority**: P0

所有新增的统计查询应添加适当的数据库索引，确保查询性能。EXPLAIN分析应显示使用了索引。 This requirement SHALL be enforced.

### NFR-003: 测试覆盖率
**Priority**: P0

新增代码的测试覆盖率应达到80%以上，包括： This requirement SHALL be enforced.
- 单元测试：所有领域模型和应用服务
- 集成测试：Repository层统计方法
- 端到端测试：完整的成就解锁流程

### NFR-004: 向后兼容性
**Priority**: P0

虽然 `Achievement.canUnlock()` 方法签名发生变化，但应保证现有成就类型的解锁逻辑不受影响，已解锁的成就数据不应丢失或损坏。 This requirement SHALL be enforced.

---

## Dependencies

### Upstream Dependencies
- Goal领域模型和Repository
- User领域模型和Repository
- Category领域模型

### Downstream Dependencies
- 前端成就展示UI（不在本次范围）
- 推送通知服务（后续实现）

---

## Acceptance Criteria

- [ ] 所有15+种新成就类型在 `AchievementType` 枚举中定义
- [ ] `Achievement.canUnlock()` 方法支持所有新成就类型
- [ ] 所有Repository统计方法实现并通过集成测试
- [ ] 定时任务正常执行，日志记录完整
- [ ] 单次成就检查响应时间 < 100ms
- [ ] 测试覆盖率 >= 80%
- [ ] 数据初始化脚本执行成功
- [ ] 现有用户成就状态补偿完成
- [ ] 生产环境部署无回滚

---

## Migration Strategy

### 数据迁移步骤

1. **Step 1**: 在测试环境执行数据初始化脚本
   - 为所有现有用户创建新成就记录（未解锁状态）
   - 验证数据完整性

2. **Step 2**: 执行成就状态补偿脚本
   - 批量检查现有用户的成就解锁条件
   - 更新符合条件的成就为已解锁状态
   - 记录补偿日志

3. **Step 3**: 生产环境执行
   - 选择低峰时段（凌晨2-4点）
   - 执行数据迁移脚本
   - 监控系统性能指标

4. **Step 4**: 验证和监控
   - 抽样验证成就数据正确性
   - 监控错误日志
   - 收集用户反馈

---

## Rollback Plan

如果生产环境出现严重问题，回滚步骤如下：

1. 立即停止定时任务
2. 回滚代码到上一个稳定版本
3. 评估是否需要回滚数据（通常新增成就数据无需回滚）
4. 通知用户暂时停用新成就功能
5. 分析问题根因，修复后重新发布

---

## References

- [Achievement Domain Model](file:///Users/zhoufeng/Documents/trae_projects/crazydream/src/main/java/com/crazydream/domain/achievement/model/aggregate/Achievement.java)
- [AchievementType Enum](file:///Users/zhoufeng/Documents/trae_projects/crazydream/src/main/java/com/crazydream/domain/achievement/model/valueobject/AchievementType.java)
- [Design Document](../design.md)
- [Task Breakdown](../tasks.md)
