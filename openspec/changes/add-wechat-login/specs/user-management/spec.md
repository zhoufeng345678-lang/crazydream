## ADDED Requirements

### Requirement: 微信OAuth2.0一键登录

系统 SHALL 提供微信OAuth2.0一键登录功能，允许用户通过微信授权码快速登录系统。系统 SHALL 在用户首次使用微信登录时自动创建账号(自动注册)，在用户已有账号时直接登录。

系统 SHALL 接收前端传入的微信授权码(code)，调用微信开放平台code2Session接口换取用户openid和session_key。系统 SHALL 根据openid查询用户是否存在，不存在则自动创建新用户，存在则直接返回JWT token。

系统 SHALL 为微信登录用户生成唯一标识邮箱，格式为 `wechat_{openid}@crazydream.com`。系统 SHALL 为微信登录用户设置默认昵称"微信用户"，password字段设置为null以区分登录方式。

系统 SHALL 在wechat_open_id字段上创建唯一索引，保证微信用户唯一性。微信API调用超时时间 SHALL 设置为3秒。微信AppID和AppSecret SHALL 通过环境变量配置，不得硬编码在代码中。

#### Scenario: 新用户首次使用微信登录成功

**GIVEN** 用户在微信小程序中授权登录，获得临时授权码 `081AbcDef2`  
**AND** 该授权码对应的openid为 `oXYZ123abc`，数据库中不存在该openid的用户  
**WHEN** 前端调用 `POST /api/v2/auth/wechat-login` 接口，传入 `{"code": "081AbcDef2"}`  
**THEN** 系统应：
  - 调用微信code2Session接口，传入appId、secret和code
  - 从微信响应中解析出openid `oXYZ123abc`
  - 在数据库中查询wechat_open_id为 `oXYZ123abc` 的用户
  - 查询结果为空，触发自动注册逻辑
  - 创建新用户：
    - email: `wechat_oXYZ123abc@crazydream.com`
    - nickName: `微信用户`
    - wechatOpenId: `oXYZ123abc`
    - password: null
    - level: BEGINNER
    - points: 0
  - 保存新用户到数据库
  - 生成JWT token
  - 返回响应：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 100,
      "email": "wechat_oXYZ123abc@crazydream.com",
      "nickName": "微信用户",
      "wechatOpenId": "oXYZ123abc",
      "level": 1,
      "levelDescription": "初学者",
      "points": 0
    }
  }
}
```

**AND** 返回的JWT token可正常访问受保护接口

#### Scenario: 已有用户使用微信登录成功

**GIVEN** 数据库中已存在wechat_open_id为 `oXYZ123abc` 的用户，用户ID为888  
**AND** 用户获得新的微信授权码 `082DefGhi3`，对应同一openid  
**WHEN** 前端调用 `POST /api/v2/auth/wechat-login` 接口，传入 `{"code": "082DefGhi3"}`  
**THEN** 系统应：
  - 调用微信API换取openid `oXYZ123abc`
  - 在数据库中查询到已有用户(ID=888)
  - 直接使用该用户信息生成JWT token
  - 不创建新用户
  - 返回响应包含现有用户的完整信息
  - 返回的token可正常访问受保护接口

#### Scenario: 微信授权码无效

**GIVEN** 用户传入一个已过期或无效的微信授权码 `invalid_code`  
**WHEN** 前端调用 `POST /api/v2/auth/wechat-login` 接口  
**THEN** 系统应：
  - 调用微信code2Session接口
  - 微信API返回错误响应：`{"errcode": 40029, "errmsg": "invalid code"}`
  - 捕获微信API错误
  - 返回401错误响应：
```json
{
  "code": 401,
  "message": "微信授权失败，请重新登录",
  "data": null
}
```

#### Scenario: 微信API调用超时

**GIVEN** 微信服务暂时不可用或网络延迟过高  
**AND** 用户传入有效的微信授权码  
**WHEN** 系统调用微信code2Session接口  
**AND** 3秒内未收到响应  
**THEN** 系统应：
  - 触发超时异常
  - 记录错误日志："微信API调用超时"
  - 返回500错误响应：
```json
{
  "code": 500,
  "message": "微信服务暂时不可用，请稍后重试",
  "data": null
}
```

#### Scenario: 微信授权码为空

**GIVEN** 用户未提供微信授权码  
**WHEN** 前端调用 `POST /api/v2/auth/wechat-login` 接口，传入 `{"code": ""}`或`{"code": null}`  
**THEN** 系统应：
  - 参数校验失败
  - 返回400错误响应：
```json
{
  "code": 400,
  "message": "微信授权码不能为空",
  "data": null
}
```

#### Scenario: 数据库wechat_open_id唯一性冲突

**GIVEN** 数据库中已存在wechat_open_id为 `oXYZ123abc` 的用户  
**AND** 系统在自动注册时尝试创建相同openid的新用户(并发场景)  
**WHEN** 执行INSERT操作  
**THEN** 系统应：
  - 触发数据库UNIQUE约束冲突异常
  - 捕获异常后重新查询该openid的用户
  - 使用查询到的用户信息生成token
  - 正常返回登录响应

**Note**: 此场景为极端并发情况的保护机制

---
