CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `account_id` VARCHAR(64) UNIQUE NOT NULL COMMENT '账号ID',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    `email` VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态(1:正常,0:禁用)',
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色(USER,ADMIN)',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_account_id` (`account_id`),
    INDEX `idx_email` (`email`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


CREATE TABLE `user_session` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                `chat_id` VARCHAR(64) NOT NULL COMMENT '会话ID',
                                `session_type` VARCHAR(32) NOT NULL COMMENT '会话类型(chat,service,pdf,game)',
                                `title` VARCHAR(255) COMMENT '会话标题',
                                `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                INDEX `idx_user_id` (`user_id`),
                                INDEX `idx_chat_id` (`chat_id`),
                                INDEX `idx_session_type` (`session_type`),
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会话表';

/*CREATE TABLE `chat_message` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                `chat_id` VARCHAR(64) NOT NULL COMMENT '会话ID',
                                `message_type` VARCHAR(32) NOT NULL COMMENT '消息类型(user,assistant,system)',
                                `role` VARCHAR(32) NOT NULL COMMENT '角色(user,assistant)',
                                `content` TEXT NOT NULL COMMENT '消息内容',
                                `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                INDEX `idx_user_id` (`user_id`),
                                INDEX `idx_chat_id` (`chat_id`),
                                INDEX `idx_created_time` (`created_time`),
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';*/

