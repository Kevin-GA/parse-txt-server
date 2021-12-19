CREATE TABLE `project` (
  `project_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL COMMENT '项目名',
  `src_lang` varchar(5) NOT NULL COMMENT '源语言',
  `tgt_lang` varchar(5) NOT NULL COMMENT '目标语言',
  `field` varchar(32) DEFAULT NULL COMMENT '领域',
  `due_time` datetime DEFAULT NULL COMMENT '交付时间',
  `progress` decimal(3,2) NOT NULL COMMENT '进度',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `team_id` bigint(20) NOT NULL COMMENT '团队id',
  `create_by` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_del` tinyint(4) DEFAULT '0' COMMENT '删除标识 0：正常 1：已删除',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4

原稿/翻译稿/校对稿/编辑稿/译后终稿/双语终稿

CREATE TABLE `project_file` (
  `file_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `user_id` bigint(20) NOT NULL,
  `file_name` varchar(256) NOT NULL COMMENT '文件名',
  `file_path` varchar(100) NOT NULL COMMENT '文件路径',
  `file_ext` varchar(10) NOT NULL COMMENT '文件扩展名',
  `file_size` int(10) NOT NULL COMMENT '文件大小',
  `middle_file` varchar(100) DEFAULT NULL COMMENT '中间文件',
  `src_xlf_path` varchar(100) DEFAULT NULL COMMENT '原文xlf文件',
  `tgt_xlf_path` varchar(100) DEFAULT NULL COMMENT '译文xlf文件',
  `proofread_path` varchar(100) DEFAULT NULL COMMENT '校对稿',
  `edit_path` varchar(100) DEFAULT NULL COMMENT '编辑稿',
  `ft_path` varchar(100) DEFAULT NULL COMMENT '译后终稿',
  `bilingual_path` varchar(100) DEFAULT NULL COMMENT '双语终稿',
  `tgt_path` varchar(100) DEFAULT NULL COMMENT '目标文件',
  `total` int(10) DEFAULT NULL COMMENT '总数',
  `translated_count` int(10) DEFAULT NULL COMMENT '已翻译数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '删除标识 0：正常    1：已删除',
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

CREATE TABLE `project_process` (
  `process_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `type` int(4) NOT NULL COMMENT '类型    1：翻译  2：编辑  3：校验',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 0：未开始 1：进行中  2：已完成',
  `progress` decimal(3,2) NOT NULL DEFAULT '0.00' COMMENT '进度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '删除标识 0：正常   1：已删除',
  PRIMARY KEY (`process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

CREATE TABLE `task` (
  `task_id` bigint(20) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL COMMENT '项目id',
  `file_id` bigint(20) DEFAULT NULL COMMENT '项目文件id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `type` varchar(16) DEFAULT NULL COMMENT '工作类型',
  `due_time` datetime DEFAULT NULL COMMENT '交付时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 0：未开始  1：进行中  2：已完成',
  `progress` decimal(3,2) DEFAULT '0.00' COMMENT '进度',
  `handled_count` bigint(20) DEFAULT '0' COMMENT '已处理数',
  `tatal_count` bigint(20) DEFAULT '0' COMMENT '总数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '删除标识 0：正常  1：已删除',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4