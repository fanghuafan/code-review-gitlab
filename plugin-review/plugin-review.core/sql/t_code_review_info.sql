--- 创建数据库
CREATE DATDBASE code_review; 
--- 创建数据库表
CREATE TABLE `t_code_review_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `reviewer` varchar(124) DEFAULT NULL COMMENT '代码审查人',
  `to_coder` varchar(124) DEFAULT NULL COMMENT '被审查人',
  `project` varchar(128) DEFAULT NULL COMMENT 'review的工程名',
  `class_path` varchar(2048) DEFAULT NULL COMMENT '评论类的路径',
  `start_line` int(11) DEFAULT NULL COMMENT '选中开始行',
  `end_line` int(11) DEFAULT NULL COMMENT '选中结束行',
  `comment_time` datetime DEFAULT NULL COMMENT '评论时间',
  `comment` text COMMENT '评论内容（包过选中的代码和reviewer comment）',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `status` int(11) DEFAULT NULL COMMENT 'review状态',
  `code` text COMMENT '代码行内容',
  `code_change` int(11) DEFAULT NULL COMMENT '代码行是否改变',
  `comment_type` varchar(45) DEFAULT NULL COMMENT '评论类型【comment or issue】',
  `commit_id` varchar(248) DEFAULT NULL COMMENT 'git提交log id',
  `coder_reply` text COMMENT '被审查人回复',
  `reviewer_id` int(11) DEFAULT NULL COMMENT '关联t_user',
  `to_coder_id` int(11) DEFAULT NULL COMMENT '关联t_user',
  `review_grade` varchar(45) DEFAULT NULL COMMENT 'review等级',
  `delete_status` int(11) DEFAULT NULL COMMENT '删除状态【0 正常 1 删除】',
  `gitlab_project_id` int(11) DEFAULT NULL COMMENT '关联gitlab project id',
  `gitlab_review_id` int(11) DEFAULT NULL COMMENT '关联gitlab上的主键id\ncomment对应code note id\nissue对应issue id\nsnippets对应snippets id',
  `repo_name` varchar(1024) DEFAULT NULL COMMENT '仓库名称',
  `gitlab_owner` varchar(256) DEFAULT NULL COMMENT 'gitlab上工程创建人',
  `project_class_path` varchar(2048) DEFAULT NULL COMMENT '类的相对路径',
  `title` varchar(4096) DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='code review相关相关信息记录数据库'