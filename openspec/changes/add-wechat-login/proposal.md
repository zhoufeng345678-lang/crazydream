# Change: 添加微信一键登录功能

## Why

当前系统仅支持邮箱密码登录，用户注册流程繁琐，需输入邮箱、密码、昵称等信息，首次使用门槛高。移动端用户更习惯使用微信快速登录，可显著提升用户留存率和登录转化率。

## What Changes

- 新增微信OAuth2.0授权登录流程，支持通过微信授权码换取用户openid
- 实现自动注册/登录逻辑：首次登录自动创建账号，已有用户直接登录
- 新增`WechatService`封装微信开放平台API调用
- 扩展`UserApplicationService`添加`wechatLogin()`方法
- 扩展`UserRepository`添加按wechatOpenId查询用户能力
- 新增接口`POST /api/v2/auth/wechat-login`接收微信授权码
- 扩展User聚合根添加`createByWechat()`工厂方法
- 新增数据库索引`idx_wechat_open_id`优化查询性能
- 新增配置项：微信AppID、AppSecret、API超时等
- 不影响现有邮箱密码登录功能，保持向后兼容

## Impact

### 影响的Specs
- `user-management`: 新增微信登录需求

### 影响的代码
- **Interface层**: `AuthController` - 新增wechat-login接口
- **Application层**: 
  - `UserApplicationService` - 新增wechatLogin方法
  - `WechatService` (新建) - 微信API调用服务
  - 新增DTO: `WechatLoginCommand`
- **Domain层**: 
  - `User` - 新增createByWechat工厂方法
  - `UserRepository` - 新增findByWechatOpenId方法
- **Infrastructure层**:
  - `UserRepositoryImpl` - 实现按wechatOpenId查询
  - `UserPersistenceMapper.xml` - 新增SQL查询
  - 新增`WechatConfig` - 微信配置类
- **配置文件**: `application.yml` - 新增wechat配置节
- **数据库**: 新增索引`idx_wechat_open_id`
- **安全配置**: `SecurityConfig` - 白名单添加微信登录路径

### 依赖的外部服务
- 微信开放平台API (code2Session接口)

### 非功能性影响
- 接口响应时间增加微信API调用耗时(约500ms-1s)
- 需配置微信AppID和AppSecret环境变量
- 需在微信开放平台配置服务器IP白名单
