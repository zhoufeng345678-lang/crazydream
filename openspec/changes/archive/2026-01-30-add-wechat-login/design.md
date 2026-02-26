## Context

当前系统已实现邮箱密码登录体系，采用DDD四层架构(Interface → Application → Domain → Infrastructure)。User聚合根已包含`wechatOpenId`字段但未实现微信登录功能。系统使用JWT进行身份认证，数据持久化采用MyBatis + MySQL。

### 技术栈
- Spring Boot 3.2.0
- Java 17
- MyBatis 3.0.3
- MySQL 8.0
- JWT (jjwt 0.12.3)
- Spring Security

### 现有能力
- 邮箱密码注册/登录
- JWT token生成和验证
- User领域模型包含wechatOpenId字段
- 用户自动注册逻辑(register方法)

## Goals / Non-Goals

### Goals
1. 实现微信OAuth2.0一键登录功能
2. 支持新用户自动注册(首次登录创建账号)
3. 支持老用户通过wechatOpenId直接登录
4. 保持与现有邮箱登录体系的兼容性
5. 遵循项目DDD架构模式

### Non-Goals
- 不实现微信账号与邮箱账号的绑定功能(Phase 2)
- 不实现微信用户信息同步(头像、昵称)(Phase 2)
- 不支持微信公众号登录，仅支持小程序登录
- 不实现微信支付功能

## Decisions

### 决策1: HTTP客户端选择

**选择**: 使用Spring Boot内置的RestTemplate

**理由**:
- 项目已有RestTemplate配置，无需引入新依赖
- 对于简单的HTTP GET请求足够
- 团队熟悉度高

**备选方案**:
- WebClient (Spring WebFlux): 支持响应式编程，但项目未采用响应式架构，引入复杂度不值得
- OkHttp: 需要额外依赖，RestTemplate已满足需求

### 决策2: 微信用户邮箱生成策略

**选择**: 生成格式为 `wechat_{openid}@crazydream.com` 的虚拟邮箱

**理由**:
- email字段在数据库中定义为NOT NULL UNIQUE，必须提供唯一值
- openid是微信用户的唯一标识，保证生成的邮箱唯一
- 前缀`wechat_`便于识别微信登录用户
- 后续可支持用户绑定真实邮箱

**备选方案**:
- 使用微信UnionID: UnionID仅在用户授权多个应用时才返回，不可靠
- 允许email为NULL: 需要修改数据库表结构，影响现有逻辑

### 决策3: 微信登录用户密码处理

**选择**: password字段设置为null

**理由**:
- 微信登录用户无需密码
- null值可区分微信登录和邮箱登录用户
- 后续支持密码登录时可要求用户设置密码

**备选方案**:
- 生成随机密码: 用户不知道密码无意义，且增加安全风险

### 决策4: 微信API调用超时设置

**选择**: 3秒超时

**理由**:
- 微信API通常响应时间在500ms-1s
- 3秒足够覆盖网络波动情况
- 避免用户等待时间过长影响体验

**备选方案**:
- 5秒: 过长，影响用户体验
- 1秒: 过短，网络波动时容易超时

### 决策5: WechatService放置位置

**选择**: 放在Infrastructure层 (`infrastructure/wechat/WechatService`)

**理由**:
- WechatService封装外部API调用，属于基础设施层职责
- 与OssService位置一致，保持架构一致性
- 便于Mock和单元测试

**备选方案**:
- 放在Application层: 不符合DDD分层原则，Application层不应直接调用外部API

## Risks / Trade-offs

### 风险1: 微信API调用失败

**风险级别**: 中

**影响**: 用户无法登录

**缓解措施**:
- 设置3秒超时，避免长时间等待
- 返回友好错误提示"微信服务暂时不可用，请稍后重试"
- 记录详细日志便于排查问题
- 配置监控告警(错误率 > 10%)

### 风险2: OpenID重复导致数据一致性问题

**风险级别**: 低

**影响**: 用户账号混乱

**缓解措施**:
- 数据库wechat_open_id字段添加UNIQUE约束
- 代码层面捕获唯一性冲突异常并返回友好提示

### 风险3: 测试环境AppID泄露

**风险级别**: 中

**影响**: AppSecret被滥用，影响微信登录服务

**缓解措施**:
- 使用微信测试号，与生产环境隔离
- 生产环境通过环境变量配置，不提交到代码仓库
- 定期轮换AppSecret

### 权衡1: 自动注册 vs 显式注册

**选择**: 自动注册

**权衡**:
- 优点: 用户体验好，一键登录无需额外步骤
- 缺点: 用户可能不知道已创建账号

**决定**: 采用自动注册，在首次登录后引导用户完善资料

### 权衡2: 同步调用微信API vs 异步调用

**选择**: 同步调用

**权衡**:
- 优点: 实现简单，逻辑清晰
- 缺点: 增加接口响应时间(约500ms-1s)

**决定**: 采用同步调用，微信API响应时间可接受，无需引入异步复杂度

## Migration Plan

### 步骤1: 数据库迁移

**操作**:
```sql
CREATE UNIQUE INDEX idx_wechat_open_id ON user(wechat_open_id);
```

**验证**:
```sql
SHOW INDEX FROM user WHERE Key_name = 'idx_wechat_open_id';
```

**回滚**:
```sql
DROP INDEX idx_wechat_open_id ON user;
```

### 步骤2: 配置迁移

**操作**: 在application.yml中添加wechat配置节

**验证**: 启动应用，检查WechatConfig bean加载成功

**回滚**: 删除wechat配置节，重启应用

### 步骤3: 代码部署

**操作**: 按tasks.md顺序部署代码

**验证**: 执行集成测试，确保微信登录功能正常

**回滚**: 回滚到上一版本，微信登录接口返回501 Not Implemented

### 数据兼容性

- 现有用户数据无需迁移
- 新增字段wechat_open_id已存在，仅添加索引
- 向后兼容: 未使用微信登录的用户wechat_open_id保持为NULL

## Open Questions

1. **是否需要支持微信用户信息同步(头像、昵称)?**
   - 建议: Phase 2实现，调用微信getUserInfo接口
   - 需要用户额外授权

2. **是否需要支持微信账号与邮箱账号绑定?**
   - 建议: Phase 2实现，在用户设置页面添加绑定功能
   - 需要设计绑定流程和解绑逻辑

3. **微信登录用户是否允许设置密码?**
   - 建议: 允许，用户可在设置页面设置密码，实现邮箱密码登录
   - 设置密码后支持两种登录方式

4. **是否需要支持微信UnionID?**
   - 建议: 暂不支持，小程序登录场景下OpenID已足够
   - 如需支持多个微信小程序，可在Phase 3添加UnionID支持
