# CrazyDream 人生计划清单后端服务

## 项目简介

CrazyDream 是一个专为个人人生规划设计的后端服务系统，帮助用户有效地管理和追踪人生目标、学习计划和个人成就。系统提供了完整的用户认证、目标管理、分类管理、成就系统和数据统计功能，支持用户将宏大的人生目标分解为可执行的小目标，并通过持续的追踪和激励机制帮助用户实现梦想。

## 核心功能

### 用户管理
- 用户注册与登录
- 个人信息管理
- JWT身份认证与授权

### 目标管理
- 创建、查询、更新和删除目标
- 目标优先级和状态管理
- 目标起止日期设置
- 目标图标自定义
- 批量操作支持

### 子目标管理
- 为目标创建子任务
- 子目标进度追踪
- 子目标优先级和状态管理
- 截止日期提醒

### 分类管理
- 目标分类创建与管理
- 分类图标和名称自定义
- 分类权限控制

### 成就系统
- 成就自动解锁机制
- 成就列表查询
- 成就条件检查

### 数据统计
- 目标完成情况统计
- 仪表盘数据展示
- 多维度数据分析

### 文件管理
- 头像上传
- 阿里云OSS集成

## 技术架构

### 技术栈

| 技术/框架 | 版本 | 用途 |
|-----------|------|------|
| Java | 17 | 开发语言 |
| Spring Boot | 3.2.0 | 应用框架 |
| MyBatis | 3.0.3 | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| Spring Security | 6.0+ | 安全框架 |
| JWT | 0.12.3 | 认证令牌 |
| Lombok | 1.18.30 | 代码简化 |
| Alibaba Cloud OSS | 3.17.1 | 对象存储 |
| Maven | 3.8+ | 项目构建 |

### 架构设计

系统采用 **DDD + COLA 四层架构**，以领域模型为核心组织代码，各层职责明确，便于维护和扩展：

1. **Interface 层（接口层）**：位于 `com.crazydream.interfaces`，对外暴露 REST API（v2：`/api/v2/*`），负责请求适配与 DTO 组装
2. **Application 层（应用层）**：位于 `com.crazydream.application`，负责编排用例流程、事务控制，不直接依赖基础设施
3. **Domain 层（领域层）**：位于 `com.crazydream.domain`，包含聚合根、实体、值对象、领域服务等，承载核心业务规则
4. **Infrastructure 层（基础设施层）**：位于 `com.crazydream.infrastructure`，实现 Repository、数据持久化与外部系统（如 OSS）的适配
5. **支撑模块**：`config`、`security`、`common`、`utils` 提供配置、安全、公共模型和通用工具支持

### 项目结构

```
crazydream/
├── src/main/java/com/crazydream/    # Java源代码
│   ├── CrazydreamApplication.java   # 应用启动类
│   ├── interfaces/                  # Interface层：REST Controller（/api/v2/*）
│   ├── application/                 # Application层：用例编排、DTO
│   ├── domain/                      # Domain层：领域模型与领域服务
│   ├── infrastructure/              # Infrastructure层：持久化与外部系统适配
│   ├── config/                      # Spring & Web配置
│   ├── security/                    # 安全与认证
│   ├── common/                      # 公共返回体等通用类
│   └── utils/                       # 工具模块
├── tests/                           # 测试脚本目录
│   ├── api_tests/                   # API集成测试
│   └── performance_tests/           # 性能和负载测试
├── docs/                            # 文档目录
│   ├── api_documentation/           # API文档
│   ├── technical_reports/           # 技术报告
│   └── archived_reports/            # 历史归档报告
├── openspec/                        # OpenSpec提案管理
├── README.md                        # 项目说明
├── DEPLOYMENT.md                    # 部署文档
└── pom.xml                          # Maven配置
```

## 安装步骤

### 前提条件

- JDK 17+ 已安装
- MySQL 8.0+ 已安装并运行
- Maven 3.8+ 已安装
- Git 已安装（可选）

### 安装过程

1. **克隆项目**

   ```bash
   git clone https://github.com/your-username/crazydream.git
   cd crazydream
   ```

2. **配置数据库**

   - 创建MySQL数据库：
     ```sql
     CREATE DATABASE crazydream CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     ```

   - 修改数据库配置文件 `src/main/resources/application.yml`：
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/crazydream?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
         username: your-username
         password: your-password
     ```

3. **配置阿里云OSS（可选）**

   - 如需使用文件上传功能，修改阿里云OSS配置（位于 `application.yml` 中的 `aliyun.oss` 节点）：
     ```yaml
     aliyun:
       oss:
         endpoint: your-endpoint
         access-key-id: your-access-key-id
         access-key-secret: your-access-key-secret
         bucket-name: your-bucket-name
         domain: your-bucket-domain
     ```

4. **构建项目**

   ```bash
   mvn clean install
   ```

5. **运行项目**

   ```bash
   mvn spring-boot:run
   ```

   或使用jar包运行：
   ```bash
   java -jar target/crazydream-0.0.1-SNAPSHOT.jar
   ```

6. **验证服务**

   服务启动后，访问以下URL验证服务是否正常运行：
   ```
   http://localhost:8080/api
   ```

## 使用指南

### API文档

系统提供了完整的API文档，包含所有接口的详细信息：

- 查看 `api-documentation.md` 文件获取完整API文档
- API根路径：业务接口 `http://localhost:8080/api/v2`，认证接口 `http://localhost:8080/api/auth/*`
- 认证方式：JWT令牌，请求头中添加 `Authorization: Bearer {token}`

### 主要API使用示例

1. **用户注册**

   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
        -H "Content-Type: application/json" \
        -d '{"email":"user@example.com","password":"password123","nickName":"用户昵称"}'
   ```

2. **用户登录**

   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
        -H "Content-Type: application/json" \
        -d '{"email":"user@example.com","password":"password123"}'
   ```

3. **创建目标**

   ```bash
   curl -X POST http://localhost:8080/api/v2/goals \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer {token}" \
        -d '{"title":"学习Spring Boot","description":"掌握Spring Boot框架","categoryId":1,"priority":"high","status":"in_progress","startDate":"2025-12-01","endDate":"2025-12-31","icon":"📚"}'
   ```

4. **获取所有目标**

   ```bash
   curl -X GET http://localhost:8080/api/v2/goals \
        -H "Authorization: Bearer {token}"
   ```

### 测试

系统提供了API测试脚本，可用于验证API功能：

```bash
python3 api_test.py
```

测试结果将输出到控制台，并生成JSON格式的测试报告 `api_test_report.json`。

## 配置说明

### 应用配置

主要配置文件：`src/main/resources/application.yml`（以及 `application-dev.yml`、`application-test.yml`、`application-prod.yml` 等环境配置）

| 配置项 | 描述 | 默认值 |
|-------|------|-------|
| server.port | 服务端口 | 8080 |
| server.servlet.context-path | 上下文路径 | / （未显式配置时为根路径） |
| spring.datasource.* | 数据库配置 | - |
| mybatis.mapper-locations | Mapper文件位置 | classpath:mapper/*.xml |
| jwt.secret | JWT密钥 | 自动生成 |
| jwt.expiration | JWT过期时间 | 86400000 (1天) |
| aliyun.oss.* | 阿里云OSS配置 | - |
| spring.servlet.multipart.max-file-size | 最大文件大小 | 10MB |

### 环境变量

支持通过环境变量覆盖配置文件中的值，例如：

```bash
SERVER_PORT=9090 DB_USERNAME=admin mvn spring-boot:run
```

## 开发与贡献

### 开发流程

1. Fork项目仓库
2. 创建特性分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送到分支：`git push origin feature/your-feature`
5. 提交Pull Request

### 代码规范

- 遵循Java编码规范
- 使用Lombok简化代码
- 添加适当的注释
- 编写单元测试

### 构建与测试

```bash
# 构建项目
mvn clean install

# 运行单元测试
mvn test

# 运行API测试
python3 api_test.py
```

## 安全考虑

- 使用JWT进行身份认证
- 密码加密存储
- 权限控制和访问限制
- 输入参数验证
- 防止SQL注入
- 防止跨站脚本攻击

## 文档与测试

### 测试脚本
项目包含完整的测试套件，位于 `tests/` 目录：

- **API测试**: `tests/api_tests/` - 包含API集成测试和微信登录测试
- **性能测试**: `tests/performance_tests/` - 包含性能和负载测试脚本

详见：[tests/README.md](tests/README.md)

### 技术文档
完整的技术文档和API文档位于 `docs/` 目录：

- **API文档**: `docs/api_documentation/` - 完整的API接口说明
  - [API文档](docs/api_documentation/api-documentation.md) - 42KB完整API文档
  - [微信登录API](docs/api_documentation/WECHAT_LOGIN_API.md) - 微信一键登录接口说明
- **技术报告**: `docs/technical_reports/` - DDD架构演进报告
- **历史报告**: `docs/archived_reports/` - 问题修复和历史记录

详见：[docs/README.md](docs/README.md)

## 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目地址：https://github.com/your-username/crazydream
- 问题反馈：https://github.com/your-username/crazydream/issues

## 更新日志

### v0.0.1-SNAPSHOT (2025-12-10)

- 初始版本发布
- 实现用户管理功能
- 实现目标和子目标管理
- 实现分类管理
- 实现成就系统
- 实现数据统计功能

---

**感谢使用 CrazyDream 人生计划清单后端服务！** 🚀