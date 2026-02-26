# Change: 添加家庭管理模块

## Why
用户需要管理家庭成员信息，记录重要日期（如生日、体检日期），并接收相关提醒，帮助用户更好地关注和照顾家人健康。这是一个贴近用户生活场景的核心功能，能够提升产品的实用价值和用户粘性。

## What Changes
- **新增家庭管理领域模型**：创建 Family（家庭）和 FamilyMember（家庭成员）聚合根
- **新增家庭成员管理功能**：支持添加、查询、编辑、删除家庭成员
- **新增生日提醒功能**：根据家庭成员生日自动生成提醒
- **新增健康体检管理**：记录父母体检信息，提供体检到期提醒
- **新增 RESTful API**：提供完整的家庭管理相关接口（遵循 `/api/v2/` 规范）
- **新增数据库表结构**：family_member、health_checkup 表
- **新增前端页面**：家庭成员列表、添加/编辑成员、生日提醒、体检管理等页面
- **新增单元测试和集成测试**：确保功能正确性

## Impact
### 后端影响
- **新增领域**：`com.crazydream.domain.family`（包含 model、repository、service）
- **新增应用服务**：`com.crazydream.application.family`（Command/Query 执行器）
- **新增基础设施**：`com.crazydream.infrastructure.persistence.family`（Mapper、Repository 实现）
- **新增 API 接口**：`com.crazydream.interfaces.v2.FamilyController`、`HealthCheckupController`
- **数据库变更**：新增 2 张表（family_member、health_checkup）
- **依赖提醒模块**：复用现有的 Reminder 领域能力

### 前端影响
- **新增页面**：
  - `pages/family/family-list`（家庭成员列表）
  - `pages/family/member-form`（添加/编辑成员）
  - `pages/family/birthday-reminder`（生日提醒）
  - `pages/family/health-checkup`（体检管理）
- **新增组件**：`components/family-member-card`（成员卡片组件）
- **新增 API 服务**：`services/familyAPI.js`
- **导航配置**：在个人中心页面添加"家庭管理"入口

### 跨模块影响
- 提醒模块将新增"生日提醒"和"体检提醒"类型
- 用户模块无需修改，通过 userId 关联家庭成员

## Affected Specs
- **新增**：`family-management`（家庭管理核心能力）
- **轻度修改**：`reminder-system`（扩展提醒类型，支持生日和体检提醒）

## Dependencies
- 依赖现有的 User 领域（通过 userId 关联）
- 依赖现有的 Reminder 领域（生成提醒）
- 无外部服务依赖
