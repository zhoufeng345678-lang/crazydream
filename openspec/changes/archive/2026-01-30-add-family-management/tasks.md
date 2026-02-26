## 1. 数据库设计与迁移
- [ ] 1.1 在 schema.sql 中添加 family_member 表定义
- [ ] 1.2 在 schema.sql 中添加 health_checkup 表定义
- [ ] 1.3 为 reminder 表添加 related_entity_type 和 related_entity_id 字段（支持关联家庭成员）
- [ ] 1.4 执行数据库迁移脚本并验证表结构

## 2. 后端领域层开发（Domain）
- [ ] 2.1 创建 FamilyMemberId 值对象（`domain/family/model/valueobject/FamilyMemberId.java`）
- [ ] 2.2 创建 RelationType 枚举（父亲、母亲、配偶、子女、兄弟姐妹等）
- [ ] 2.3 创建 FamilyMember 聚合根（包含业务行为方法）
- [ ] 2.4 创建 HealthCheckup 实体（体检记录）
- [ ] 2.5 创建 FamilyMemberRepository 接口（`domain/family/repository/`）
- [ ] 2.6 创建 HealthCheckupRepository 接口
- [ ] 2.7 编写 FamilyMember 单元测试

## 3. 后端基础设施层开发（Infrastructure）
- [ ] 3.1 创建 FamilyMemberPO 持久化对象（`infrastructure/persistence/po/FamilyMemberPO.java`）
- [ ] 3.2 创建 HealthCheckupPO 持久化对象
- [ ] 3.3 创建 FamilyMemberPersistenceMapper（MyBatis Mapper 接口）
- [ ] 3.4 创建 FamilyMemberPersistenceMapper.xml（SQL 映射文件）
- [ ] 3.5 创建 HealthCheckupPersistenceMapper 和 XML
- [ ] 3.6 创建 FamilyMemberConvertor（PO ↔ Entity 转换器）
- [ ] 3.7 创建 HealthCheckupConvertor 转换器
- [ ] 3.8 实现 FamilyMemberRepositoryImpl（Repository 实现类）
- [ ] 3.9 实现 HealthCheckupRepositoryImpl
- [ ] 3.10 编写 Repository 集成测试

## 4. 后端应用层开发（Application）
- [ ] 4.1 创建 FamilyMemberCreateCmd（`application/family/command/`）
- [ ] 4.2 创建 FamilyMemberUpdateCmd
- [ ] 4.3 创建 FamilyMemberDeleteCmd
- [ ] 4.4 创建 FamilyMemberListQry（`application/family/query/`）
- [ ] 4.5 创建 FamilyMemberByIdQry
- [ ] 4.6 创建 HealthCheckupCreateCmd
- [ ] 4.7 创建 HealthCheckupListQry
- [ ] 4.8 实现 FamilyMemberCreateCmdExe（`application/family/executor/command/`）
  - 保存家庭成员
  - 如果有生日，自动创建生日提醒
- [ ] 4.9 实现 FamilyMemberUpdateCmdExe
  - 更新家庭成员
  - 更新或创建生日提醒
- [ ] 4.10 实现 FamilyMemberDeleteCmdExe
  - 删除家庭成员
  - 删除关联的提醒
- [ ] 4.11 实现 FamilyMemberListQryExe（`application/family/executor/query/`）
- [ ] 4.12 实现 FamilyMemberByIdQryExe
- [ ] 4.13 实现 HealthCheckupCreateCmdExe
  - 保存体检记录
  - 如果有下次体检日期，创建体检提醒
- [ ] 4.14 实现 HealthCheckupListQryExe
- [ ] 4.15 编写应用服务单元测试

## 5. 后端接口层开发（Interface）
- [ ] 5.1 创建 FamilyMemberDTO（`interfaces/dto/`）
- [ ] 5.2 创建 HealthCheckupDTO
- [ ] 5.3 创建 FamilyMemberCreateRequest（`interfaces/v2/request/`）
- [ ] 5.4 创建 FamilyMemberUpdateRequest
- [ ] 5.5 创建 HealthCheckupCreateRequest
- [ ] 5.6 实现 FamilyController（`interfaces/v2/FamilyController.java`）
  - `POST /api/v2/family/members` - 添加家庭成员
  - `PUT /api/v2/family/members/{id}` - 更新家庭成员
  - `DELETE /api/v2/family/members/{id}` - 删除家庭成员
  - `GET /api/v2/family/members` - 查询家庭成员列表
  - `GET /api/v2/family/members/{id}` - 查询单个家庭成员
- [ ] 5.7 实现 HealthCheckupController（`interfaces/v2/HealthCheckupController.java`）
  - `POST /api/v2/family/members/{memberId}/checkups` - 添加体检记录
  - `GET /api/v2/family/members/{memberId}/checkups` - 查询体检记录列表
  - `DELETE /api/v2/family/checkups/{id}` - 删除体检记录
- [ ] 5.8 配置 API 权限（需要登录）
- [ ] 5.9 编写 API 集成测试

## 6. 前端 API 服务层开发
- [ ] 6.1 创建 familyAPI.js（`crazydream-front/services/familyAPI.js`）
  - 封装所有家庭管理相关的 API 请求
  - 包含错误处理和数据转换
- [ ] 6.2 在 api-v2.js 中添加家庭管理 API 配置

## 7. 前端组件开发
- [ ] 7.1 创建 family-member-card 组件（`components/family-member-card/`）
  - 展示成员基本信息（姓名、关系、生日等）
  - 支持点击进入详情/编辑
- [ ] 7.2 创建 relation-type-selector 组件（关系类型选择器）
- [ ] 7.3 编写组件单元测试

## 8. 前端页面开发
- [ ] 8.1 创建家庭成员列表页（`pages/family/family-list/`）
  - family-list.wxml - 页面结构
  - family-list.js - 页面逻辑（加载成员列表、搜索、筛选）
  - family-list.wxss - 页面样式
  - family-list.json - 页面配置
- [ ] 8.2 创建成员表单页（`pages/family/member-form/`）
  - 支持添加和编辑模式
  - 表单字段：姓名、关系、生日、手机号、头像、备注
  - 表单验证（必填项、格式校验）
- [ ] 8.3 创建生日提醒页（`pages/family/birthday-reminder/`）
  - 展示即将到来的生日列表（30天内）
  - 按日期排序
  - 支持快速跳转到成员详情
- [ ] 8.4 创建体检管理页（`pages/family/health-checkup/`）
  - 展示所有家庭成员的体检记录
  - 支持添加体检记录
  - 支持查看历史体检记录
  - 显示下次体检提醒
- [ ] 8.5 在 app.json 中注册新页面路由

## 9. 前端导航与集成
- [ ] 9.1 在个人中心页面（pages/profile/profile.wxml）添加"家庭管理"入口
- [ ] 9.2 设计并添加家庭管理入口图标
- [ ] 9.3 实现页面间跳转逻辑
- [ ] 9.4 测试页面导航流畅性

## 10. 测试与验证
- [ ] 10.1 后端单元测试（Domain 层业务逻辑）
- [ ] 10.2 后端集成测试（Application 层完整流程）
- [ ] 10.3 后端 API 测试（使用 Postman 或自动化测试）
- [ ] 10.4 前端页面功能测试（手动测试所有用户场景）
- [ ] 10.5 前后端联调测试
- [ ] 10.6 边界测试（空数据、大数据量、网络异常等）
- [ ] 10.7 性能测试（列表加载速度、接口响应时间）

## 11. 文档与部署
- [ ] 11.1 更新 API 文档（记录所有新增接口）
- [ ] 11.2 编写用户使用说明
- [ ] 11.3 执行数据库迁移脚本
- [ ] 11.4 部署后端服务到测试环境
- [ ] 11.5 部署前端小程序到测试环境
- [ ] 11.6 生产环境验证
- [ ] 11.7 监控错误日志和性能指标

## 备注
- 每个任务完成后需要通过代码审查
- 优先实现核心功能（添加、查询家庭成员），再扩展生日提醒和体检管理
- 前后端可以并行开发，通过 Mock 数据进行前端开发
