# Change: 添加用户个人简介功能

## Why

当前用户资料管理系统仅支持昵称、头像、手机号和微信OpenID等基本信息，缺少个人简介(bio)字段。个人简介是用户展示个人特色、兴趣爱好或个性签名的重要途径，对于提升用户个性化体验和社交属性具有重要价值。

具体问题：
1. 用户无法在个人资料中添加自我描述或个性签名
2. 缺少一个灵活的文本字段来展示用户个性
3. 与常见社交应用的用户资料功能存在差距

## What Changes

### 功能扩展
- 在用户资料更新接口中添加bio字段支持
- bio字段为可选字段，最大长度500字符
- 用户可以在个人资料中查看和编辑bio

### 数据模型变更
- User领域模型增加bio值对象（Bio）
- UserDTO增加bio字段
- UpdateProfileCommand增加bio参数
- UserPO（持久化对象）增加bio字段映射

### API变更
- 修改 `PUT /api/v2/users/profile` 接口，请求体增加bio字段
- 修改 `GET /api/v2/users/profile` 接口，响应体增加bio字段

### 数据库变更
- user表增加bio列（VARCHAR(500)）

**BREAKING**: 无破坏性变更（新增字段为可选）

## Impact

### 受影响的规范（Specs）
- `user-management` - 修改用户资料更新和查询需求，增加bio字段支持

### 受影响的代码
- `domain/user/model/aggregate/User.java` - 增加bio字段和更新方法
- `domain/user/model/valueobject/Bio.java` - 新增Bio值对象（需创建）
- `application/user/dto/UserDTO.java` - 增加bio字段
- `application/user/dto/UpdateProfileCommand.java` - 增加bio参数
- `application/user/assembler/UserAssembler.java` - 更新映射逻辑
- `application/user/service/UserApplicationService.java` - 更新业务逻辑
- `infrastructure/persistence/po/UserPO.java` - 增加bio字段映射
- `infrastructure/persistence/mapper/UserPersistenceMapper.xml` - 更新SQL映射
- `interfaces/user/UserController.java` - API接口无需修改（字段自动映射）
- `src/main/resources/schema.sql` - 增加bio列定义

### 依赖变更
- 无新增外部依赖

### 配置变更
- 无需修改配置文件

## Timeline

预计实现时间：2-3小时
- 30分钟：创建Bio值对象和更新User聚合根
- 30分钟：更新DTO、Command和Assembler
- 30分钟：更新数据库schema和持久化层
- 30分钟：测试验证
- 30分钟：更新API文档
