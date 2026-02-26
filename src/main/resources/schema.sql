-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `nick_name` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
  `email` VARCHAR(100) DEFAULT NULL UNIQUE COMMENT '邮箱',
  `password` VARCHAR(255) DEFAULT NULL COMMENT '密码',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
  `level` INT DEFAULT 1 COMMENT '用户等级',
  `points` INT DEFAULT 0 COMMENT '用户积分',
  `openid` VARCHAR(100) DEFAULT NULL COMMENT '微信openid（可选）',
  `gender` TINYINT DEFAULT 0 COMMENT '性别（0:未知, 1:男, 2:女）',
  `country` VARCHAR(50) DEFAULT NULL COMMENT '国家',
  `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
  `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
  `language` VARCHAR(20) DEFAULT NULL COMMENT '语言',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1:正常, 0:禁用）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试用户数据
INSERT INTO `user` (`id`, `nick_name`, `email`, `password`, `avatar`, `level`, `points`) VALUES
(1, '测试用户', 'test@example.com', 'password123', 'https://example.com/avatar.jpg', 1, 0)
ON DUPLICATE KEY UPDATE id=id;

-- 创建分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '分类图标',
  `color` VARCHAR(20) DEFAULT NULL COMMENT '分类颜色',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1:正常, 0:禁用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 创建目标表
CREATE TABLE IF NOT EXISTS `goal` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(100) NOT NULL COMMENT '目标标题',
  `description` TEXT DEFAULT NULL COMMENT '目标描述',
  `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
  `priority` VARCHAR(20) DEFAULT 'medium' COMMENT '优先级（low, medium, high）',
  `deadline` DATE DEFAULT NULL COMMENT '截止日期',
  `progress` INT DEFAULT 0 COMMENT '进度（0-100）',
  `status` VARCHAR(20) DEFAULT 'in_progress' COMMENT '状态（in_progress, completed）',
  `image_url` VARCHAR(255) DEFAULT NULL COMMENT '图片URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目标表';

-- 创建提醒表
CREATE TABLE IF NOT EXISTS `reminder` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `goal_id` BIGINT DEFAULT NULL COMMENT '目标ID（可选）',
  `related_entity_type` VARCHAR(50) DEFAULT 'GOAL' COMMENT '关联实体类型(GOAL/FAMILY_MEMBER/HEALTH_CHECKUP)',
  `related_entity_id` BIGINT DEFAULT NULL COMMENT '关联实体ID',
  `title` VARCHAR(100) NOT NULL COMMENT '提醒标题',
  `content` VARCHAR(200) DEFAULT NULL COMMENT '提醒内容',
  `remind_time` DATETIME NOT NULL COMMENT '提醒时间',
  `reminder_type` VARCHAR(50) DEFAULT 'GOAL_DEADLINE' COMMENT '提醒类型(GOAL_DEADLINE/BIRTHDAY/HEALTH_CHECKUP)',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读（0:未读, 1:已读）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`goal_id`) REFERENCES `goal`(`id`) ON DELETE CASCADE,
  INDEX idx_user_remind_time (`user_id`, `remind_time`),
  INDEX idx_related_entity (`related_entity_type`, `related_entity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提醒表';

-- 创建子目标表
CREATE TABLE IF NOT EXISTS `sub_goal` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `goal_id` BIGINT NOT NULL COMMENT '所属目标ID',
  `title` VARCHAR(100) NOT NULL COMMENT '子目标标题',
  `description` TEXT DEFAULT NULL COMMENT '子目标描述',
  `progress` INT DEFAULT 0 COMMENT '进度（0-100）',
  `status` VARCHAR(20) DEFAULT 'not_started' COMMENT '状态（not_started, in_progress, completed）',
  `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成（0:未完成, 1:已完成）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`goal_id`) REFERENCES `goal`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子目标表';

-- 创建成就表
CREATE TABLE IF NOT EXISTS `achievement` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `type` VARCHAR(50) NOT NULL COMMENT '成就类型',
  `title` VARCHAR(100) NOT NULL COMMENT '成就标题',
  `description` TEXT DEFAULT NULL COMMENT '成就描述',
  `badge_url` VARCHAR(255) DEFAULT NULL COMMENT '徽章URL',
  `unlock_condition` VARCHAR(200) DEFAULT NULL COMMENT '解锁条件',
  `unlocked` TINYINT DEFAULT 0 COMMENT '是否已解锁（0:未解锁, 1:已解锁）',
  `is_unlocked` TINYINT DEFAULT 0 COMMENT '是否已解锁（兼容字段）',
  `unlocked_time` DATETIME DEFAULT NULL COMMENT '解锁时间',
  `unlocked_at` DATETIME DEFAULT NULL COMMENT '解锁时间（兼容字段）',
  `progress` INT DEFAULT 0 COMMENT '当前进度值',
  `target` INT DEFAULT 1 COMMENT '目标进度值',
  `category` VARCHAR(50) DEFAULT 'general' COMMENT '成就分类(goal_count/consecutive/category_focus/efficiency/milestone)',
  `tier` VARCHAR(20) DEFAULT 'bronze' COMMENT '成就等级(bronze/silver/gold/platinum/diamond)',
  `icon` VARCHAR(10) DEFAULT '🏆' COMMENT '成就图标emoji',
  `sort_order` INT DEFAULT 0 COMMENT '排序权重',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就表';

-- 创建日记表
CREATE TABLE IF NOT EXISTS `diary` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '日记标题',
  `content` TEXT NOT NULL COMMENT '日记内容(支持基础富文本)',
  `category` VARCHAR(50) DEFAULT 'general' COMMENT '分类(mood/work/study/general)',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON数组)',
  `mood` VARCHAR(20) DEFAULT NULL COMMENT '心情(happy/sad/calm/excited)',
  `weather` VARCHAR(20) DEFAULT NULL COMMENT '天气',
  `related_goal_id` BIGINT DEFAULT NULL COMMENT '关联的目标ID',
  `image_urls` TEXT DEFAULT NULL COMMENT '图片URL列表(JSON数组)',
  `is_public` TINYINT DEFAULT 0 COMMENT '是否公开(0:私密, 1:公开)',
  `view_count` INT DEFAULT 0 COMMENT '查看次数',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `diary_date` DATE NOT NULL COMMENT '日记日期',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`related_goal_id`) REFERENCES `goal`(`id`) ON DELETE SET NULL,
  INDEX idx_user_date (`user_id`, `diary_date`),
  INDEX idx_category (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记表';

-- 创建待办事项表
CREATE TABLE IF NOT EXISTS `todo` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '待办标题',
  `description` TEXT DEFAULT NULL COMMENT '待办描述',
  `priority` VARCHAR(20) DEFAULT 'medium' COMMENT '优先级(low/medium/high/urgent)',
  `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态(pending/in_progress/completed/cancelled)',
  `due_date` DATETIME DEFAULT NULL COMMENT '截止时间',
  `remind_time` DATETIME DEFAULT NULL COMMENT '提醒时间',
  `remind_sent` TINYINT DEFAULT 0 COMMENT '提醒是否已发送(0:未发送, 1:已发送)',
  `related_goal_id` BIGINT DEFAULT NULL COMMENT '关联的目标ID',
  `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON数组)',
  `estimated_minutes` INT DEFAULT NULL COMMENT '预计耗时(分钟)',
  `actual_minutes` INT DEFAULT NULL COMMENT '实际耗时(分钟)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`related_goal_id`) REFERENCES `goal`(`id`) ON DELETE SET NULL,
  INDEX idx_user_status (`user_id`, `status`),
  INDEX idx_due_date (`due_date`),
  INDEX idx_priority (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';

-- 创建家庭成员表
CREATE TABLE IF NOT EXISTS `family_member` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '成员姓名',
  `relation_type` VARCHAR(20) NOT NULL COMMENT '关系类型(FATHER/MOTHER/SPOUSE/SON/DAUGHTER/BROTHER/SISTER/GRANDFATHER/GRANDMOTHER/OTHER)',
  `birthday` DATE DEFAULT NULL COMMENT '生日',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `notes` TEXT DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  INDEX idx_user_id (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家庭成员表';

-- 创建健康体检表
CREATE TABLE IF NOT EXISTS `health_checkup` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `family_member_id` BIGINT NOT NULL COMMENT '家庭成员ID',
  `checkup_date` DATE NOT NULL COMMENT '体检日期',
  `next_checkup_date` DATE DEFAULT NULL COMMENT '下次体检日期',
  `hospital` VARCHAR(100) DEFAULT NULL COMMENT '体检医院',
  `notes` TEXT DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`family_member_id`) REFERENCES `family_member`(`id`) ON DELETE CASCADE,
  INDEX idx_member_id (`family_member_id`),
  INDEX idx_checkup_date (`checkup_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康体检表';

-- 插入默认分类数据
INSERT INTO `category` (`name`, `icon`, `color`, `sort`, `status`) VALUES
('职业发展', '💼', '#3498db', 1, 1),
('学习成长', '📚', '#2ecc71', 2, 1),
('健康生活', '🏃', '#e74c3c', 3, 1),
('兴趣爱好', '🎨', '#f39c12', 4, 1),
('家庭关系', '👨‍👩‍👧‍👦', '#9b59b6', 5, 1),
('财务管理', '💰', '#1abc9c', 6, 1);

-- 创建资料分类表
CREATE TABLE IF NOT EXISTS `document_category` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
  `icon` VARCHAR(20) DEFAULT NULL COMMENT '分类图标(emoji)',
  `color` VARCHAR(20) DEFAULT NULL COMMENT '分类颜色',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1:正常, 0:禁用）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料分类表';

-- 插入默认资料分类
INSERT INTO `document_category` (`name`, `description`, `icon`, `color`, `sort_order`) VALUES
('工作资料', '工作相关的文档和资料', '💼', '#667eea', 1),
('学习资料', '学习相关的文档和资料', '📚', '#10b981', 2),
('生活资料', '生活相关的文档和资料', '🏠', '#f59e0b', 3);

-- 创建资料文档表
CREATE TABLE IF NOT EXISTS `document` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型(PDF/DOC/DOCX/XLS/XLSX/PPT/PPTX/TXT/IMAGE)',
  `file_extension` VARCHAR(20) NOT NULL COMMENT '文件扩展名',
  `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
  `file_url` VARCHAR(500) NOT NULL COMMENT '文件访问URL',
  `mime_type` VARCHAR(100) DEFAULT NULL COMMENT 'MIME类型',
  `description` TEXT DEFAULT NULL COMMENT '文件描述',
  `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签(JSON数组)',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `view_count` INT DEFAULT 0 COMMENT '查看次数',
  `status` TINYINT DEFAULT 1 COMMENT '状态（1:正常, 0:已删除）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`category_id`) REFERENCES `document_category`(`id`) ON DELETE CASCADE,
  INDEX idx_user_category (`user_id`, `category_id`),
  INDEX idx_file_type (`file_type`),
  INDEX idx_create_time (`create_time`),
  FULLTEXT INDEX ft_file_name (`file_name`, `original_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料文档表';