# Change: 增强用户资料管理功能

## Why

当前用户管理系统仅支持基本的昵称和头像更新功能，缺少手机号、微信OpenID等社交账号关联信息的管理能力。同时，头像更新时直接传入URL字符串，未实现真正的文件上传到OSS服务器功能。这限制了：

1. 用户无法关联手机号进行账号安全验证
2. 无法与微信等第三方平台集成进行社交登录
3. 头像上传流程不完整，需要前端自行处理OSS上传逻辑

## What Changes

### 功能扩展
- 添加手机号码字段及验证
- 添加微信OpenID字段用于微信登录集成
- 实现头像文件上传至阿里云OSS功能

### API变更
- 修改更新用户资料接口，支持手机号和OpenID字段
- 新增头像上传接口，返回OSS存储URL

### 数据模型变更
- User领域模型增加phone和wechatOpenId值对象
- UserDTO增加对应字段
- UpdateProfileCommand增加phone和wechatOpenId参数

### 基础设施变更
- 实现FileUploadService处理文件上传到OSS
- 更新UserRepository支持新字段持久化

**BREAKING**: 无破坏性变更（新增字段均为可选）

## Impact

### 受影响的规范（Specs）
- `user-management` - 新增手机号和OpenID管理需求，修改更新资料接口

### 受影响的代码
- `domain/user/model/aggregate/User.java` - 增加phone和wechatOpenId字段
- `domain/user/model/valueobject/` - 新增Phone.java和WechatOpenId.java
- `application/user/dto/UserDTO.java` - 增加字段
- `application/user/dto/UpdateProfileCommand.java` - 增加字段
- `application/user/service/UserApplicationService.java` - 更新逻辑，集成OSS上传
- `infrastructure/oss/OssService.java` - 实现OSS文件上传服务
- `interfaces/user/UserController.java` - 新增头像上传端点
- `infrastructure/persistence/po/UserPO.java` - 增加数据库字段映射
- `src/main/resources/schema.sql` - 已包含phone和openid字段，无需修改

### 依赖变更
- 无新增外部依赖（阿里云OSS SDK已存在）

## Timeline

预计实现时间：2-3天
- Day 1: 领域模型和值对象扩展，数据库映射更新
- Day 2: OSS上传服务实现，应用服务和控制器更新
- Day 3: 测试和文档更新
