# Change: expand-achievement-types

## Quick Links

- [Proposal](./proposal.md) - 完整提案文档
- [Tasks](./tasks.md) - 详细任务分解
- [Design](./design.md) - 架构设计和技术方案
- [Spec](./specs/achievement-system-enhancement/spec.md) - 规格变更文档

## Summary

扩展成就系统，从当前的6种基础成就类型扩展到20+种，涵盖多个维度：

- **目标完成数量**: 30个、200个等里程碑
- **连续打卡**: 3天、7天、14天、30天、100天
- **分类专注**: 单分类达人、全能选手
- **效率提升**: 早起鸟、夜猫子、速度大师、守时之星
- **使用时长**: 7天、30天、365天里程碑
- **完成质量**: 高完成率成就

## Key Changes

1. 新增15+种成就类型定义
2. 扩展Repository层统计方法
3. 创建统计数据收集服务
4. 实现定时任务检查机制
5. 重构成就解锁判断逻辑

## Timeline

约9个工作日，分5个阶段：
- Phase 1: 领域模型扩展 (2天)
- Phase 2: Repository统计方法 (3天)
- Phase 3: 成就检查逻辑 (2天)
- Phase 4: 测试 (1天)
- Phase 5: 数据初始化 (1天)

## Status

**DRAFT** - 等待审批

## Approvers

- [ ] Tech Lead
- [ ] Product Owner
- [ ] Architecture Team
