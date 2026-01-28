# user-management Specification

## Purpose
定义用户资料管理的功能需求，包括用户信息编辑、手机号和微信OpenID管理、头像上传等核心能力。

## ADDED Requirements

### Requirement: UPE-003 头像文件上传到OSS

系统 SHALL 提供独立的头像文件上传接口，自动上传到阿里云OSS并返回存储URL，并且 SHALL 为图片格式文件设置公共读权限以便前端直接访问。

系统 SHALL 仅支持的文件格式：JPG, JPEG, PNG, GIF。文件大小 SHALL 限制为最大5MB。OSS存储路径 SHALL 遵循规则：`avatars/{userId}/{timestamp}-{uuid}.{ext}`。上传成功后系统 SHALL 返回完整的OSS访问URL。

上传成功后，系统 SHALL 检查文件扩展名是否为图片格式（jpg, jpeg, png, gif）。对于图片格式文件，系统 SHALL 设置OSS对象ACL为PublicRead。ACL设置失败 SHALL NOT 阻止上传流程，系统 SHALL 记录错误日志但仍返回URL。

#### Scenario: 成功上传头像文件并设置公共读权限

**Given** 用户已登录，用户ID为123  
**And** 用户准备上传一个有效的PNG图片文件（2MB）  
**When** 用户调用头像上传接口 `POST /api/v2/users/avatar`  
**Then** 系统应：
  - 验证文件格式为PNG
  - 验证文件大小小于5MB
  - 生成唯一文件名（如 `avatars/123/1706000000000-a1b2c3d4.png`）
  - 上传文件到阿里云OSS
  - 检测到文件扩展名为"png"（图片格式）
  - 调用OSS SDK设置对象ACL为PublicRead
  - 记录日志：成功设置公共读权限
  - 返回完整的OSS URL（如 `https://bucket.oss-cn-hangzhou.aliyuncs.com/avatars/123/1706000000000-a1b2c3d4.png`）

**And** 返回的URL可在浏览器中直接访问，无需任何认证

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

#### Scenario: ACL设置失败但上传成功

**Given** 用户已登录并提交有效的PNG图片文件  
**And** OSS上传成功，但setObjectAcl调用失败（权限不足或临时错误）  
**When** 系统尝试设置ACL时发生异常  
**Then** 系统应：
  - 捕获ACL设置异常
  - 记录错误日志："设置OSS对象公共读权限失败: {错误信息}"
  - 不抛出异常，继续执行
  - 正常返回OSS文件URL给用户

**Note**: ACL设置失败不应中断用户上传流程

---
