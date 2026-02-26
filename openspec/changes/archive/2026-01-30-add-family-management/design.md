## Context
家庭管理模块需要在现有的 COLA 四层架构下实现，复用 Reminder 领域的提醒能力，同时需要确保前后端数据流的完整性和一致性。

## Goals
- 实现家庭成员的完整生命周期管理（CRUD）
- 提供生日和体检日期的自动提醒机制
- 遵循 DDD + COLA 架构规范，保持代码清晰度和可维护性
- 前端提供直观友好的用户界面
- 确保数据隐私和安全

## Non-Goals
- 不实现家庭成员之间的社交互动功能
- 不实现健康数据的详细追踪（如血压、血糖等）
- 不支持多用户共享同一家庭账户
- 不实现医院对接或在线预约体检功能

## Decisions

### 决策 1：领域模型设计
**选择**：使用 FamilyMember 作为聚合根，HealthCheckup 作为值对象附属于 FamilyMember

**理由**：
- 每个家庭成员是独立的业务实体，有自己的生命周期
- 体检记录依附于家庭成员，不单独存在
- 符合 DDD 聚合设计原则，减少跨聚合事务

**替代方案**：
- 创建 Family 作为聚合根，FamilyMember 作为实体
  - **缺点**：增加了不必要的复杂度，因为当前系统是单用户视角
  - **放弃原因**：用户不需要管理"家庭"这个整体概念，只需要管理自己的家庭成员列表

### 决策 2：关系类型设计
**选择**：使用枚举类型 RelationType 定义关系（父亲、母亲、配偶、子女、兄弟姐妹等）

**理由**：
- 类型安全，避免字符串拼写错误
- 便于前端展示和国际化
- 易于扩展新的关系类型

### 决策 3：生日提醒机制
**选择**：在添加/更新家庭成员时，自动创建或更新生日提醒（提前 7 天提醒）

**理由**：
- 用户无需手动创建生日提醒
- 提醒时间可配置（后续可扩展为用户自定义）
- 复用现有 Reminder 领域能力，避免重复开发

**实现方式**：
- 在 FamilyMemberApplicationService 中，保存成员后调用 ReminderApplicationService 创建提醒
- 生日提醒类型为 "BIRTHDAY"，关联到家庭成员 ID

### 决策 4：体检提醒机制
**选择**：体检记录存储为 HealthCheckup 值对象，每条记录可设置下次体检日期，自动生成提醒

**理由**：
- 父母健康体检是重要需求，需要独立管理
- 体检记录包含历史信息（体检日期、下次体检日期、备注等）
- 提前 30 天提醒用户安排体检

**数据模型**：
- health_checkup 表独立存储，通过 family_member_id 关联
- 包含字段：checkup_date（体检日期）、next_checkup_date（下次体检日期）、notes（备注）

### 决策 5：数据库设计
**选择**：使用两张表 family_member 和 health_checkup

**表结构**：
```sql
-- 家庭成员表
CREATE TABLE family_member (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  relation_type VARCHAR(20) NOT NULL,
  birthday DATE,
  phone VARCHAR(20),
  avatar VARCHAR(255),
  notes TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 健康体检表
CREATE TABLE health_checkup (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_member_id BIGINT NOT NULL,
  checkup_date DATE NOT NULL,
  next_checkup_date DATE,
  hospital VARCHAR(100),
  notes TEXT,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (family_member_id) REFERENCES family_member(id) ON DELETE CASCADE
);
```

**理由**：
- 符合第三范式，数据结构清晰
- 便于查询和维护
- 支持一对多关系（一个成员可有多条体检记录）

### 决策 6：前端页面结构
**选择**：采用列表+详情的经典模式

**页面设计**：
1. **家庭成员列表页**（family-list）：展示所有成员卡片，支持筛选和搜索
2. **成员表单页**（member-form）：添加/编辑成员信息
3. **生日提醒页**（birthday-reminder）：展示即将到来的生日列表
4. **体检管理页**（health-checkup）：查看和管理体检记录

**导航入口**：在个人中心（profile）页面添加"家庭管理"入口

## Risks / Trade-offs

### 风险 1：提醒重复创建
**风险**：如果多次编辑家庭成员，可能创建重复的生日提醒

**缓解措施**：
- 在创建提醒前，先删除该成员的旧提醒
- 使用唯一约束：reminder 表添加 (user_id, type, related_id) 的唯一索引

### 风险 2：数据隐私
**风险**：家庭成员信息属于敏感数据

**缓解措施**：
- API 接口必须验证用户登录态
- 只能查询和修改自己的家庭成员数据
- 数据库级别通过 user_id 隔离

### 风险 3：前端页面数量增加
**风险**：新增 4 个页面，可能影响小程序包体积

**缓解措施**：
- 按需加载页面
- 复用现有组件（如表单、列表组件）
- 图片资源使用 CDN

## Migration Plan

### 步骤 1：后端开发（预计 6-8 小时）
1. 创建数据库表（schema.sql）
2. 实现领域模型（Domain 层）
3. 实现应用服务（Application 层）
4. 实现持久化层（Infrastructure 层）
5. 实现 REST API（Interface 层）
6. 编写单元测试和集成测试

### 步骤 2：前端开发（预计 4-6 小时）
1. 创建 API 服务层（familyAPI.js）
2. 实现家庭成员列表页
3. 实现成员表单页
4. 实现生日提醒页
5. 实现体检管理页
6. 在个人中心添加入口
7. 编写页面测试

### 步骤 3：联调测试（预计 2 小时）
1. 前后端接口联调
2. 功能测试和边界测试
3. 性能测试（如列表加载速度）

### 步骤 4：部署上线
1. 执行数据库迁移脚本
2. 部署后端服务
3. 部署前端小程序
4. 生产环境验证

### 回滚方案
- 如需回滚，删除 family_member 和 health_checkup 表
- 删除相关的 API 路由
- 前端移除家庭管理页面入口

## Open Questions
1. **生日提醒提前天数是否需要用户可配置？**
   - 当前设计：固定提前 7 天
   - 可选方案：允许用户在设置中自定义提前天数（1-30 天）

2. **是否需要支持家庭成员头像上传？**
   - 当前设计：支持，复用现有的 OSS 上传能力
   - 路径规则：`family/avatars/{userId}/{memberName}-{timestamp}.{ext}`

3. **体检记录是否需要支持附件上传（如体检报告 PDF）？**
   - 当前设计：暂不支持
   - 后续扩展：可添加 attachment 字段存储 OSS 文件 URL

4. **是否需要实现家庭成员的批量导入功能？**
   - 当前设计：暂不支持
   - 评估：功能使用频率较低，优先级 P2
