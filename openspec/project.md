# Project Context

## Purpose
CrazyDream 是一个目标管理和个人成长追踪系统，帮助用户制定目标、记录进度、获得成就，并提供家庭管理功能。

## Tech Stack
### 后端
- Java 17+
- Spring Boot 3.2.0
- MyBatis 3.0.3
- MySQL 8.0
- Maven 3.8+
- Alibaba Cloud OSS（对象存储）

### 前端
- 微信小程序原生框架
- JavaScript ES6+

## Project Conventions

### Code Style
- 遵循阿里巴巴 Java 开发手册
- 类名使用 UpperCamelCase
- 方法名、变量名使用 lowerCamelCase
- 常量全大写，下划线分隔
- 中文注释，描述业务逻辑

### Architecture Patterns
- **DDD + COLA 四层架构**：
  1. Interface 层（接口层）：`com.crazydream.interfaces`，REST API 暴露（v2: `/api/v2/*`）
  2. Application 层（应用层）：`com.crazydream.application`，编排用例流程、事务控制
  3. Domain 层（领域层）：`com.crazydream.domain`，聚合根、实体、值对象、领域服务，核心业务规则
  4. Infrastructure 层（基础设施层）：`com.crazydream.infrastructure`，Repository 实现、数据持久化、外部系统适配

- **充血模型**：领域对象包含业务行为，避免贫血模型
- **CQRS 模式**：Command（写操作）和 Query（读操作）分离
- **Repository 模式**：领域层定义接口，基础设施层实现，实现防腐层
- **PO/Entity 分离**：持久化对象（PO）与领域实体（Entity）解耦

### Testing Strategy
- 单元测试：Domain 层业务逻辑测试
- 集成测试：Application 层完整流程测试
- API 测试：Interface 层 REST API 测试
- 测试覆盖率目标：70%+

### Git Workflow
- 主分支：main/master
- 功能分支：feature/功能名称
- 修复分支：bugfix/问题描述
- Commit 规范：type(scope): description
  - feat: 新功能
  - fix: 修复
  - refactor: 重构
  - test: 测试
  - docs: 文档

## Domain Context
### 核心领域
1. **User（用户）**：用户注册、登录、资料管理
2. **Goal（目标）**：目标创建、进度追踪、完成状态管理
3. **SubGoal（子目标）**：目标分解，支持层级管理
4. **Category（分类）**：目标分类管理
5. **Achievement（成就）**：成就解锁系统，激励用户
6. **Reminder（提醒）**：提醒管理，支持多种提醒类型
7. **Diary（日记）**：用户日记记录
8. **Todo（待办）**：待办事项管理
9. **Family（家庭）**：家庭成员管理、生日提醒、健康体检管理（新增）

### 业务规则
- 每个用户数据通过 user_id 隔离
- 所有 API 需要 JWT 认证（除登录注册）
- 删除操作使用级联删除（CASCADE）
- 时间字段使用 `create_time` 和 `update_time` 标准命名

## Important Constraints
### 技术约束
- 不使用 Spring Data JPA，使用 MyBatis
- 数据库表名使用小写下划线命名
- 所有实体必须有 create_time 和 update_time 字段
- API 响应统一使用 Response<T> 包装

### 业务约束
- 用户只能访问自己的数据
- 提醒时间精确到分钟
- 文件上传限制 5MB
- 头像上传仅支持 JPG、PNG、GIF 格式

## External Dependencies
- **Alibaba Cloud OSS**：头像、图片存储
- **微信开放平台**：微信登录 OAuth
