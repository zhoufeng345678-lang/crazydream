# Tasks: expand-achievement-types

## Overview
实现成就系统的扩展，新增15+种不同维度的成就类型，并完善成就解锁的自动检查逻辑。

## Task Breakdown

### Phase 1: 领域模型扩展 (2天)

#### Task 1.1: 扩展 AchievementType 枚举
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.5天  
**Dependencies**: 无

**Description**:
- 在 `AchievementType.java` 中添加新的成就类型定义
- 包括：目标完成数量系列、连续打卡系列、分类专注系列、效率提升系列、里程碑系列
- 每个成就类型包含：code、name、description

**Validation**:
- 编译通过
- 所有现有测试通过
- 代码review通过

---

#### Task 1.2: 重构 Achievement 领域模型
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 1天  
**Dependencies**: Task 1.1

**Description**:
- 重构 `Achievement.canUnlock()` 方法，支持新的成就类型判断
- 新增成就条件检查所需的参数（连续天数、分类统计、时间范围统计等）
- 考虑方法签名的扩展性，避免频繁修改接口

**Validation**:
- 单元测试覆盖所有成就类型的解锁条件
- 边界条件测试（如恰好达到条件、差一点达到条件）
- 性能测试：单次检查 < 10ms

---

#### Task 1.3: 创建成就统计数据DTO
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.5天  
**Dependencies**: Task 1.2

**Description**:
- 创建 `AchievementStatistics` DTO，封装成就检查所需的所有统计数据
- 包括：目标总数、完成数、连续天数、分类统计、时间范围统计、注册天数、完成率等
- 方便一次性查询所有统计数据，减少数据库查询次数

**Validation**:
- DTO结构清晰，字段命名符合规范
- 包含必要的构造方法和Getter方法

---

### Phase 2: Repository层统计方法 (3天)

#### Task 2.1: GoalRepository 接口扩展
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.5天  
**Dependencies**: Task 1.3

**Description**:
- 在 `GoalRepository` 接口中添加新的统计方法
- 方法列表：
  - `countConsecutiveDays(UserId userId)`
  - `countByCategoryId(UserId userId, CategoryId categoryId)`
  - `countCompletedCategoriesCount(UserId userId)`
  - `countEarlyCompletedGoals(UserId userId)`
  - `countTimeRangeCompletedGoals(UserId userId, int startHour, int endHour)`
  - `getCompletionRate(UserId userId)`

**Validation**:
- 接口方法签名清晰
- JavaDoc注释完整

---

#### Task 2.2: Repository 实现类开发
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 1天  
**Dependencies**: Task 2.1

**Description**:
- 在 `GoalRepositoryImpl` 中实现新增的统计方法
- 调用 MyBatis Mapper 进行数据库查询
- 考虑性能优化，避免全表扫描

**Validation**:
- 单元测试覆盖所有统计方法
- 集成测试验证数据准确性
- 性能测试：单个查询 < 50ms

---

#### Task 2.3: MyBatis Mapper XML 开发
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 1天  
**Dependencies**: Task 2.2

**Description**:
- 在 `GoalPersistenceMapper.xml` 中编写SQL查询
- 重点SQL：
  - 连续天数计算（需要查询完成目标的日期序列，计算最长连续天数）
  - 分类统计（GROUP BY category_id）
  - 时间范围统计（WHERE HOUR(update_time) BETWEEN ? AND ?）
  - 完成率计算（completed_count / total_count）
- 确保SQL性能良好，添加必要的索引

**Validation**:
- SQL在测试数据库上执行正确
- 使用EXPLAIN分析查询计划，确保使用了索引
- 边界测试：空数据、大量数据场景

---

#### Task 2.4: UserRepository 扩展（注册天数）
**Owner**: Backend Developer  
**Priority**: P1  
**Estimate**: 0.5天  
**Dependencies**: 无

**Description**:
- 在 `UserRepository` 中添加 `getRegistrationDays(UserId userId)` 方法
- 计算用户从注册到现在的天数
- 考虑时区处理

**Validation**:
- 单元测试验证天数计算准确
- 测试不同时区的场景

---

### Phase 3: 成就检查逻辑实现 (2天)

#### Task 3.1: 创建 AchievementStatisticsService
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.5天  
**Dependencies**: Task 2.4

**Description**:
- 创建 `AchievementStatisticsService`，负责收集成就检查所需的所有统计数据
- 方法：`AchievementStatistics collectStatistics(UserId userId)`
- 一次性查询所有需要的统计数据，返回 `AchievementStatistics` DTO

**Validation**:
- 单元测试验证数据收集完整性
- Mock测试验证Repository调用

---

#### Task 3.2: 重构 AchievementApplicationService
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 1天  
**Dependencies**: Task 3.1

**Description**:
- 修改 `checkAndUnlock()` 方法，使用 `AchievementStatistics` 进行检查
- 调用 `Achievement.canUnlock()` 时传入完整的统计数据
- 优化检查逻辑，批量处理所有成就类型

**Validation**:
- 集成测试验证成就解锁逻辑
- 测试各种成就类型的解锁场景
- 性能测试：单次检查所有成就 < 100ms

---

#### Task 3.3: 添加定时任务（连续打卡检查）
**Owner**: Backend Developer  
**Priority**: P1  
**Estimate**: 0.5天  
**Dependencies**: Task 3.2

**Description**:
- 创建 `AchievementScheduledTask`，每日凌晨执行
- 检查所有活跃用户的连续打卡成就
- 使用分页查询，避免一次性加载过多用户

**Validation**:
- 手动触发定时任务验证功能
- 测试大量用户场景的性能
- 日志记录完整

---

### Phase 4: 单元测试和集成测试 (1天)

#### Task 4.1: 领域模型单元测试
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.3天  
**Dependencies**: Task 1.2

**Description**:
- 为 `Achievement` 类编写完整的单元测试
- 覆盖所有成就类型的 `canUnlock()` 方法
- 测试边界条件和异常情况

**Validation**:
- 测试覆盖率 >= 90%
- 所有测试通过

---

#### Task 4.2: Repository 层集成测试
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.4天  
**Dependencies**: Task 2.4

**Description**:
- 为所有新增的统计方法编写集成测试
- 使用H2内存数据库或测试数据库
- 准备测试数据，验证查询结果

**Validation**:
- 所有集成测试通过
- 测试数据准备脚本完整

---

#### Task 4.3: 应用层集成测试
**Owner**: Backend Developer  
**Priority**: P0  
**Estimate**: 0.3天  
**Dependencies**: Task 3.3

**Description**:
- 为 `AchievementApplicationService` 编写集成测试
- 模拟各种场景，验证成就解锁的完整流程
- 测试多个成就同时解锁的情况

**Validation**:
- 所有场景测试通过
- 事务回滚测试验证

---

### Phase 5: 数据初始化和验证 (1天)

#### Task 5.1: 创建成就数据初始化脚本
**Owner**: Backend Developer  
**Priority**: P1  
**Estimate**: 0.3天  
**Dependencies**: Task 1.1

**Description**:
- 在 `schema.sql` 中添加新成就类型的初始化数据
- 为所有现有用户创建新的成就记录（未解锁状态）
- 编写数据迁移脚本

**Validation**:
- 在测试环境执行脚本，验证数据正确性
- 检查外键约束和数据完整性

---

#### Task 5.2: 现有用户成就状态补偿
**Owner**: Backend Developer  
**Priority**: P1  
**Estimate**: 0.4天  
**Dependencies**: Task 5.1

**Description**:
- 编写补偿脚本，为现有用户检查并解锁应该解锁的成就
- 批量处理，避免一次性查询过多数据
- 记录补偿日志

**Validation**:
- 在测试环境验证补偿逻辑
- 检查补偿后的成就状态正确性

---

#### Task 5.3: 生产环境部署验证
**Owner**: Backend Developer + DevOps  
**Priority**: P0  
**Estimate**: 0.3天  
**Dependencies**: Task 5.2

**Description**:
- 准备生产环境部署计划
- 执行数据初始化和补偿脚本
- 监控系统性能和错误日志
- 验证新成就功能正常工作

**Validation**:
- 生产环境成就功能正常
- 无性能下降
- 无数据错误

---

## Dependency Graph

```
Task 1.1 → Task 1.2 → Task 1.3
             ↓
Task 2.1 → Task 2.2 → Task 2.3
             ↓
Task 2.4 → Task 3.1 → Task 3.2 → Task 3.3
                          ↓
                       Task 4.1
                       Task 4.2
                       Task 4.3
                          ↓
Task 5.1 → Task 5.2 → Task 5.3
```

## Parallel Work Opportunities

- Task 1.1 和 Task 2.4 可以并行开发
- Task 4.1、4.2、4.3 可以并行开发（由不同开发者）
- Task 5.1 可以在 Phase 1 完成后就开始准备

## Risk Mitigation

### 性能风险
- **风险**: 统计查询可能影响数据库性能
- **缓解**: 
  - 在开发阶段进行性能测试
  - 添加必要的数据库索引
  - 考虑引入缓存层

### 数据准确性风险
- **风险**: 连续天数计算可能出错
- **缓解**:
  - 编写详细的单元测试
  - 在测试环境充分验证
  - 记录详细日志便于排查

### 部署风险
- **风险**: 数据初始化脚本执行失败
- **缓解**:
  - 提前在测试环境演练
  - 准备回滚方案
  - 分批次执行，逐步验证

## Success Criteria

- [ ] 所有15+种成就类型定义完成
- [ ] 所有统计方法开发并测试完成
- [ ] 成就解锁逻辑准确率100%
- [ ] 单次成就检查响应时间 < 100ms
- [ ] 代码测试覆盖率 >= 80%
- [ ] 生产环境部署成功，无回滚
- [ ] 监控数据显示系统性能稳定
