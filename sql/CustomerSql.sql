CREATE TABLE `consultation_record` (
                                       `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                       `consultation_no` VARCHAR(64) UNIQUE NOT NULL COMMENT '咨询编号',
                                       `customer_name` VARCHAR(50) NOT NULL COMMENT '客户姓名',
                                       `contact_info` VARCHAR(100) NOT NULL COMMENT '联系方式',
                                       `use_case` VARCHAR(200) COMMENT '使用场景',
                                       `budget_range` VARCHAR(50) COMMENT '预算范围',
                                       `requirements_json` JSON COMMENT '用户需求JSON',
                                       `recommended_config_json` JSON COMMENT '推荐配置JSON',
                                       `status` VARCHAR(20) DEFAULT '待跟进' COMMENT '状态：待跟进、已联系、已成交、已关闭',
                                       `priority` TINYINT DEFAULT 1 COMMENT '优先级：1-低，2-中，3-高',
                                       `assigned_to` VARCHAR(50) COMMENT '分配给谁',
                                       `remark` TEXT COMMENT '备注',
                                       `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       INDEX `idx_consultation_no` (`consultation_no`),
                                       INDEX `idx_status` (`status`),
                                       INDEX `idx_priority` (`priority`),
                                       INDEX `idx_created_time` (`created_time`),
                                       INDEX `idx_assigned_to` (`assigned_to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询记录表';

CREATE TABLE `cloud_desktop` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                 `name` VARCHAR(100) NOT NULL COMMENT '配置名称',
                                 `type` VARCHAR(20) NOT NULL COMMENT '配置类型：经济型、标准型、专业型、高端型',
                                 `config_json` JSON COMMENT '配置详情JSON',
                                 `price_per_month` DECIMAL(10,2) COMMENT '月租价格',
                                 `price_per_year` DECIMAL(10,2) COMMENT '年租价格',
                                 `price_per_hour` DECIMAL(10,2) COMMENT '小时价格',
                                 `status` TINYINT DEFAULT 1 COMMENT '状态(1:可用,0:不可用)',
                                 `sort_order` INT DEFAULT 0 COMMENT '排序',
                                 `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 INDEX `idx_type` (`type`),
                                 INDEX `idx_status` (`status`),
                                 INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='云桌面配置表';

CREATE TABLE `preload_software` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                    `name` VARCHAR(100) NOT NULL COMMENT '软件名称',
                                    `category` VARCHAR(50) NOT NULL COMMENT '软件分类：设计软件、办公软件、开发工具等',
                                    `version` VARCHAR(20) COMMENT '版本号',
                                    `description` TEXT COMMENT '软件描述',
                                    `icon_url` VARCHAR(255) COMMENT '图标URL',
                                    `is_free` TINYINT DEFAULT 0 COMMENT '是否免费：0-收费，1-免费',
                                    `status` TINYINT DEFAULT 1 COMMENT '状态(1:可用,0:不可用)',
                                    `sort_order` INT DEFAULT 0 COMMENT '排序',
                                    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    INDEX `idx_category` (`category`),
                                    INDEX `idx_status` (`status`),
                                    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预载软件表';

INSERT INTO `cloud_desktop` (`name`, `type`, `config_json`, `price_per_month`, `price_per_year`, `price_per_hour`, `sort_order`) VALUES
                                                                                                                                     ('经济型-4核', '经济型', '{"cpu":{"model":"Intel i5-9400","cores":4,"threads":6},"memory":{"size":8,"type":"DDR4"},"gpu":{"model":"GTX 1650","memory":4},"storage":{"size":100,"type":"SSD"},"network":{"bandwidth":100}}', 299.00, 2999.00, 0.50, 1),
                                                                                                                                     ('标准型-8核', '标准型', '{"cpu":{"model":"Intel i7-10700","cores":8,"threads":16},"memory":{"size":16,"type":"DDR4"},"gpu":{"model":"RTX 3060","memory":6},"storage":{"size":200,"type":"SSD"},"network":{"bandwidth":200}}', 599.00, 5999.00, 1.00, 2),
                                                                                                                                     ('专业型-12核', '专业型', '{"cpu":{"model":"Intel i9-10900","cores":12,"threads":20},"memory":{"size":32,"type":"DDR4"},"gpu":{"model":"RTX 3070","memory":8},"storage":{"size":500,"type":"SSD"},"network":{"bandwidth":500}}', 999.00, 9999.00, 1.50, 3),
                                                                                                                                     ('高端型-16核', '高端型', '{"cpu":{"model":"Intel i9-10900K","cores":16,"threads":32},"memory":{"size":64,"type":"DDR4"},"gpu":{"model":"RTX 3080","memory":10},"storage":{"size":1000,"type":"SSD"},"network":{"bandwidth":1000}}', 1599.00, 15999.00, 2.50, 4);


INSERT INTO `preload_software` (`name`, `category`, `version`, `description`, `is_free`, `sort_order`) VALUES
                                                                                                           ('Adobe After Effects', '设计软件', '2023', '专业视频特效制作软件', 0, 1),
                                                                                                           ('Adobe Premiere Pro', '设计软件', '2023', '专业视频剪辑软件', 0, 2),
                                                                                                           ('Adobe Photoshop', '设计软件', '2023', '专业图像处理软件', 0, 3),
                                                                                                           ('Adobe Illustrator', '设计软件', '2023', '专业矢量图形设计软件', 0, 4),
                                                                                                           ('Autodesk Maya', '设计软件', '2023', '3D动画制作软件', 0, 5),
                                                                                                           ('Cinema 4D', '设计软件', '2023', '3D建模和动画软件', 0, 6),
                                                                                                           ('Microsoft Office', '办公软件', '2021', '办公套件', 0, 7),
                                                                                                           ('WPS Office', '办公软件', '2023', '国产办公软件', 1, 8),
                                                                                                           ('Visual Studio Code', '开发工具', '2023', '代码编辑器', 1, 9),
                                                                                                           ('IntelliJ IDEA', '开发工具', '2023', 'Java开发工具', 0, 10),
                                                                                                           ('Unity', '开发工具', '2023', '游戏开发引擎', 0, 11),
                                                                                                           ('Unreal Engine', '开发工具', '5.0', '游戏开发引擎', 0, 12);


