# user-management Specification

## Purpose
定义用户资料管理的功能需求，包括用户信息编辑、手机号和微信OpenID管理、头像上传等核心能力。

## ADDED Requirements

### Requirement: UPE-001 用户手机号管理
**Priority**: P1  
**Category**: Feature Addition

系统 SHALL 支持用户绑定和更新手机号码。

手机号必须满足以下规则：
- 仅支持中国大陆手机号格式（11位数字，以1开头）
- 同一手机号不得被多个账号绑定
- 手机号为可选字段，用户可选择不绑定

#### Scenario: 绑定有效手机号

**Given** 用户已登录系统  
**And** 用户当前未绑定手机号  
**When** 用户提交有效的手机号 "13812345678"  
**Then** 系统应：
  - 验证手机号格式正确
  - 保存手机号到用户资料
  - 返回更新成功的响应

#### Scenario: 更新已绑定的手机号

**Given** 用户已登录且当前手机号为 "13812345678"  
**When** 用户更新手机号为 "13987654321"  
**Then** 系统应：
  - 验证新手机号格式正确
  - 更新用户的手机号
  - 返回更新成功的响应

#### Scenario: 提交无效手机号格式

**Given** 用户已登录系统  
**When** 用户提交无效手机号 "12345"  
**Then** 系统应：
  - 拒绝更新
  - 返回错误信息 "手机号格式不正确"

#### Scenario: 解绑手机号

**Given** 用户已登录且当前手机号为 "13812345678"  
**When** 用户提交空值或null作为手机号  
**Then** 系统应：
  - 清除用户的手机号绑定
  - 返回更新成功的响应

---

### Requirement: UPE-002 微信OpenID管理
**Priority**: P1  
**Category**: Feature Addition

系统 SHALL 支持用户绑定微信OpenID，为微信第三方登录集成做准备。

OpenID管理规则：
- OpenID为可选字段
- OpenID必须为非空字符串（长度1-100）
- 同一OpenID不得被多个账号绑定

#### Scenario: 绑定微信OpenID

**Given** 用户已登录系统  
**And** 用户当前未绑定微信OpenID  
**When** 用户提交有效的OpenID "oXYZ123abc456def"  
**Then** 系统应：
  - 验证OpenID非空且长度合法
  - 保存OpenID到用户资料
  - 返回更新成功的响应

#### Scenario: 更新已绑定的OpenID

**Given** 用户已登录且当前OpenID为 "oOLD123"  
**When** 用户更新OpenID为 "oNEW456"  
**Then** 系统应：
  - 验证新OpenID格式正确
  - 更新用户的OpenID
  - 返回更新成功的响应

#### Scenario: 解绑微信OpenID

**Given** 用户已登录且当前OpenID为 "oXYZ123abc"  
**When** 用户提交空值或null作为OpenID  
**Then** 系统应：
  - 清除用户的OpenID绑定
  - 返回更新成功的响应

---

### Requirement: UPE-003 头像文件上传到OSS
**Priority**: P0  
**Category**: Feature Addition

系统 SHALL 提供独立的头像文件上传接口，自动上传到阿里云OSS并返回存储URL。

上传规则：
- 支持的文件格式：JPG, JPEG, PNG, GIF
- 文件大小限制：最大5MB
- OSS存储路径规则：`avatars/{userId}/{timestamp}-{uuid}.{ext}`
- 上传成功后返回完整的OSS访问URL

#### Scenario: 成功上传头像文件

**Given** 用户已登录，用户ID为123  
**And** 用户准备上传一个有效的PNG图片文件（2MB）  
**When** 用户调用头像上传接口 `POST /api/v2/users/avatar`  
**Then** 系统应：
  - 验证文件格式为PNG
  - 验证文件大小小于5MB
  - 生成唯一文件名（如 `avatars/123/1706000000000-a1b2c3d4.png`）
  - 上传文件到阿里云OSS
  - 返回完整的OSS URL（如 `https://bucket.oss-cn-hangzhou.aliyuncs.com/avatars/123/1706000000000-a1b2c3d4.png`）

#### Scenario: 上传文件格式不支持

**Given** 用户已登录  
**When** 用户尝试上传BMP格式的图片文件  
**Then** 系统应：
  - 拒绝上传
  - 返回错误信息 "不支持的文件格式，仅支持 JPG、PNG、GIF"

#### Scenario: 上传文件超过大小限制

**Given** 用户已登录  
**When** 用户尝试上传一个10MB的图片文件  
**Then** 系统应：
  - 拒绝上传
  - 返回错误信息 "文件大小超过限制（最大5MB）"

#### Scenario: OSS上传失败

**Given** 用户已登录并提交有效的图片文件  
**And** OSS服务暂时不可用  
**When** 系统尝试上传文件到OSS  
**Then** 系统应：
  - 捕获OSS异常
  - 返回错误信息 "文件上传失败，请稍后重试"

---

### Requirement: UM-001 用户资料更新接口
**Priority**: P0  
**Category**: API Enhancement

系统 SHALL 扩展用户资料更新接口，支持手机号和微信OpenID字段的更新。

**API**: `PUT /api/v2/users/profile`

**Request Body**:
```json
{
  "nickName": "string (optional)",
  "avatar": "string (optional)",
  "phone": "string (optional)",
  "wechatOpenId": "string (optional)"
}
```

#### Scenario: 同时更新多个字段

**Given** 用户已登录，当前昵称为"旧昵称"，未绑定手机号  
**When** 用户提交更新请求：
  - nickName: "新昵称"
  - phone: "13812345678"
  - wechatOpenId: "oXYZ123"
**Then** 系统应：
  - 验证所有字段格式正确
  - 更新所有提交的字段
  - 保持未提交字段不变（如avatar）
  - 返回更新后的完整用户信息

#### Scenario: 仅更新手机号

**Given** 用户已登录  
**When** 用户仅提交 phone: "13812345678"  
**Then** 系统应：
  - 验证手机号格式
  - 更新手机号字段
  - 保持其他字段不变
  - 返回更新成功

#### Scenario: 部分字段验证失败

**Given** 用户已登录  
**When** 用户提交更新请求：
  - nickName: "合法昵称"
  - phone: "12345"（无效格式）
**Then** 系统应：
  - 拒绝整个更新请求
  - 返回错误信息 "手机号格式不正确"
  - 所有字段均不更新

---

### Requirement: UM-002 用户信息查询接口
**Priority**: P1  
**Category**: API Enhancement

系统 SHALL 在用户信息查询响应中包含手机号和微信OpenID字段。

**API**: `GET /api/v2/users/profile`

**Response Body**:
```json
{
  "id": 123,
  "nickName": "string",
  "email": "string",
  "avatar": "string",
  "phone": "string",
  "wechatOpenId": "string",
  "level": 1,
  "points": 100,
  "createTime": "2025-01-01T00:00:00",
  "updateTime": "2025-01-21T00:00:00"
}
```

#### Scenario: 查询已绑定手机号和OpenID的用户

**Given** 用户已登录，ID为123  
**And** 该用户手机号为 "13812345678"  
**And** 该用户OpenID为 "oXYZ123"  
**When** 用户调用获取个人信息接口  
**Then** 系统应返回包含 phone 和 wechatOpenId 字段的完整信息

#### Scenario: 查询未绑定手机号和OpenID的用户

**Given** 用户已登录但未绑定手机号和OpenID  
**When** 用户调用获取个人信息接口  
**Then** 系统应返回 phone: null, wechatOpenId: null

---
