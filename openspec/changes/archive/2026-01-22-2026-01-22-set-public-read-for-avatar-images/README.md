# 设置头像图片为OSS公共读权限

## 概述

本变更为OSS头像上传功能添加公共读权限设置，确保上传的图片文件可以被前端直接访问，无需认证。

## 问题背景

当前实现中，上传到OSS的头像图片使用默认的私有读权限，导致前端无法直接通过URL访问图片，影响用户体验。

## 解决方案

在上传图片到OSS后，自动检测文件类型，对图片格式（JPG、JPEG、PNG、GIF）设置公共读权限（PublicRead）。

## 关键文件

- **proposal.md** - 完整的变更提案
- **tasks.md** - 实现任务清单
- **specs/file-upload-management/spec.md** - 功能规范和场景

## 实现要点

1. 使用阿里云OSS SDK的 `setObjectAcl()` API
2. 仅对ALLOWED_EXTENSIONS中的图片格式设置公共读
3. ACL设置失败不影响主流程，记录日志即可

## 验证方法

上传图片后，在浏览器中直接访问返回的URL，应能正常显示图片。
