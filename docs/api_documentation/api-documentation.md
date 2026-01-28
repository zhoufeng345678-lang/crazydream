# CrazyDream API 文档

## 文档信息

- **版本**: v3.2.0
- **更新时间**: 2026-01-26
- **基础URL**: `http://localhost:8080`
- **架构**: DDD + COLA 四层架构
- **认证方式**: JWT Bearer Token

---

## 架构说明

本项目采用 **DDD（领域驱动设计）+ COLA 四层架构**，确保代码的清晰度、可维护性和可扩展性。

### 四层架构

1. **Interface 层**（接口层）
   - 职责：处理 HTTP 请求、参数校验、响应封装
   - 位置：`src/main/java/com/crazydream/interfaces`
   - 示例：`AuthController`, `GoalController`, `UserController`, `AchievementController`

2. **Application 层**（应用层）
   - 职责：应用服务编排、DTO 转换、事务管理、统计数据采集
   - 位置：`src/main/java/com/crazydream/application`
   - 示例：`UserApplicationService`, `GoalApplicationService`, `AchievementStatisticsService`

3. **Domain 层**（领域层）
   - 职责：核心业务逻辑、领域模型、领域服务、成就判定逻辑
   - 位置：`src/main/java/com/crazydream/domain`
   - 示例：聚合根 `User`, `Goal`, `Achievement`；值对象 `AchievementStatistics`, `UserId`

4. **Infrastructure 层**（基础设施层）
   - 职责：数据持久化、外部服务集成、配置管理
   - 位置：`src/main/java/com/crazydream/infrastructure`
   - 示例：`UserRepositoryImpl`, `MyBatis Mapper`

### 架构优势

- **清晰的职责划分**：每一层只关注自己的职责，避免代码混乱
- **业务逻辑集中**：核心业务逻辑放在 Domain 层，便于理解和测试
- **易于维护和扩展**：修改某一层不会影响其他层
- **符合 DDD 最佳实践**：使用聚合根、值对象等 DDD 概念建模

---

## 通用响应格式

所有接口统一使用 `ResponseResult` 包装响应数据：

```json
{
  "code": 200,
  "message": "成功",
  "data": { }
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 业务状态码，200表示成功 |
| message | String | 响应消息描述 |
| data | Object | 响应数据，成功时包含具体业务数据 |

### 常见状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/认证失败 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 认证说明

### JWT 认证机制

大部分接口需要在请求头中携带 JWT Token：

```
Authorization: Bearer <your_jwt_token>
```

### 获取 Token

通过以下接口获取 Token：
- 用户注册：`POST /api/v2/auth/register`
- 用户登录：`POST /api/v2/auth/login`

### 测试模式

开发环境下，如果未提供 Token，系统会使用默认测试用户（ID=1）处理请求。生产环境请务必提供有效的 Token。

---

## API 接口列表

### 1. 认证模块

#### 1.1 用户注册

**接口说明**: 注册新用户并返回 JWT Token

- **URL**: `/api/v2/auth/register`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`

**请求参数**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "nickName": "用户昵称"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | String | 是 | 邮箱地址，需唯一 |
| password | String | 是 | 密码，建议6位以上 |
| nickName | String | 是 | 用户昵称，最多50字符 |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "nickName": "用户昵称",
      "avatar": null,
      "bio": null,
      "level": 1,
      "levelDescription": "初学者",
      "points": 0,
      "createTime": "2026-01-17T12:00:00",
      "updateTime": "2026-01-17T12:00:00"
    }
  }
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "邮箱已被注册",
  "data": null
}
```

---

#### 1.2 用户登录

**接口说明**: 用户登录并返回 JWT Token

- **URL**: `/api/v2/auth/login`
- **方法**: `POST`
- **请求头**: `Content-Type: application/json`

**请求参数**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | String | 是 | 邮箱地址 |
| password | String | 是 | 密码 |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "nickName": "用户昵称",
      "avatar": "https://example.com/avatar.jpg",
      "bio": "热爱生活，追求梦想",
      "level": 1,
      "levelDescription": "初学者",
      "points": 100,
      "createTime": "2026-01-17T12:00:00",
      "updateTime": "2026-01-17T12:00:00"
    }
  }
}
```

**错误响应**:
```json
{
  "code": 401,
  "message": "邮箱或密码错误",
  "data": null
}
```

---

### 2. 用户模块

#### 2.1 获取当前用户信息

**接口说明**: 获取当前登录用户的详细信息

- **URL**: `/api/v2/users/me`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickName": "用户昵称",
    "avatar": "https://example.com/avatar.jpg",
    "phone": null,
    "wechatOpenId": null,
    "bio": null,
    "level": 1,
    "levelDescription": "初学者",
    "points": 100,
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 2.2 获取用户资料

**接口说明**: 获取当前用户资料（与 /me 功能相同，提供备用路径）

- **URL**: `/api/v2/users/profile`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickName": "用户昵称",
    "avatar": "https://example.com/avatar.jpg",
    "phone": null,
    "wechatOpenId": null,
    "bio": null,
    "level": 1,
    "levelDescription": "初学者",
    "points": 100,
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 2.3 根据ID获取用户信息

**接口说明**: 根据用户ID获取用户信息

- **URL**: `/api/v2/users/{id}`
- **方法**: `GET`
- **路径参数**: 
  - `id` - 用户ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickName": "用户昵称",
    "avatar": "https://example.com/avatar.jpg",
    "phone": "13812345678",
    "wechatOpenId": "oXYZ123abc",
    "bio": "热爱生活，追求梦想",
    "level": 1,
    "levelDescription": "初学者",
    "points": 100,
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 2.4 更新用户信息

**接口说明**: 更新指定用户的信息

- **URL**: `/api/v2/users/{id}`
- **方法**: `PUT`
- **路径参数**: 
  - `id` - 用户ID（Long类型）
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "nickName": "新昵称",
  "avatar": "https://example.com/new-avatar.jpg"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickName": "新昵称",
    "avatar": "https://example.com/new-avatar.jpg",
    "phone": null,
    "wechatOpenId": null,
    "bio": null,
    "level": 1,
    "levelDescription": "初学者",
    "points": 100,
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:05:00"
  }
}
```

---

#### 2.5 更新用户资料

**接口说明**: 更新当前用户的资料（支持部分更新）

- **URL**: `/api/v2/users/profile`
- **方法**: `PUT`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "nickName": "新昵称",
  "avatar": "https://example.com/new-avatar.jpg",
  "phone": "13812345678",
  "wechatOpenId": "oXYZ123abc",
  "bio": "热爱生活，追求梦想"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| nickName | String | 否 | 用户昵称，最多50字符 |
| avatar | String | 否 | 头像URL（建议使用头像上传接口获取） |
| phone | String | 否 | 手机号（11位数字，1开头），为null时清空 |
| wechatOpenId | String | 否 | 微信OpenID（1-100字符），为null时清空 |
| bio | String | 否 | 个人简介，最多500字符，为null时清空 |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickName": "新昵称",
    "avatar": "https://example.com/new-avatar.jpg",
    "phone": "13812345678",
    "wechatOpenId": "oXYZ123abc",
    "bio": "热爱生活，追求梦想",
    "level": 1,
    "levelDescription": "初学者",
    "points": 100,
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-21T22:03:28"
  }
}
```

**错误响应** - 手机号格式错误:
```json
{
  "code": 400,
  "message": "手机号格式不正确",
  "data": null
}
```

**错误响应** - 微信OpenID格式错误:
```json
{
  "code": 400,
  "message": "微信OpenID长度不能超过100个字符",
  "data": null
}
```

**错误响应** - 个人简介过长:
```json
{
  "code": 400,
  "message": "个人简介长度不能超过500字符",
  "data": null
}
```

**注意事项**:
- 所有字段均为可选，未提供的字段保持不变
- 手机号、微信OpenID和个人简介可以通过传入null来清空
- 手机号仅支持中国大陆格式（11位数字，以1开头）
- 个人简介最大长度为500字符

---

#### 2.6 上传用户头像

**接口说明**: 上传头像文件到阿里云OSS并返回URL

- **URL**: `/api/v2/users/avatar`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: multipart/form-data`
  - `Authorization: Bearer <token>`

**请求参数**:
- `file` - 头像文件（FormData）

**文件要求**:
- 支持格式：JPG、JPEG、PNG、GIF
- 文件大小：最大5MB
- 存储路径：`avatars/{userId}/{timestamp}-{uuid}.{ext}`

**请求示例** (curl):
```bash
curl -X POST http://localhost:8080/api/v2/users/avatar \
  -H "Authorization: Bearer <token>" \
  -F "file=@avatar.jpg"
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": "crazydream01.oss-cn-beijing.aliyuncs.com/avatars/1/1769004251408-9c7638c021bb424da8e0b3f1f5024f1c.jpg"
}
```

**错误响应** - 不支持的文件格式:
```json
{
  "code": 400,
  "message": "不支持的文件格式，仅支持 JPG、PNG、GIF",
  "data": null
}
```

**错误响应** - 文件超大:
```json
{
  "code": 400,
  "message": "文件大小超过限制（最大5MB）",
  "data": null
}
```

**错误响应** - OSS上传失败:
```json
{
  "code": 500,
  "message": "文件上传失败，请稍后重试",
  "data": null
}
```

**使用流程**:
1. 调用此接口上传头像文件
2. 获取返回的OSS URL
3. 调用`PUT /api/v2/users/profile`接口更新avatar字段

---

#### 2.7 增加用户积分

**接口说明**: 为当前用户增加积分

- **URL**: `/api/v2/users/points`
- **方法**: `POST`
- **查询参数**: 
  - `points` - 积分值（int类型）
- **请求头**: `Authorization: Bearer <token>`

**请求示例**: `/api/v2/users/points?points=10`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": null
}
```

---

### 3. 分类模块

#### 3.1 创建分类

**接口说明**: 创建新的目标分类

- **URL**: `/api/v2/categories`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "name": "职业发展",
  "icon": "💼",
  "color": "#3498db",
  "sort": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 分类名称 |
| icon | String | 否 | 分类图标（Emoji） |
| color | String | 否 | 分类颜色（十六进制） |
| sort | Integer | 否 | 排序序号 |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "name": "职业发展",
    "icon": "💼",
    "color": "#3498db",
    "sort": 1,
    "enabled": true
  }
}
```

---

#### 3.2 获取所有分类

**接口说明**: 获取系统中所有分类列表

- **URL**: `/api/v2/categories`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "职业发展",
      "icon": "💼",
      "color": "#3498db",
      "sort": 1,
      "enabled": true
    },
    {
      "id": 2,
      "name": "学习成长",
      "icon": "📚",
      "color": "#2ecc71",
      "sort": 2,
      "enabled": true
    }
  ]
}
```

---

#### 3.3 获取启用的分类

**接口说明**: 获取所有启用状态的分类

- **URL**: `/api/v2/categories/enabled`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "职业发展",
      "icon": "💼",
      "color": "#3498db",
      "sort": 1,
      "enabled": true
    }
  ]
}
```

---

#### 3.4 更新分类

**接口说明**: 更新指定分类信息

- **URL**: `/api/v2/categories/{id}`
- **方法**: `PUT`
- **路径参数**: 
  - `id` - 分类ID（Long类型）
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "name": "职业发展-更新",
  "icon": "💼",
  "color": "#3498db"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "name": "职业发展-更新"
  }
}
```

---

#### 3.5 删除分类

**接口说明**: 删除指定分类

- **URL**: `/api/v2/categories/{id}`
- **方法**: `DELETE`
- **路径参数**: 
  - `id` - 分类ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

### 4. 目标模块

#### 4.1 创建目标

**接口说明**: 创建新的目标

- **URL**: `/api/v2/goals`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "title": "学习 Spring Boot",
  "description": "深入学习 Spring Boot 框架",
  "categoryId": 2,
  "priority": "HIGH",
  "deadline": "2026-12-31T23:59:59",
  "imageUrl": "https://example.com/image.jpg"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 目标标题 |
| description | String | 否 | 目标描述 |
| categoryId | Long | 否 | 分类ID |
| priority | String | 否 | 优先级：LOW/MEDIUM/HIGH |
| deadline | String | 否 | 截止时间（ISO 8601格式） |
| imageUrl | String | 否 | 目标图片URL |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "学习 Spring Boot",
    "description": "深入学习 Spring Boot 框架",
    "categoryId": 2,
    "priority": "HIGH",
    "deadline": "2026-12-31T23:59:59",
    "progress": 0,
    "status": "NOT_STARTED",
    "imageUrl": "https://example.com/image.jpg",
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 4.2 更新目标

**接口说明**: 更新指定目标的信息

- **URL**: `/api/v2/goals/{id}`
- **方法**: `PUT`
- **路径参数**: 
  - `id` - 目标ID（Long类型）
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "title": "学习 Spring Boot（更新）",
  "description": "深入学习 Spring Boot 框架及其生态",
  "priority": "MEDIUM"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "学习 Spring Boot（更新）",
    "description": "深入学习 Spring Boot 框架及其生态",
    "categoryId": 2,
    "priority": "MEDIUM",
    "deadline": "2026-12-31T23:59:59",
    "progress": 0,
    "status": "NOT_STARTED",
    "imageUrl": "https://example.com/image.jpg",
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:10:00"
  }
}
```

---

#### 4.3 获取用户所有目标

**接口说明**: 获取当前用户的所有目标列表

- **URL**: `/api/v2/goals`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "title": "学习 Spring Boot",
      "description": "深入学习 Spring Boot 框架",
      "categoryId": 2,
      "priority": "HIGH",
      "deadline": "2026-12-31T23:59:59",
      "progress": 30,
      "status": "IN_PROGRESS",
      "imageUrl": "https://example.com/image.jpg",
      "createTime": "2026-01-17T12:00:00",
      "updateTime": "2026-01-17T12:00:00"
    }
  ]
}
```

---

#### 4.4 根据ID获取目标

**接口说明**: 根据目标ID获取目标详情

- **URL**: `/api/v2/goals/{id}`
- **方法**: `GET`
- **路径参数**: 
  - `id` - 目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "学习 Spring Boot",
    "description": "深入学习 Spring Boot 框架",
    "categoryId": 2,
    "priority": "HIGH",
    "deadline": "2026-12-31T23:59:59",
    "progress": 30,
    "status": "IN_PROGRESS",
    "imageUrl": "https://example.com/image.jpg",
    "createTime": "2026-01-17T12:00:00",
    "updateTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 4.5 根据分类获取目标

**接口说明**: 获取指定分类下的所有目标

- **URL**: `/api/v2/goals/category/{categoryId}`
- **方法**: `GET`
- **路径参数**: 
  - `categoryId` - 分类ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "title": "学习 Spring Boot",
      "categoryId": 2,
      "priority": "HIGH",
      "progress": 30,
      "status": "IN_PROGRESS",
      "createTime": "2026-01-17T12:00:00"
    }
  ]
}
```

---

#### 4.6 删除目标

**接口说明**: 删除指定目标

- **URL**: `/api/v2/goals/{id}`
- **方法**: `DELETE`
- **路径参数**: 
  - `id` - 目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

#### 4.7 批量删除目标

**接口说明**: 批量删除多个目标

- **URL**: `/api/v2/goals/batch`
- **方法**: `DELETE`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
[1, 2, 3, 4, 5]
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": 5
}
```

---

#### 4.8 更新目标进度

**接口说明**: 更新目标的完成进度（部分更新）

- **URL**: `/api/v2/goals/{id}/progress`
- **方法**: `PATCH`
- **路径参数**: 
  - `id` - 目标ID（Long类型）
- **查询参数**: 
  - `progress` - 进度值（0-100）
- **请求头**: `Authorization: Bearer <token>`

**请求示例**: `/api/v2/goals/1/progress?progress=50`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "学习 Spring Boot",
    "progress": 50,
    "status": "IN_PROGRESS",
    "updateTime": "2026-01-17T12:20:00"
  }
}
```

---

#### 4.9 完成目标

**接口说明**: 标记目标为已完成

- **URL**: `/api/v2/goals/{id}/complete`
- **方法**: `PATCH`
- **路径参数**: 
  - `id` - 目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "学习 Spring Boot",
    "progress": 100,
    "status": "COMPLETED",
    "updateTime": "2026-01-17T12:30:00"
  }
}
```

---

#### 4.10 获取最近更新的目标

**接口说明**: 获取最近更新的目标列表

- **URL**: `/api/v2/goals/recent`
- **方法**: `GET`
- **查询参数**: 
  - `limit` - 返回数量限制（默认10）
- **请求头**: `Authorization: Bearer <token>`

**请求示例**: `/api/v2/goals/recent?limit=5`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "title": "学习 Spring Boot",
      "progress": 50,
      "updateTime": "2026-01-17T12:20:00"
    }
  ]
}
```

---

#### 4.11 获取今日提醒的目标

**接口说明**: 获取今日需要提醒的目标列表

- **URL**: `/api/v2/goals/today-reminders`
- **方法**: `GET`
- **查询参数**: 
  - `date` - 日期（可选，格式：yyyy-MM-dd，默认为当天）
- **请求头**: `Authorization: Bearer <token>`

**请求示例**: 
- `/api/v2/goals/today-reminders`
- `/api/v2/goals/today-reminders?date=2026-01-17`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "title": "学习 Spring Boot",
      "deadline": "2026-01-17T23:59:59",
      "progress": 30
    }
  ]
}
```

---

#### 4.12 获取目标统计

**接口说明**: 获取当前用户的目标统计信息

- **URL**: `/api/v2/goals/statistics`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "total": 10,
    "notStarted": 2,
    "inProgress": 5,
    "completed": 3,
    "completionRate": 30
  }
}
```

---

### 5. 子目标模块

#### 5.1 创建子目标

**接口说明**: 为目标创建子目标

- **URL**: `/api/v2/subgoals`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "goalId": 1,
  "title": "学习 Spring Boot 基础",
  "description": "完成基础教程学习"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "goalId": 1,
    "title": "学习 Spring Boot 基础",
    "description": "完成基础教程学习",
    "progress": 0,
    "status": "NOT_STARTED",
    "createTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 5.2 获取所有子目标

**接口说明**: 获取当前用户的所有子目标

- **URL**: `/api/v2/subgoals`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": []
}
```

---

#### 5.3 根据目标ID获取子目标

**接口说明**: 获取指定目标下的所有子目标

- **URL**: `/api/v2/subgoals/goal/{goalId}`
- **方法**: `GET`
- **路径参数**: 
  - `goalId` - 目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "goalId": 1,
      "title": "学习 Spring Boot 基础",
      "progress": 50,
      "status": "IN_PROGRESS"
    }
  ]
}
```

---

#### 5.4 根据ID获取子目标

**接口说明**: 根据子目标ID获取详情

- **URL**: `/api/v2/subgoals/{id}`
- **方法**: `GET`
- **路径参数**: 
  - `id` - 子目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "goalId": 1,
    "title": "学习 Spring Boot 基础",
    "description": "完成基础教程学习",
    "progress": 50,
    "status": "IN_PROGRESS",
    "createTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 5.5 更新子目标

**接口说明**: 更新子目标信息

- **URL**: `/api/v2/subgoals/{id}`
- **方法**: `PUT`
- **路径参数**: 
  - `id` - 子目标ID（Long类型）
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "progress": 75
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "goalId": 1,
    "title": "学习 Spring Boot 基础",
    "progress": 75,
    "status": "IN_PROGRESS"
  }
}
```

---

#### 5.6 完成子目标

**接口说明**: 标记子目标为已完成

- **URL**: `/api/v2/subgoals/{id}/complete`
- **方法**: `PATCH`
- **路径参数**: 
  - `id` - 子目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "goalId": 1,
    "title": "学习 Spring Boot 基础",
    "progress": 100,
    "status": "COMPLETED"
  }
}
```

---

#### 5.7 更新子目标进度

**接口说明**: 更新子目标的完成进度（部分更新）

- **URL**: `/api/v2/subgoals/{id}/progress`
- **方法**: `PATCH`
- **路径参数**: 
  - `id` - 子目标ID（Long类型）
- **查询参数**: 
  - `progress` - 进度值（0-100）
- **请求头**: `Authorization: Bearer <token>`

**请求示例**: `/api/v2/subgoals/1/progress?progress=80`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "goalId": 1,
    "title": "学习 Spring Boot 基础",
    "progress": 80,
    "status": "IN_PROGRESS"
  }
}
```

---

#### 5.8 删除子目标

**接口说明**: 删除指定子目标

- **URL**: `/api/v2/subgoals/{id}`
- **方法**: `DELETE`
- **路径参数**: 
  - `id` - 子目标ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

#### 5.9 批量删除子目标

**接口说明**: 批量删除多个子目标

- **URL**: `/api/v2/subgoals/batch`
- **方法**: `DELETE`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
[1, 2, 3]
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": 3
}
```

---

### 6. 统计模块

#### 6.1 获取目标统计

**接口说明**: 获取用户目标的统计数据

- **URL**: `/api/v2/statistics/goals`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "total": 10,
    "notStarted": 2,
    "inProgress": 5,
    "completed": 3,
    "completionRate": 30
  }
}
```

---

#### 6.2 获取仪表盘统计

**接口说明**: 获取用户仪表盘的综合统计数据

- **URL**: `/api/v2/statistics/dashboard`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "totalGoals": 10,
    "completedGoals": 3,
    "inProgressGoals": 5,
    "totalSubGoals": 20,
    "completedSubGoals": 8,
    "totalPoints": 100,
    "level": 1,
    "todayReminders": 2
  }
}
```

---

#### 6.3 获取趋势统计

**接口说明**: 获取用户目标完成趋势数据

- **URL**: `/api/v2/statistics/trends`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "dates": ["2026-01-10", "2026-01-11", "2026-01-12"],
    "completedCounts": [1, 2, 0],
    "createdCounts": [2, 1, 3]
  }
}
```

---

#### 6.4 获取分类统计

**接口说明**: 获取各分类下的目标统计数据

- **URL**: `/api/v2/statistics/categories`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "categoryId": 1,
      "categoryName": "职业发展",
      "totalGoals": 5,
      "completedGoals": 2,
      "completionRate": 40
    },
    {
      "categoryId": 2,
      "categoryName": "学习成长",
      "totalGoals": 5,
      "completedGoals": 1,
      "completionRate": 20
    }
  ]
}
```

---

### 7. 成就模块

#### 7.1 获取用户所有成就

**接口说明**: 获取用户的所有成就记录（首次访问时会自动为用户创建所有成就类型记录）

- **URL**: `/api/v2/achievements`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "type": "first_goal",
      "typeName": "首个目标",
      "description": "创建第一个目标",
      "unlocked": true,
      "unlockedTime": "2026-01-15T10:00:00",
      "createTime": "2026-01-12T17:29:23"
    },
    {
      "id": 2,
      "userId": 1,
      "type": "goal_10",
      "typeName": "小有成就",
      "description": "完成10个目标",
      "unlocked": true,
      "unlockedTime": "2026-01-20T21:55:10",
      "createTime": "2026-01-12T17:29:23"
    },
    {
      "id": 3,
      "userId": 1,
      "type": "consecutive_7",
      "typeName": "七日坚持",
      "description": "连续7天完成目标",
      "unlocked": false,
      "unlockedTime": null,
      "createTime": "2026-01-20T22:49:38"
    }
  ]
}
```

**成就字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 成就ID |
| userId | Long | 用户ID |
| type | String | 成就类型代码 |
| typeName | String | 成就名称 |
| description | String | 成就描述 |
| unlocked | Boolean | 是否已解锁 |
| unlockedTime | DateTime | 解锁时间（未解锁时为null） |
| createTime | DateTime | 创建时间 |

**成就类型列表**:

##### 目标完成数量系列
- `first_goal` - 首个目标：创建第一个目标
- `goal_10` - 小有成就：完成10个目标
- `goal_30` - 初露锋芒：完成30个目标
- `goal_50` - 坚持不懈：完成50个目标
- `goal_100` - 成就达人：完成100个目标
- `goal_200` - 目标大师：完成200个目标

##### 连续打卡系列
- `consecutive_3` - 三日坚持：连续3天完成目标
- `consecutive_7` - 七日坚持：连续7天完成目标
- `consecutive_14` - 两周习惯：连续14天完成目标
- `consecutive_30` - 月度冠军：连续30天完成目标
- `consecutive_100` - 百日传奇：连续100天完成目标

##### 分类专注系列
- `category_master_10` - 分类达人：在单个分类完成10个目标
- `category_master_30` - 分类专家：在单个分类完成30个目标
- `all_category_explorer` - 全能选手：在所有分类都至少完成1个目标

##### 效率提升系列
- `early_bird` - 早起鸟：早上6-8点完成5个目标
- `night_owl` - 夜猫子：晚上22-24点完成5个目标
- `speed_master` - 效率达人：创建目标后24小时内完成，累计10次
- `deadline_keeper` - 守时之星：提前完成有截止日期的目标，累计20次

##### 里程碑系列
- `first_week` - 初入殿堂：使用系统满7天
- `first_month` - 月度会员：使用系统满30天
- `one_year` - 年度坚持：使用系统满365天
- `high_completion_rate` - 完美主义者：目标完成率达到90%，且完成目标数>=20

##### 等级提升
- `level_up` - 等级提升：用户等级提升

---

#### 7.2 获取已解锁成就

**接口说明**: 获取用户已解锁的成就列表

- **URL**: `/api/v2/achievements/unlocked`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "type": "first_goal",
      "typeName": "首个目标",
      "description": "创建第一个目标",
      "unlocked": true,
      "unlockedTime": "2026-01-15T10:00:00",
      "createTime": "2026-01-12T17:29:23"
    },
    {
      "id": 2,
      "userId": 1,
      "type": "goal_10",
      "typeName": "小有成就",
      "description": "完成10个目标",
      "unlocked": true,
      "unlockedTime": "2026-01-20T21:55:10",
      "createTime": "2026-01-12T17:29:23"
    }
  ]
}
```

---

#### 7.3 解锁成就

**接口说明**: 手动解锁指定成就。系统通常会自动检查并解锁成就，此接口主要用于管理员手动触发或测试。

- **URL**: `/api/v2/achievements/unlock`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "achievementId": 1,
  "userId": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| achievementId | Long | 是 | 数据库中的成就记录ID |
| userId | Long | 否 | 用户ID（默认为当前登录用户） |

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "type": "first_goal",
    "typeName": "首个目标",
    "description": "创建第一个目标",
    "unlocked": true,
    "unlockedTime": "2026-01-17T12:00:00",
    "createTime": "2026-01-12T17:29:23"
  }
}
```

---

### 8. 提醒模块

#### 8.1 创建提醒

**接口说明**: 创建新的提醒记录

- **URL**: `/api/v2/reminders`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: application/json`
  - `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "goalId": 1,
  "content": "记得完成 Spring Boot 学习",
  "remindTime": "2026-01-18T10:00:00"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "goalId": 1,
    "content": "记得完成 Spring Boot 学习",
    "remindTime": "2026-01-18T10:00:00",
    "isRead": false,
    "createTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 8.2 获取用户所有提醒

**接口说明**: 获取当前用户的所有提醒

- **URL**: `/api/v2/reminders`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "goalId": 1,
      "content": "记得完成 Spring Boot 学习",
      "remindTime": "2026-01-18T10:00:00",
      "isRead": false,
      "createTime": "2026-01-17T12:00:00"
    }
  ]
}
```

---

#### 8.3 获取未读提醒

**接口说明**: 获取当前用户的未读提醒列表

- **URL**: `/api/v2/reminders/unread`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "goalId": 1,
      "content": "记得完成 Spring Boot 学习",
      "remindTime": "2026-01-18T10:00:00",
      "isRead": false
    }
  ]
}
```

---

#### 8.4 标记提醒已读

**接口说明**: 将指定提醒标记为已读

- **URL**: `/api/v2/reminders/{id}/read`
- **方法**: `PATCH`
- **路径参数**: 
  - `id` - 提醒ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "goalId": 1,
    "content": "记得完成 Spring Boot 学习",
    "remindTime": "2026-01-18T10:00:00",
    "isRead": true
  }
}
```

---

#### 8.5 删除提醒

**接口说明**: 删除指定提醒

- **URL**: `/api/v2/reminders/{id}`
- **方法**: `DELETE`
- **路径参数**: 
  - `id` - 提醒ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

### 9. 文件模块

#### 9.1 上传文件

**接口说明**: 上传文件到服务器

- **URL**: `/api/v2/files/upload`
- **方法**: `POST`
- **请求头**: 
  - `Content-Type: multipart/form-data`
  - `Authorization: Bearer <token>`

**请求参数**:
- `file` - 文件（MultipartFile类型）

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": null,
    "fileName": "example.jpg",
    "fileSize": 102400,
    "fileUrl": "/uploads/example.jpg"
  }
}
```

---

#### 9.2 获取用户所有文件

**接口说明**: 获取当前用户上传的所有文件

- **URL**: `/api/v2/files`
- **方法**: `GET`
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "fileName": "example.jpg",
      "fileSize": 102400,
      "fileUrl": "/uploads/example.jpg",
      "uploadTime": "2026-01-17T12:00:00"
    }
  ]
}
```

---

#### 9.3 根据ID获取文件

**接口说明**: 根据文件ID获取文件信息

- **URL**: `/api/v2/files/{id}`
- **方法**: `GET`
- **路径参数**: 
  - `id` - 文件ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "userId": 1,
    "fileName": "example.jpg",
    "fileSize": 102400,
    "fileUrl": "/uploads/example.jpg",
    "uploadTime": "2026-01-17T12:00:00"
  }
}
```

---

#### 9.4 删除文件

**接口说明**: 删除指定文件

- **URL**: `/api/v2/files/{id}`
- **方法**: `DELETE`
- **路径参数**: 
  - `id` - 文件ID（Long类型）
- **请求头**: `Authorization: Bearer <token>`

**响应示例**:
```json
{
  "code": 200,
  "message": "成功",
  "data": true
}
```

---

## 数据模型

### UserDTO（用户数据传输对象）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户ID |
| email | String | 邮箱地址 |
| nickName | String | 用户昵称 |
| avatar | String | 头像URL |
| bio | String | 个人简介 |
| level | Integer | 用户等级 |
| levelDescription | String | 等级描述 |
| points | Integer | 积分 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

---

### GoalDTO（目标数据传输对象）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 目标ID |
| userId | Long | 用户ID |
| title | String | 目标标题 |
| description | String | 目标描述 |
| categoryId | Long | 分类ID |
| priority | String | 优先级（LOW/MEDIUM/HIGH） |
| deadline | LocalDateTime | 截止时间 |
| progress | Integer | 完成进度（0-100） |
| status | String | 状态（NOT_STARTED/IN_PROGRESS/COMPLETED） |
| imageUrl | String | 目标图片URL |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

---

### CategoryDTO（分类数据传输对象）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 分类ID |
| name | String | 分类名称 |
| icon | String | 分类图标 |
| color | String | 分类颜色 |
| sort | Integer | 排序序号 |
| enabled | Boolean | 是否启用 |

---

### SubGoalDTO（子目标数据传输对象）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 子目标ID |
| goalId | Long | 所属目标ID |
| title | String | 子目标标题 |
| description | String | 子目标描述 |
| progress | Integer | 完成进度（0-100） |
| status | String | 状态 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

---

## 错误处理

### 错误响应格式

```json
{
  "code": 400,
  "message": "错误描述信息",
  "data": null
}
```

### 常见错误码及处理

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 400 | 请求参数错误 | 检查请求参数格式和必填字段 |
| 401 | 未授权 | 检查 Token 是否有效或已过期 |
| 404 | 资源不存在 | 确认请求的资源ID是否正确 |
| 500 | 服务器内部错误 | 联系后端开发人员排查 |

---

## 版本更新日志

### v3.2.0 (2026-01-26)

**新增功能**:
- 用户资料管理增强：新增个人简介(bio)字段
  - 支持在用户资料更新接口中添加/修改个人简介
  - 个人简介最大长度500字符
  - 支持通过传入null清空个人简介
  - 所有用户信息查询接口响应中包含bio字段

**数据模型更新**:
- UserDTO新增bio字段
- UpdateProfileCommand新增bio字段
- User聚合根新增bio值对象
- 数据库user表新增bio列

### v3.0.0 (2026-01-20)

**重大变更**:
- 全面升级为 DDD + COLA 四层架构
- 所有接口路径统一使用 `/api/v2/*` 前缀
- 优化数据模型，引入聚合根和值对象概念
- **成就系统深度扩展**: 成就类型从 6 种扩展到 23 种，涵盖完成数、连续打卡、分类专注、效率、里程碑等维度

**新增功能**:
- 新增 `/api/v2/users/profile` 接口（解决路径冲突）
- 新增 `/api/v2/goals/today-reminders` 支持可选日期参数
- 新增完整的统计模块接口（dashboard、trends、categories）
- **成就系统联动**: 在目标创建、完成、进度更新时自动触发成就检查
- **数据自动补齐**: 用户访问成就列表时，系统会自动检测并持久化该用户缺失的新成就记录

**接口优化**:
- 优化目标和子目标的进度更新、完成接口，统一改用 PATCH 方法（符合 RESTful 规范）
- 优化日期参数处理，支持默认值（当前日期）
- 统一错误响应格式和状态码
- **统计下沉**: 将核心统计（如完成率、有效目标数）从内存计算下沉到 SQL 统计，提升性能

**架构改进**:
- 引入 Application Service 层，分离业务编排逻辑
- 引入 `AchievementStatisticsService` 负责跨领域（Goal, User）数据采集并构建统计快照
- 引入 Assembler 模式，规范 DTO 与领域模型转换
- 优化 Repository 实现，分离持久化逻辑，支持复杂的 SQL 统计下沉逻辑

**文档更新**:
- 新增架构说明章节
- 完善所有接口的请求参数和响应示例
- 新增数据模型定义
- 新增错误处理说明

---

## 兼容性说明

### 推荐使用（v2 新架构接口）

所有 `/api/v2/*` 路径下的接口均为新架构接口，推荐前端优先使用：

✅ **推荐使用的接口前缀**:
- `/api/v2/auth/*` - 认证模块
- `/api/v2/users/*` - 用户模块
- `/api/v2/categories/*` - 分类模块
- `/api/v2/goals/*` - 目标模块
- `/api/v2/subgoals/*` - 子目标模块
- `/api/v2/statistics/*` - 统计模块
- `/api/v2/achievements/*` - 成就模块
- `/api/v2/reminders/*` - 提醒模块
- `/api/v2/files/*` - 文件模块

### 已废弃（旧架构接口）

⚠️ **已废弃的接口**（不再维护，建议迁移）:
- `/api/auth/*` - 旧认证接口
- `/api/user/*` - 旧用户接口
- `/api/goal/*` - 旧目标接口
- `/api/category/*` - 旧分类接口

---

## 开发建议

1. **始终使用 v2 接口**：新功能开发请使用 `/api/v2/*` 接口
2. **携带认证 Token**：除注册和登录外，其他接口都需要携带 JWT Token
3. **检查业务状态码**：除了 HTTP 状态码，还要检查响应体中的 `code` 字段
4. **遵循 RESTful 规范**：使用正确的 HTTP 方法（GET/POST/PUT/PATCH/DELETE）
5. **合理使用 PATCH**：部分更新操作使用 PATCH，完整更新使用 PUT

---

## 联系方式

如有问题或建议，请联系后端开发团队。

**技术栈**:
- Java 17
- Spring Boot 3.2.0
- MyBatis
- MySQL 8.0
- JWT

**项目地址**: CrazyDream 目标管理系统

---

*文档最后更新时间: 2026-01-26 22:50:00*
