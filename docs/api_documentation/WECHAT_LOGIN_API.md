# 微信一键登录API文档

## 概述

微信一键登录功能允许用户通过微信OAuth2.0授权快速登录系统，无需注册即可自动创建账号。

## 接口信息

### 微信一键登录

**接口地址**: `POST /api/v2/auth/wechat-login`

**权限要求**: 无需认证（已加入白名单）

**Content-Type**: `application/json`

### 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| code | string | 是 | 微信授权码（通过微信登录获取） |

### 请求示例

```json
{
  "code": "071234567890abcdef"
}
```

### 响应格式

#### 成功响应

**HTTP状态码**: 200

**响应体**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 123,
      "email": "wechat_oXXXXXXXXXXXXXXXXXXXX@crazydream.com",
      "nickName": "微信用户",
      "avatar": null,
      "phone": null,
      "bio": null,
      "level": "BEGINNER",
      "points": 0,
      "createTime": "2026-01-26T23:00:00",
      "updateTime": "2026-01-26T23:00:00"
    }
  }
}
```

#### 错误响应

**1. 参数验证失败**

HTTP状态码: 200  
业务代码: 400

```json
{
  "code": 400,
  "message": "微信授权码不能为空",
  "data": null
}
```

**2. 微信授权失败**

HTTP状态码: 200  
业务代码: 401

```json
{
  "code": 401,
  "message": "微信授权失败，请重新登录",
  "data": null
}
```

可能的错误消息：
- `微信授权失败，请重新登录` - 授权码无效（错误码40029）
- `微信服务繁忙，请稍后重试` - 微信服务异常（错误码-1）
- `微信授权码已使用` - 授权码已被使用（错误码40163）
- `微信授权失败: {errmsg}` - 其他微信API错误

**3. 系统错误**

HTTP状态码: 200  
业务代码: 500

```json
{
  "code": 500,
  "message": "微信服务暂时不可用，请稍后重试",
  "data": null
}
```

## 业务流程

```
1. 客户端调用微信登录，获取授权码（code）
   ↓
2. 客户端将code发送到本接口
   ↓
3. 服务端调用微信code2Session接口换取openid
   ↓
4. 服务端根据openid查询用户
   ├─ 用户存在：直接返回JWT token
   └─ 用户不存在：自动注册新用户 → 返回JWT token
   ↓
5. 客户端使用JWT token访问其他需要认证的接口
```

## 技术实现

### 自动注册规则

首次使用微信登录的用户会自动创建账号，规则如下：

- **邮箱**: `wechat_{openid}@crazydream.com`（系统内部标识）
- **昵称**: `微信用户`（默认昵称）
- **密码**: `null`（微信登录用户无密码）
- **等级**: `BEGINNER`
- **积分**: `0`

### 并发处理

系统使用数据库唯一索引（`idx_wechat_open_id`）确保同一openid不会创建重复用户。并发场景下，第二个请求会捕获唯一性冲突异常并重新查询用户。

### 超时设置

微信API调用超时时间为3秒，超时后返回错误提示。

## 配置说明

### 应用配置

在 `application.yml` 中配置微信开放平台信息：

```yaml
wechat:
  app-id: ${WECHAT_APP_ID:your-app-id}
  app-secret: ${WECHAT_APP_SECRET:your-app-secret}
  api:
    code2session-url: https://api.weixin.qq.com/sns/jscode2session
    timeout: 3000
```

### 环境变量

建议通过环境变量配置敏感信息：

- `WECHAT_APP_ID`: 微信小程序AppID
- `WECHAT_APP_SECRET`: 微信小程序AppSecret

## 安全说明

1. **AppSecret保护**: AppSecret应通过环境变量配置，不要硬编码在代码中
2. **授权码单次有效**: 每个code只能使用一次，使用后立即失效
3. **传输安全**: 生产环境必须使用HTTPS传输
4. **Token管理**: 返回的JWT token应妥善保存，避免泄露

## 测试

### 使用微信开发者工具测试

1. 在微信开发者工具中调用 `wx.login()` 获取code
2. 将code发送到本接口
3. 验证返回的token和用户信息

### 集成测试脚本

项目根目录下的 `test_wechat_login.py` 提供了完整的测试用例：

```bash
python3 test_wechat_login.py
```

测试覆盖：
- ✓ 健康检查
- ✓ 接口白名单验证
- ✓ 参数验证（缺失/空值）
- ✓ 无效授权码处理

## FAQ

**Q: 为什么我的测试一直返回"invalid appid"？**

A: 需要在配置文件中设置正确的微信AppID和AppSecret，默认值仅用于占位。

**Q: 微信登录的用户能否使用邮箱密码登录？**

A: 不能。微信登录用户的密码字段为null，只能通过微信登录方式访问系统。

**Q: 如何将微信登录账号与邮箱账号绑定？**

A: 当前版本暂不支持账号绑定功能，计划在后续版本中实现。

**Q: 用户昵称和头像如何更新？**

A: 可以调用用户信息更新接口修改昵称和头像。微信登录不会自动同步微信用户信息。

## 相关文档

- [OpenSpec提案文档](./openspec/changes/add-wechat-login/proposal.md)
- [技术设计文档](./openspec/changes/add-wechat-login/design.md)
- [任务清单](./openspec/changes/add-wechat-login/tasks.md)
- [需求规格](./openspec/changes/add-wechat-login/specs/user-management/spec.md)

## 版本历史

- **v1.0.0** (2026-01-26): 初始版本，支持微信一键登录和自动注册
