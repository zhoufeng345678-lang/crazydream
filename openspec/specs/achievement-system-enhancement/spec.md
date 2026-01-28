# achievement-system-enhancement Specification

## Purpose
TBD - created by archiving change expand-achievement-types. Update Purpose after archive.
## Requirements
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

