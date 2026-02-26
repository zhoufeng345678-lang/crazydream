## 1. 基础设施准备

### 1.1 数据库索引创建
- [ ] 在user表的wechat_open_id字段上创建唯一索引 `idx_wechat_open_id`
- [ ] 验证索引创建成功并测试查询性能

### 1.2 配置微信开放平台
- [ ] 在application.yml中添加wechat配置节（appId、appSecret、apiUrl、timeout）
- [ ] 创建WechatConfig配置类读取配置项
- [ ] 添加.env.example文档说明环境变量

### 1.3 依赖检查
- [ ] 确认RestTemplate或其他HTTP客户端已配置
- [ ] 确认Jackson JSON解析库可用

## 2. 领域层扩展

### 2.1 User聚合根
- [ ] 在User类中添加`createByWechat(WechatOpenId)`静态工厂方法
- [ ] 实现逻辑：生成默认昵称"微信用户"、生成唯一邮箱、设置空密码
- [ ] 添加单元测试验证工厂方法

### 2.2 UserRepository接口
- [ ] 在UserRepository接口中添加`Optional<User> findByWechatOpenId(WechatOpenId)`方法签名
- [ ] 更新接口文档注释

## 3. 基础设施层实现

### 3.1 MyBatis Mapper扩展
- [ ] 在UserPersistenceMapper接口中添加`findByWechatOpenId(String)`方法
- [ ] 在UserPersistenceMapper.xml中添加对应的SELECT查询SQL
- [ ] 编写Mapper单元测试

### 3.2 UserRepositoryImpl实现
- [ ] 实现`findByWechatOpenId()`方法，调用Mapper并转换PO到领域对象
- [ ] 处理异常情况返回Optional.empty()

### 3.3 WechatService创建
- [ ] 创建`WechatService`类（放在infrastructure/wechat包下）
- [ ] 注入WechatConfig和RestTemplate
- [ ] 实现`getOpenIdByCode(String code)`方法：
  - 构建微信API请求URL
  - 调用微信code2Session接口
  - 解析响应JSON获取openid
  - 处理微信API错误码（-1系统繁忙、40029 code无效等）
  - 设置3秒超时
- [ ] 编写单元测试（Mock RestTemplate）

## 4. 应用层实现

### 4.1 DTO创建
- [ ] 创建`WechatLoginCommand`类，包含code字段
- [ ] 添加参数校验注解@NotBlank

### 4.2 UserApplicationService扩展
- [ ] 添加`LoginResponse wechatLogin(String code)`方法
- [ ] 实现逻辑：
  - 调用WechatService获取openid
  - 调用UserRepository.findByWechatOpenId查询用户
  - 如用户不存在，调用User.createByWechat创建新用户并保存
  - 如用户存在，直接使用
  - 生成JWT token
  - 封装LoginResponse返回
- [ ] 添加@Transactional注解
- [ ] 编写单元测试（Mock依赖）

## 5. 接口层实现

### 5.1 AuthController扩展
- [ ] 添加`@PostMapping("/wechat-login")`接口方法
- [ ] 接收`@RequestBody WechatLoginCommand`参数
- [ ] 调用`userApplicationService.wechatLogin()`
- [ ] 统一异常处理：
  - 400: code为空或格式错误
  - 401: 微信授权失败
  - 500: 系统异常
- [ ] 返回ResponseResult封装的LoginResponse

### 5.2 SecurityConfig更新
- [ ] 在白名单中添加`/api/v2/auth/wechat-login`路径
- [ ] 在JwtAuthenticationFilter中添加该路径到跳过列表
- [ ] 验证配置生效

## 6. 测试

### 6.1 单元测试
- [ ] WechatService单元测试（Mock微信API）
- [ ] UserApplicationService.wechatLogin单元测试
- [ ] User.createByWechat单元测试
- [ ] AuthController.wechatLogin单元测试
- [ ] 确保测试覆盖率 > 80%

### 6.2 集成测试
- [ ] 使用微信测试号获取真实code进行端到端测试
- [ ] 测试场景1：新用户首次登录（自动注册）
- [ ] 测试场景2：已有用户登录（直接返回token）
- [ ] 测试场景3：无效code（返回401错误）
- [ ] 测试场景4：微信API超时（返回500错误）
- [ ] 验证生成的JWT token可正常访问受保护接口

### 6.3 性能测试
- [ ] 测试微信登录接口响应时间 < 2秒
- [ ] 测试并发10个请求的表现

## 7. 文档更新

### 7.1 API文档
- [ ] 在api-documentation.md的认证模块添加"微信一键登录"章节
- [ ] 包含：接口说明、请求参数、响应示例、错误响应
- [ ] 更新版本号和更新日志

### 7.2 配置文档
- [ ] 更新README.md添加微信登录配置说明
- [ ] 更新.env.example添加WECHAT_APP_ID和WECHAT_APP_SECRET

### 7.3 Postman测试集
- [ ] 添加微信登录接口到Postman collection
- [ ] 提供测试用code示例

## 8. 部署准备

### 8.1 环境配置
- [ ] 在测试环境配置微信测试号AppID和AppSecret
- [ ] 在生产环境配置正式微信AppID和AppSecret（使用环境变量）
- [ ] 验证配置加载正确

### 8.2 数据库迁移
- [ ] 编写数据库迁移脚本（CREATE INDEX）
- [ ] 在测试环境执行并验证
- [ ] 准备回滚脚本

### 8.3 监控告警
- [ ] 添加微信API调用失败的日志记录
- [ ] 配置告警规则（微信API错误率 > 10%）

## 9. 验收测试

### 9.1 功能验收
- [ ] 前端集成测试：完整的微信登录流程
- [ ] 验证自动注册用户的默认数据正确
- [ ] 验证已有用户登录返回正确的用户信息
- [ ] 验证JWT token有效期和刷新机制

### 9.2 安全验证
- [ ] 确认wechat_open_id字段有UNIQUE约束
- [ ] 确认微信AppSecret不会泄露到日志或响应中
- [ ] 确认微信code不可重复使用

### 9.3 兼容性验证
- [ ] 验证邮箱密码登录功能不受影响
- [ ] 验证已有用户可正常使用所有功能

## 10. 归档

- [ ] 所有任务完成后，执行`openspec archive add-wechat-login`
- [ ] 更新`openspec/specs/user-management/spec.md`合并delta
- [ ] 删除技术方案设计.md临时文档
