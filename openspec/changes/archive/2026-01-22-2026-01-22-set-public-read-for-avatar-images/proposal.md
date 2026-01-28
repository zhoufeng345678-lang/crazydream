# Change: 设置头像图片为OSS公共读权限

## Why

当前OSS头像上传功能虽然能成功上传图片到阿里云OSS并返回URL，但上传的图片默认使用私有读权限，导致前端无法直接通过URL访问显示头像图片。用户需要通过签名URL或其他方式才能访问，这增加了系统复杂度且影响用户体验。

具体问题：
1. 头像图片应该是公开可访问的资源，不需要权限验证
2. 前端直接使用返回的URL无法加载图片
3. 当前实现没有根据文件类型设置合适的访问权限

## What Changes

### 功能增强
- 在上传头像图片到OSS后，自动检测文件是否为图片格式（JPG、JPEG、PNG、GIF）
- 对图片格式文件设置公共读权限（PublicRead），允许匿名访问
- 非图片格式文件保持默认的私有权限
- 保持现有的错误处理和日志记录机制

### 实现变更
- 修改 `OssService.uploadAvatar()` 方法，在 `putObject` 成功后增加ACL设置逻辑
- 使用阿里云OSS SDK的 `setObjectAcl()` API设置对象访问控制
- 复用现有的 `ALLOWED_EXTENSIONS` 列表来判断是否为图片格式

**BREAKING**: 无破坏性变更（仅增强现有功能）

## Impact

### 受影响的规范（Specs）
- `user-management` - 修改Requirement UPE-003，增加OSS公共读权限管理需求

### 受影响的代码
- `infrastructure/oss/OssService.java` - 修改 `uploadAvatar` 方法，增加ACL设置逻辑

### 依赖变更
- 无新增依赖（使用现有的阿里云OSS SDK 3.17.1）

### 配置变更
- 无需修改配置文件

## Timeline

预计实现时间：30分钟
- 修改OssService.java的uploadAvatar方法
- 添加图片格式检测和ACL设置逻辑
- 验证功能正常工作
