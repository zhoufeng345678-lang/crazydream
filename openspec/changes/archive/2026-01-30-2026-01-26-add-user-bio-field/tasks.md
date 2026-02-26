# Tasks: 添加用户个人简介功能

## 实现任务

### 1. 创建Bio值对象
**优先级**: P0  
**预计时间**: 15分钟

在 `domain/user/model/valueobject/` 目录下创建Bio.java：
- 实现bio字段的封装和验证逻辑
- 限制最大长度为500字符
- 允许null值（可选字段）
- 提供构造方法和getter方法
- 实现equals和hashCode方法

**依赖**: 无  
**可并行**: 是

**验证标准**:
- Bio对象可以接受null值
- Bio对象可以接受0-500字符的字符串
- 超过500字符抛出IllegalArgumentException
- 单元测试通过

---

### 2. 更新User聚合根
**优先级**: P0  
**预计时间**: 15分钟

修改 `domain/user/model/aggregate/User.java`：
- 添加 `private Bio bio;` 字段
- 在rebuild方法中增加bio参数
- 在updateProfile方法中增加bio参数
- 添加getBio()方法

**依赖**: 任务1  
**可并行**: 否

**验证标准**:
- User对象包含bio字段
- updateProfile可以更新bio
- rebuild可以重建包含bio的User对象
- 编译无错误

---

### 3. 更新DTO和Command
**优先级**: P0  
**预计时间**: 10分钟

修改应用层数据传输对象：
- `application/user/dto/UserDTO.java` - 添加 `private String bio;`
- `application/user/dto/UpdateProfileCommand.java` - 添加 `private String bio;`

**依赖**: 无  
**可并行**: 与任务1并行

**验证标准**:
- UserDTO包含bio字段
- UpdateProfileCommand包含bio字段
- Lombok注解正确生成getter/setter
- 编译无错误

---

### 4. 更新UserAssembler
**优先级**: P0  
**预计时间**: 15分钟

修改 `application/user/assembler/UserAssembler.java`：
- 在toDTO方法中映射bio字段
- 在fromCommand方法中处理bio参数

**依赖**: 任务2、任务3  
**可并行**: 否

**验证标准**:
- User到UserDTO的转换包含bio字段
- UpdateProfileCommand到User的映射正确处理bio
- null值正确处理
- 编译无错误

---

### 5. 更新UserApplicationService
**优先级**: P0  
**预计时间**: 10分钟

修改 `application/user/service/UserApplicationService.java`：
- 在updateProfile方法中传递bio参数到User.updateProfile()

**依赖**: 任务4  
**可并行**: 否

**验证标准**:
- updateProfile方法正确处理bio字段
- bio可以被更新或保持不变
- 编译无错误

---

### 6. 更新数据库Schema
**优先级**: P0  
**预计时间**: 10分钟

修改 `src/main/resources/schema.sql`：
- 在user表定义中添加bio列：`bio VARCHAR(500) DEFAULT NULL COMMENT '个人简介'`
- 执行ALTER TABLE语句在现有数据库中添加列（如需要）

**依赖**: 无  
**可并行**: 与任务1-5并行

**验证标准**:
- schema.sql包含bio列定义
- 数据库成功添加bio列
- 现有数据bio值为NULL

---

### 7. 更新UserPO持久化对象
**优先级**: P0  
**预计时间**: 10分钟

修改 `infrastructure/persistence/po/UserPO.java`：
- 添加 `private String bio;` 字段
- 确保getter/setter正确

**依赖**: 任务6  
**可并行**: 否

**验证标准**:
- UserPO包含bio字段
- 字段映射正确
- 编译无错误

---

### 8. 更新MyBatis映射文件
**优先级**: P0  
**预计时间**: 15分钟

修改 `infrastructure/persistence/mapper/UserPersistenceMapper.xml`：
- 在resultMap中添加bio字段映射
- 在INSERT语句中添加bio列
- 在UPDATE语句中添加bio列
- 在SELECT语句中包含bio列

**依赖**: 任务7  
**可并行**: 否

**验证标准**:
- 所有SQL语句包含bio字段
- 查询结果正确映射bio
- 更新操作正确保存bio
- MyBatis配置验证通过

---

### 9. 更新UserRepository
**优先级**: P0  
**预计时间**: 10分钟

修改 `domain/user/repository/UserRepository.java` 实现类：
- 在POToAggregate方法中映射bio字段
- 在aggregateToPO方法中映射bio字段

**依赖**: 任务2、任务7  
**可并行**: 否

**验证标准**:
- User到UserPO转换包含bio
- UserPO到User转换包含bio
- null值正确处理
- 编译无错误

---

### 10. 集成测试
**优先级**: P0  
**预计时间**: 30分钟

验证完整功能：
- 调用 `PUT /api/v2/users/profile` 更新bio
- 调用 `GET /api/v2/users/profile` 查看bio
- 测试bio为null的情况
- 测试bio超长的情况
- 验证数据库bio字段正确保存

**依赖**: 任务1-9  
**可并行**: 否

**验证标准**:
- API可以成功更新bio
- API可以正确返回bio
- bio字段长度验证生效
- 数据库正确存储bio
- null值处理正确

---

### 11. 更新API文档
**优先级**: P1  
**预计时间**: 20分钟

修改 `/Users/zhoufeng/Documents/trae_projects/crazydream/api-documentation.md`：
- 在用户资料更新接口请求参数中添加bio字段说明
- 在用户信息查询接口响应字段中添加bio字段说明
- 添加bio字段的验证规则说明

**依赖**: 任务10  
**可并行**: 否

**验证标准**:
- API文档包含bio字段完整说明
- 包含字段类型、长度限制、是否必填等信息
- 提供请求示例和响应示例

---

## 任务执行顺序

**阶段1 - 领域层实现（可部分并行）**:
1. 创建Bio值对象（P0）
2. 更新User聚合根（P0）

**阶段2 - 应用层实现（可部分并行）**:
3. 更新DTO和Command（P0，可与阶段1并行）
4. 更新UserAssembler（P0）
5. 更新UserApplicationService（P0）

**阶段3 - 基础设施层实现（可部分并行）**:
6. 更新数据库Schema（P0，可与阶段1-2并行）
7. 更新UserPO持久化对象（P0）
8. 更新MyBatis映射文件（P0）
9. 更新UserRepository（P0）

**阶段4 - 测试与文档**:
10. 集成测试（P0）
11. 更新API文档（P1）

## 风险评估

**低风险**:
- 修改范围明确，仅涉及用户资料相关代码
- 新增字段为可选，不影响现有功能
- DDD架构清晰，修改点明确
- 有明确的值对象封装验证逻辑

**需要注意**:
- 数据库迁移需要谨慎，确保不影响现有数据
- 长度限制需要在多个层次验证（值对象、数据库）
- null值处理需要在各层正确实现
