# CrazyDream 部署指南

## 📋 目录

- [环境要求](#环境要求)
- [配置说明](#配置说明)
- [本地开发](#本地开发)
- [生产部署](#生产部署)
- [环境变量](#环境变量)
- [安全检查](#安全检查)

---

## 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **操作系统**: Linux / macOS / Windows

---

## 配置说明

### 配置文件结构

```
src/main/resources/
├── application.yml           # 通用配置
├── application-dev.yml       # 开发环境配置
├── application-test.yml      # 测试环境配置
└── application-prod.yml      # 生产环境配置
```

### 环境切换

通过 `spring.profiles.active` 参数切换环境：

```bash
# 开发环境（默认）
mvn spring-boot:run

# 测试环境
mvn spring-boot:run -Dspring-boot.run.profiles=test

# 生产环境
java -jar crazydream.jar --spring.profiles.active=prod
```

---

## 本地开发

### 1. 克隆项目

```bash
git clone <repository-url>
cd crazydream
```

### 2. 配置数据库

确保 MySQL 服务运行，并创建数据库：

```sql
CREATE DATABASE crazydream CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 启动应用

```bash
# 使用默认配置（开发环境）
mvn clean spring-boot:run
```

### 4. 测试接口

```bash
# 健康检查
curl http://localhost:8080/health

# 获取目标列表（开发环境会使用默认测试用户）
curl http://localhost:8080/api/goals
```

---

## 生产部署

### 方式 1: 使用环境变量（推荐）

#### 1. 准备环境变量配置

```bash
# 复制示例文件
cp .env.example .env

# 编辑 .env 文件，填入真实配置
vim .env
```

必须配置的环境变量：

```bash
# 环境
SPRING_PROFILES_ACTIVE=prod

# 数据库（必须）
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/crazydream?useSSL=true
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# JWT 密钥（必须，生产环境必须使用强随机密钥）
JWT_SECRET=$(openssl rand -hex 32)

# 阿里云 OSS（如果使用文件上传功能）
ALIYUN_OSS_ACCESS_KEY_ID=your_key_id
ALIYUN_OSS_ACCESS_KEY_SECRET=your_key_secret
ALIYUN_OSS_BUCKET_NAME=your_bucket
```

#### 2. 使用部署脚本

```bash
# 一键部署（包含安全检查、构建、启动）
./scripts/deploy-prod.sh
```

#### 3. 手动部署

```bash
# 加载环境变量
export $(cat .env | grep -v '^#' | xargs)

# 构建
mvn clean package -DskipTests

# 启动
nohup java -jar target/crazydream-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### 方式 2: 使用 JVM 参数

```bash
java -jar crazydream.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:mysql://... \
  --spring.datasource.username=user \
  --spring.datasource.password=pass \
  --jwt.secret=your_secret
```

### 方式 3: 使用 Docker（待实现）

```bash
docker build -t crazydream:latest .
docker run -p 8080:8080 --env-file .env crazydream:latest
```

---

## 环境变量

### 完整的环境变量列表

| 变量名 | 说明 | 默认值 | 必须 |
|--------|------|--------|------|
| `SPRING_PROFILES_ACTIVE` | 激活的环境 | dev | ❌ |
| `SPRING_DATASOURCE_URL` | 数据库连接URL | localhost:3306 | ✅ |
| `SPRING_DATASOURCE_USERNAME` | 数据库用户名 | root | ✅ |
| `SPRING_DATASOURCE_PASSWORD` | 数据库密码 | - | ✅ |
| `JWT_SECRET` | JWT 签名密钥 | 默认密钥 | ✅ |
| `JWT_EXPIRATION` | Token 过期时间（秒） | 86400 | ❌ |
| `ALIYUN_OSS_ENDPOINT` | OSS 端点 | - | ❌ |
| `ALIYUN_OSS_ACCESS_KEY_ID` | OSS AccessKey | - | ❌ |
| `ALIYUN_OSS_ACCESS_KEY_SECRET` | OSS Secret | - | ❌ |
| `ALIYUN_OSS_BUCKET_NAME` | OSS Bucket | - | ❌ |
| `ALIYUN_OSS_DOMAIN` | OSS 域名 | - | ❌ |

### 环境变量优先级

Spring Boot 配置优先级（从高到低）：

1. 命令行参数 `--spring.datasource.password=xxx`
2. 环境变量 `SPRING_DATASOURCE_PASSWORD=xxx`
3. application-{profile}.yml
4. application.yml

---

## 安全检查

### CI/CD 管道中的安全检查

项目包含安全检查脚本，用于验证生产环境配置：

```bash
# 检查生产环境配置
./scripts/check-security-config.sh prod

# 检查开发环境配置
./scripts/check-security-config.sh dev
```

### GitHub Actions 集成

`.github/workflows/security-check.yml` 已配置自动安全检查，在以下情况触发：

- 推送到 main/master 分支
- 向 main/master 分支发起 Pull Request

### 安全检查项

- ✅ 生产环境必须启用安全认证（`security.auth.disabled=false`）
- ✅ 开发/测试环境可以禁用认证
- ✅ JWT 密钥不能使用默认值
- ✅ 数据库密码不能明文提交

---

## 常见问题

### 1. 启动失败：端口被占用

```bash
# 查看占用 8080 端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>
```

### 2. 数据库连接失败

检查配置：
- 数据库服务是否启动
- 连接URL、用户名、密码是否正确
- 防火墙是否允许连接

### 3. JWT Token 无效

- 确保生产环境使用了自定义的 JWT_SECRET
- 检查 token 是否过期
- 确认请求头格式：`Authorization: Bearer <token>`

### 4. 文件上传失败

- 确认 OSS 配置是否正确
- 检查 AccessKey 权限
- 验证 Bucket 是否存在

---

## 监控和日志

### 查看应用日志

```bash
# 实时查看日志
tail -f app.log

# 搜索错误日志
grep ERROR app.log

# 查看最近的日志
tail -n 100 app.log
```

### 健康检查

```bash
# 应用健康检查
curl http://localhost:8080/health

# 期望响应
{
  "code": 200,
  "message": "成功",
  "data": "API服务正常运行"
}
```

---

## 停止应用

```bash
# 查找进程
ps aux | grep crazydream

# 优雅停止
kill <PID>

# 强制停止
kill -9 <PID>

# 或使用 pkill
pkill -f crazydream
```

---

## 联系方式

如有问题，请联系：
- 项目负责人：[Your Name]
- 邮箱：[your-email@example.com]
