## 1. 领域层扩展
- [ ] 1.1 创建Phone值对象 (`domain/user/model/valueobject/Phone.java`)
  - 手机号格式验证（支持中国手机号格式）
  - 不可变性保证
- [ ] 1.2 创建WechatOpenId值对象 (`domain/user/model/valueobject/WechatOpenId.java`)
  - OpenID格式验证
  - 不可变性保证
- [ ] 1.3 更新User聚合根
  - 添加phone和wechatOpenId字段
  - 更新updateProfile方法签名，支持新字段
  - 更新rebuild工厂方法
- [ ] 1.4 编写值对象单元测试

## 2. 应用层扩展
- [ ] 2.1 更新UserDTO
  - 添加phone和wechatOpenId字段
- [ ] 2.2 更新UpdateProfileCommand
  - 添加phone和wechatOpenId字段
  - 添加字段验证注解
- [ ] 2.3 创建AvatarUploadCommand DTO
  - 文件类型、大小限制参数
- [ ] 2.4 更新UserAssembler
  - 支持新字段的双向转换
- [ ] 2.5 更新UserApplicationService
  - 修改updateProfile方法，处理新字段
  - 新增uploadAvatar方法，集成OSS服务

## 3. 基础设施层实现
- [ ] 3.1 实现OssService (`infrastructure/oss/OssService.java`)
  - 实现文件上传到阿里云OSS
  - 生成唯一文件名（UUID + 时间戳）
  - 返回公开访问URL
  - 添加文件类型和大小验证
  - 配置文件路径规则（avatars/{userId}/{filename}）
- [ ] 3.2 更新UserPO
  - 添加phone和openid字段映射
- [ ] 3.3 更新UserConverter
  - 支持新字段转换
- [ ] 3.4 更新UserPersistenceMapper.xml
  - 更新SQL语句包含新字段
- [ ] 3.5 验证数据库schema
  - 确认user表已有phone和openid列

## 4. 接口层实现
- [ ] 4.1 更新UserController
  - 修改updateProfile方法处理新字段
  - 新增POST /api/v2/users/avatar上传接口
  - 添加MultipartFile文件上传参数
  - 文件大小限制（如5MB）
  - 文件类型验证（仅允许jpg, png, gif）
- [ ] 4.2 添加接口参数验证
  - 手机号格式校验
  - OpenID格式校验
  - 文件类型和大小校验

## 5. 配置和环境
- [ ] 5.1 确认application.yml中OSS配置完整
  - endpoint, bucketName, accessKeyId, accessKeySecret
- [ ] 5.2 添加文件上传相关配置
  - spring.servlet.multipart.max-file-size
  - spring.servlet.multipart.max-request-size

## 6. 测试
- [ ] 6.1 单元测试
  - Phone值对象测试（有效/无效格式）
  - WechatOpenId值对象测试
  - User聚合根新方法测试
  - OssService上传逻辑测试（Mock OSS客户端）
- [ ] 6.2 集成测试
  - 更新用户资料接口测试（包含新字段）
  - 头像上传接口测试
- [ ] 6.3 API测试脚本
  - 更新comprehensive_v2_api_test.py
  - 添加头像上传测试用例

## 7. 文档更新
- [ ] 7.1 更新api-documentation.md
  - 更新用户资料接口文档，说明新增字段
  - 添加头像上传接口文档
- [ ] 7.2 更新README（如需要）

## 验证标准

- [ ] 所有单元测试通过
- [ ] 所有集成测试通过
- [ ] API测试覆盖率达到100%
- [ ] 手机号验证准确性100%
- [ ] 文件上传成功率100%（在测试环境）
- [ ] OSS URL可公开访问
- [ ] 无数据库迁移错误
- [ ] 无破坏性变更，现有功能正常运行
