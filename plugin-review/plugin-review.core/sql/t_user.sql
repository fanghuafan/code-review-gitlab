CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(45) DEFAULT NULL COMMENT '姓名',
  `english_name` varchar(45) DEFAULT NULL COMMENT '英文名',
  `position` varchar(45) DEFAULT NULL COMMENT '用户职位',
  `gitlab_relative_id` int(11) DEFAULT NULL COMMENT '关联gitlab用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息'