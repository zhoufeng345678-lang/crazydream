# Bug修复记录

## 日期：2026-01-19

## 问题描述

**错误信息**：
```
2026-01-19 17:16:01.331 [http-nio-8080-exec-4] ERROR com.crazydream.interfaces.goal.GoalController - 更新进度失败: 当前状态不能转换为已完成: NOT_STARTED
java.lang.IllegalStateException: 当前状态不能转换为已完成: NOT_STARTED
        at com.crazydream.domain.goal.model.aggregate.Goal.complete(Goal.java:142)
```

**根本原因**：
前端在更新目标进度时，如果进度值达到100%，会自动将目标状态更新为"已完成"。但是后端的状态转换规则不允许从 `NOT_STARTED`（未开始）直接转换为 `COMPLETED`（已完成）。

## 状态转换规则

根据 `GoalStatus.java` 中的状态机定义：

```java
public boolean canTransitionTo(GoalStatus target) {
    switch (this) {
        case NOT_STARTED:
            return target == IN_PROGRESS || target == ABANDONED;
        case IN_PROGRESS:
            return target == COMPLETED || target == ABANDONED;
        case COMPLETED:
        case ABANDONED:
            return false; // 终态，不能再转换
    }
    return false;
}
```

**合法的状态转换路径**：
- `NOT_STARTED` → `IN_PROGRESS` 或 `ABANDONED`
- `IN_PROGRESS` → `COMPLETED` 或 `ABANDONED`

**非法转换**：
- `NOT_STARTED` → `COMPLETED` ❌

## 前端行为分析

在 `goal-detail.js` 的 `updateProgress()` 方法中（第214行）：

```javascript
goalAPI.updateProgress(this.data.goalId, progress)
  .then(response => {
    const serverProgress = response.data.progress || progress
    
    this.setData({
      'currentGoal.progress': serverProgress,
      'currentGoal.status': serverProgress === 100 ? 'completed' : 'in_progress',
      isUpdatingProgress: false
    })
  })
```

前端期望：当进度达到100%时，状态变为 `completed`

## 后端行为分析

在 `Goal.java` 的 `updateProgress()` 方法中（第122-135行）：

```java
public void updateProgress(int newProgress) {
    if (status.isTerminal()) {
        throw new IllegalStateException("目标已结束，不能更新进度");
    }
    
    this.progress = GoalProgress.of(newProgress);
    
    // 进度达到100%自动完成
    if (progress.isCompleted() && status != GoalStatus.COMPLETED) {
        this.complete();  // 这里会抛出异常
    }
    
    this.updateTime = LocalDateTime.now();
}
```

当目标状态为 `NOT_STARTED` 时，如果直接设置进度为100%，会尝试调用 `complete()`，但由于状态转换规则限制，抛出异常。

## 解决方案

在后端的 `updateProgress()` 方法中，增加状态转换逻辑：

```java
/**
 * 更新目标进度
 */
public void updateProgress(int newProgress) {
    if (status.isTerminal()) {
        throw new IllegalStateException("目标已结束，不能更新进度");
    }
    
    this.progress = GoalProgress.of(newProgress);
    
    // 如果目标处于未开始状态，且进度大于0，自动转为进行中状态
    if (status == GoalStatus.NOT_STARTED && newProgress > 0) {
        this.status = GoalStatus.IN_PROGRESS;
    }
    
    // 进度达到100%自动完成
    if (progress.isCompleted() && status != GoalStatus.COMPLETED) {
        this.complete();
    }
    
    this.updateTime = LocalDateTime.now();
}
```

## 修改说明

**文件**：`src/main/java/com/crazydream/domain/goal/model/aggregate/Goal.java`

**修改位置**：第122-135行，`updateProgress()` 方法

**修改内容**：
- 在更新进度前，检查目标状态是否为 `NOT_STARTED`
- 如果是 `NOT_STARTED` 且新进度大于0，先将状态转换为 `IN_PROGRESS`
- 这样后续的自动完成逻辑就能正常工作

## 测试建议

1. **测试场景1**：未开始目标直接设置进度为100%
   - 初始状态：`NOT_STARTED`，进度0%
   - 操作：更新进度到100%
   - 期望结果：状态自动转为 `IN_PROGRESS`，然后转为 `COMPLETED`，进度100%

2. **测试场景2**：未开始目标设置部分进度
   - 初始状态：`NOT_STARTED`，进度0%
   - 操作：更新进度到50%
   - 期望结果：状态转为 `IN_PROGRESS`，进度50%

3. **测试场景3**：进行中目标完成
   - 初始状态：`IN_PROGRESS`，进度50%
   - 操作：更新进度到100%
   - 期望结果：状态转为 `COMPLETED`，进度100%

4. **测试场景4**：已结束目标不应更新进度
   - 初始状态：`COMPLETED` 或 `ABANDONED`
   - 操作：尝试更新进度
   - 期望结果：抛出异常"目标已结束，不能更新进度"

## 影响范围

- ✅ **仅修改后端逻辑**：前端代码无需修改
- ✅ **符合状态机设计**：新增的转换逻辑符合"未开始→进行中"的合法路径
- ✅ **提升用户体验**：用户无需手动"开始"目标即可更新进度，更加自然

## 修改时间

2026-01-19 17:20

## 修改人

CrazyDream 开发团队
