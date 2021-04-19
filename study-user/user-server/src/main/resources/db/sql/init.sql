Create Database If Not Exists `evo_interface` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `evo_interface`.`config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '配置名',
  `describe` varchar(255) DEFAULT NULL COMMENT '描叙',
  `value` varchar(255) DEFAULT NULL COMMENT '值',
  `create_time` datetime DEFAULT NULL,
  `is_valid` int(1) NOT NULL COMMENT '是否有效 1 是 0 否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;


INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-job-complete-url', '5.2 任务完成回报接口url', 'http://localhost:10080/agv/job/done', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-confirm-bin-url', '5.8.4任务料箱信息确认接口url', 'http://localhost:10080/api/quicktron/rcs/robot.job.bin.confirm', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-rotate-url', '5.8.1请求上游辊筒转动url', 'http://localhost:10080/api/quicktron/rcs/standardized.roller.rotate.request', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('single-job-interval-time', '单条任务间隔时间，数字类型，单位秒；默认30秒', '15', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-agv-rotate-complete-url', '5.8.3小车辊筒转动完成', 'http://localhost:10080/api/quicktron/rcs/standardized.roller.rotate.finished', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-all-zone-code', '所有库区，多个逗号隔开', 'JITL2,CAL1,JITL1', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-query-bucket-zone-code', '查询bucket库区，多个逗号隔开', 'JITL2,CAL1,JITL1', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_remote_call_fail_retry_count', '调用上游接口重试次数，-1 无限，默认-1', '-1', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_remote_call_fail_retry_count_time', '调用上游接口重试时间间隔，单位秒，默认 10', '10', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_delete_records_from_current_time', '删除距离当前时间的记录，单位/天 默认7天', '7', NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_artificial_load_and_unload_point_code', '人工上下料点位，多个逗号隔开', NULL, NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_jack_up_agv_start_point_code', '任务下发 根据任务起点 为此4个接驳点时，需要RCS根据任务中货架信息调用货架入场接口，在执行任务', NULL, NULL, 1);
INSERT INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali_jack_up_agv_end_point_code', '任务下发 根据任务终点 为此4个接驳点时，任务结束后需要RCS调用货架出场接口', NULL, NULL, 1);



CREATE TABLE `evo_interface`.`job_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `robot_job_id` varchar(255) DEFAULT NULL COMMENT '上游任务编号',
  `job_id` varchar(255) DEFAULT NULL COMMENT '快仓任务编号',
  `zone_code` varchar(255) DEFAULT NULL COMMENT '仓库库区',
  `warehouse_id` int(11) DEFAULT NULL,
  `status` int(4) DEFAULT NULL COMMENT '状态 1、已创建 2、待执行 3、空车移位上料点开始 4、空车移位上料点完成 5、料箱确认开始  6、料箱确认完成 7、上料请求辊筒转动开始 8、上料-辊筒转动完成 9、辊筒上料执行中 10、辊筒上料完成 11、空车移位下料点开始 12、空车移位下料点完成 13、下料请求辊筒转动开始  14、下料-辊筒转动完成 15、辊筒下料执行中 16、辊筒下料完成 17、已完成 18、失败 19、失败上报完成 20、异常完成',
  `start_point` varchar(255) DEFAULT NULL COMMENT '起点',
  `end_point` varchar(255) DEFAULT NULL COMMENT '目标点',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `let_down_flag` varchar(255) DEFAULT NULL COMMENT '任务类型 offline 离线任务  online 在线任务',
  `bucket_slot_code` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '顶升agv是，货架编码  辊筒agv是料箱编码',
  `work_face` int(1) DEFAULT NULL COMMENT '作业面 1-4',
  `end_area` varchar(255) DEFAULT NULL,
  `job_priority` int(11) DEFAULT NULL COMMENT '任务优先级',
  `agv_type` int(2) DEFAULT NULL COMMENT '1：辊筒AGV  2：顶升AGV',
  `robot_job_group_id` varchar(255) DEFAULT NULL COMMENT 'rcs任务组id',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注',
  `agv_code` varchar(255) DEFAULT NULL COMMENT 'agv_code',
  `job_num` int(1) NOT NULL DEFAULT '1' COMMENT '任务序号',
  `is_valid` int(1) DEFAULT NULL COMMENT '是否有效 1 有 0 否 ',
  `load_type` int(2) DEFAULT NULL COMMENT '上下料方式 1、自动上下料 2、人工上下料',
  PRIMARY KEY (`id`),
  KEY `index_robot_id` (`robot_job_id`,`status`),
  KEY `index_job_id` (`job_id`,`status`),
  KEY `index_agvtype` (`agv_type`)
) ENGINE=InnoDB AUTO_INCREMENT=286 DEFAULT CHARSET=utf8;
