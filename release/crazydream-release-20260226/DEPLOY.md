# CrazyDream 后端应用部署指南

## 📦 发布包内容

```
crazydream-release-20260226/
├── crazydream-0.0.1-SNAPSHOT.jar   # 应用可执行JAR包
├── .env.example                     # 环境变量配置示例
├── schema.sql                       # 数据库建表脚本
├── start.sh                         # 启动脚本
├── stop.sh                          # 停止脚本
└── DEPLOY.md                        # 本部署文档
```

## 🚀 快速部署

### 1. 环境要求

- **Java**: JDK 17 或更高版本
- **MySQL**: 8.0 或更高版本
- **内存**: 建议至少 1GB 可用内存
- **操作系统**: Linux/macOS/Windows

### 2. 数据库初始化

```bash
# 连接到MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE crazydream CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入表结构
mysql -u root -p crazydream < schema.sql
```

### 3. 配置环境变量

```bash
# 复制环境变量示例文件
cp .env.example .env

# 编辑配置文件，填入真实值
vim .env
```

**必须配置的环境变量：**

```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/crazydream?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=你的数据库密码

# JWT密钥（生产环境必须修改）
JWT_SECRET=使用openssl_rand_-hex_32生成的随机密钥

# 阿里云OSS配置
ALIYUN_OSS_ENDPOINT=oss-cn-beijing.aliyuncs.com
ALIYUN_OSS_ACCESS_KEY_ID=你的AccessKeyId
ALIYUN_OSS_ACCESS_KEY_SECRET=你的AccessKeySecret
ALIYUN_OSS_BUCKET_NAME=你的Bucket名称
ALIYUN_OSS_DOMAIN=你的Bucket域名

# 微信小程序配置
WECHAT_APP_ID=你的小程序AppID
WECHAT_APP_SECRET=你的小程序AppSecret
```

### 4. 启动应用

```bash
# 添加执行权限
chmod +x start.sh stop.sh

# 启动应用
./start.sh
```

### 5. 验证运行状态

```bash
# 查看日志
tail -f logs/crazydream.log

# 检查进程
cat crazydream.pid
ps -p $(cat crazydream.pid)

# 测试API
curl http://localhost:8080/actuator/health
```

## 🛠️ 运维操作

### 停止应用

```bash
./stop.sh
```

### 重启应用

```bash
./stop.sh && ./start.sh
```

### 查看日志

```bash
# 实时查看日志
tail -f logs/crazydream.log

# 查看最近100行
tail -n 100 logs/crazydream.log

# 搜索错误日志
grep ERROR logs/crazydream.log
```

## 📋 配置说明

### JVM参数调优

编辑 `start.sh`，根据服务器配置调整内存参数：

```bash
# 最小堆内存512MB，最大堆内存1GB
-Xms512m -Xmx1024m

# 生产环境建议配置：
-Xms1024m -Xmx2048m
```

### 端口配置

默认端口为 8080，如需修改：

1. 在 `.env` 文件中添加：
```bash
SERVER_PORT=8081
```

2. 或在启动命令中添加参数：
```bash
java -jar -Dserver.port=8081 crazydream-0.0.1-SNAPSHOT.jar
```

## 🔒 安全建议

1. **修改默认JWT密钥**
   ```bash
   # 生成随机密钥
   openssl rand -hex 32
   ```

2. **配置数据库访问控制**
   - 使用专用数据库用户，而非root
   - 限制数据库访问IP

3. **配置防火墙**
   ```bash
   # 仅开放必要端口
   sudo ufw allow 8080/tcp
   ```

4. **使用HTTPS**
   - 配置Nginx反向代理
   - 申请SSL证书

## 📊 监控建议

### 1. 健康检查

应用提供了Spring Boot Actuator端点：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 应用信息
curl http://localhost:8080/actuator/info
```

### 2. 日志监控

建议使用日志收集工具（如ELK Stack）进行集中式日志管理。

### 3. 性能监控

可以接入监控系统（如Prometheus + Grafana）。

## 🐛 故障排查

### 应用无法启动

1. 检查Java版本
```bash
java -version
# 应该显示 Java 17 或更高版本
```

2. 检查端口占用
```bash
lsof -i :8080
# 如果端口被占用，停止占用进程或修改应用端口
```

3. 检查数据库连接
```bash
# 查看日志中的数据库连接错误
grep "database" logs/crazydream.log
```

### 内存溢出

如果出现 OutOfMemoryError：

1. 增加堆内存：修改 `start.sh` 中的 `-Xmx` 参数
2. 分析堆转储：`java -XX:+HeapDumpOnOutOfMemoryError`

### 数据库连接池耗尽

检查数据库连接数配置，在 `.env` 中添加：

```bash
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=20
```

## 📞 技术支持

如遇到问题，请检查：
1. 应用日志：`logs/crazydream.log`
2. 环境变量配置：`.env`
3. 数据库连接状态

---

**版本信息：**
- 应用版本：0.0.1-SNAPSHOT
- 发布日期：2026-02-26
- Spring Boot版本：3.2.0
