# user-management Specification

## Purpose
定义用户资料管理的功能需求，包括用户信息编辑、手机号和微信OpenID管理、头像上传、个人简介等核心能力。

## MODIFIED Requirements

### Requirement: UM-003 用户资料更新接口

系统 SHALL 扩展用户资料更新接口，支持昵称、头像、手机号、微信OpenID和个人简介字段的更新。

**API**: `PUT /api/v2/users/profile`

**Request Body**:
```json
{
  "nickName": "string (optional)",
  "avatar": "string (optional)",
  "phone": "string (optional)",
  "wechatOpenId": "string (optional)",
  "bio": "string (optional)"
}
```

bio字段规则：
- bio字段 SHALL 为可选字段，允许null或空字符串
- 当bio不为null时，系统 SHALL 验证其长度不超过500个字符
- bio字段长度超过500字符时，系统 SHALL 拒绝更新并返回错误信息
- bio字段 SHALL 支持中英文、数字、标点符号等常见字符

#### Scenario: 更新用户个人简介

**Given** 用户已登录，当前昵称为"张三"，bio为null  
**When** 用户提交更新请求：
  - nickName: "张三"
  - bio: "热爱生活，追求梦想"
**Then** 系统应：
  - 验证bio长度为11字符，未超过500字符限制
  - 更新用户的bio字段
  - 保持其他字段不变
  - 返回更新成功

#### Scenario: 同时更新多个字段包括bio

**Given** 用户已登录，当前昵称为"旧昵称"，未绑定手机号，bio为null  
**When** 用户提交更新请求：
  - nickName: "新昵称"
  - phone: "13812345678"
  - bio: "这是我的个性签名"
**Then** 系统应：
  - 验证所有字段格式正确
  - 更新所有提交的字段（nickName、phone、bio）
  - 保持未提交字段不变（如avatar）
  - 返回更新后的完整用户信息

#### Scenario: 提交超长bio

**Given** 用户已登录  
**When** 用户提交bio字段，内容超过500字符  
**Then** 系统应：
  - 验证bio长度超过限制
  - 拒绝更新请求
  - 返回错误信息 "个人简介长度不能超过500字符"
  - 所有字段均不更新

#### Scenario: 清空个人简介

**Given** 用户已登录，当前bio为"这是我的个性签名"  
**When** 用户提交更新请求：
  - bio: null 或 bio: ""
**Then** 系统应：
  - 接受空值或null值
  - 将用户的bio字段设置为null
  - 返回更新成功

#### Scenario: 仅更新bio字段

**Given** 用户已登录，当前昵称为"张三"  
**When** 用户仅提交 bio: "热爱编程，热爱生活"  
**Then** 系统应：
  - 验证bio格式正确
  - 更新bio字段
  - 保持其他字段不变（昵称、头像等）
  - 返回更新成功

---

### Requirement: UM-004 用户信息查询接口

系统 SHALL 在用户信息查询响应中包含昵称、邮箱、头像、手机号、微信OpenID、个人简介、等级和积分等字段。

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
  "bio": "string",
  "level": 1,
  "levelDescription": "string",
  "points": 100,
  "createTime": "2025-01-01T00:00:00",
  "updateTime": "2025-01-26T00:00:00"
}
```

bio字段规则：
- 响应中 SHALL 始终包含bio字段
- 当用户未设置bio时，bio字段值 SHALL 为null
- 当用户设置了bio时，bio字段 SHALL 返回完整内容

#### Scenario: 查询已设置bio的用户

**Given** 用户已登录，ID为123  
**And** 该用户bio为 "热爱生活，追求梦想"  
**When** 用户调用获取个人信息接口  
**Then** 系统应返回包含 bio: "热爱生活，追求梦想" 的完整用户信息

#### Scenario: 查询未设置bio的用户

**Given** 用户已登录但未设置bio  
**When** 用户调用获取个人信息接口  
**Then** 系统应返回 bio: null

---
