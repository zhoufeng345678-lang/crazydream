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
  `goal_id` BIGINT NOT NULL COMMENT '目标ID',
  `title` VARCHAR(100) NOT NULL COMMENT '提醒标题',
  `content` VARCHAR(200) DEFAULT NULL COMMENT '提醒内容',
  `remind_time` DATETIME NOT NULL COMMENT '提醒时间',
  `is_read` TINYINT DEFAULT 0 COMMENT '是否已读（0:未读, 1:已读）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`goal_id`) REFERENCES `goal`(`id`) ON DELETE CASCADE
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
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成就表';

-- 插入默认分类数据
INSERT INTO `category` (`name`, `icon`, `color`, `sort`, `status`) VALUES
('职业发展', '💼', '#3498db', 1, 1),
('学习成长', '📚', '#2ecc71', 2, 1),
('健康生活', '🏃', '#e74c3c', 3, 1),
('兴趣爱好', '🎨', '#f39c12', 4, 1),
('家庭关系', '👨‍👩‍👧‍👦', '#9b59b6', 5, 1),
('财务管理', '💰', '#1abc9c', 6, 1);