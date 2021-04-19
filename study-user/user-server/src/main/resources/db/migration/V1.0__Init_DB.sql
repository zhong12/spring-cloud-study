
DROP TABLE IF EXISTS `evo_interface`.`config`;
CREATE TABLE `config`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) DEFAULT NULL COMMENT '配置名',
    `describe`    varchar(255) DEFAULT NULL COMMENT '描叙',
    `value`       varchar(255) DEFAULT NULL COMMENT '值',
    `create_time` datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `is_valid`    int(1) NOT NULL DEFAULT '1' COMMENT '是否有效 1 是 0 否',
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='配置表';

INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-job-complete-report-url', '5.2 任务完成回报接口url', 'http://172.31.238.226:32474/agv/job/done', '2021-03-16 18:15:37', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-confirm-bin-url', '5.8.4任务料箱信息确认接口url', 'http://172.31.238.226:32474/api/quicktron/rcs/robot.job.bin.confirm', '2021-03-16 13:54:10', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-roller-interval-long-no-load-time', '辊筒agv多长时间未上料箱，默认 30，单位秒', '30', '2021-03-30 16:01:57', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-delete-records-from-current-time', '删除距离当前时间的记录，单位/天 默认7天', '7', '2021-03-16 13:54:18', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-jack-up-agv-start-point-code', '任务下发 根据任务起点 为此4个接驳点时，需要根据任务中货架信息调用货架入场接口，再执行任务', '5iz8Pb,dGnAjx', '2021-03-30 16:03:11', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-jack-up-agv-end-point-code', '任务下发 根据任务终点 为此4个接驳点时，任务结束后，需要调用货架出场接口', 'hDXpHb,wJXzQY', '2021-03-30 16:03:25', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-roller-load-unload-interactive-url', '5.14 辊筒AGV上下料交互请求接口', 'http://172.31.238.226:32474/api/quicktron/rcs/roller.job.upstream.request', '2021-03-16 13:54:29', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-agv-abnormal-report-url', '5.16 agv异常上报', 'http://172.31.238.226:32474/api/quicktron/wcs/agv.abnormal.report', '2021-03-16 18:07:38', 1);
INSERT IGNORE INTO `evo_interface`.`config`(`name`, `describe`, `value`, `create_time`, `is_valid`) VALUES ('ali-agv-abnormal-report-interval-time', 'agv异常上报间隔时间，默认60，单位秒', '60', '2021-03-30 16:39:02', 1);

CREATE TABLE `job_record`
(
    `id`                  int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
    `warehouse_id`        bigint(16) DEFAULT NULL COMMENT '仓库编号',
    `robot_job_id`        varchar(255)  DEFAULT NULL COMMENT '上游任务编号',
    `status`              varchar(255)  DEFAULT NULL COMMENT '状态：\r\nINIT 初始化\r\nLIFT_UP_DONE顶升完成\r\nMOVE_BEGIN开始移动\r\nPUT_DOWN_DONE放下完成\r\nDONE完成\r\nCANCEL取消\r\nROLLER_LOAD_CONFIRM_BIN确认料箱\r\nROLLER_LOAD_DOING正在上料\r\nROLLER_LOAD_DEVICE_DOING上料设备交互中\r\nROLLER_LOAD_FINISH上料完成\r\nROLLER_UNLOAD_DOING正在下料\r\nROLLER_UNLOAD_DEVICE_DOING下料设备交互中\r\nABNORMAL_COMPLETED 异常完成\r\nROLLER_NOT_LOAD 辊筒一直没上料\r\nABNORMAL_CANCEL 异常取消',
    `start_point`         varchar(255)  DEFAULT NULL COMMENT '起点',
    `end_point`           varchar(255)  DEFAULT NULL COMMENT '目标点',
    `job_type`            int(2) DEFAULT NULL COMMENT '1：辊筒AGV  2：顶升AGV',
    `remarks`             varchar(1000) DEFAULT NULL COMMENT '备注',
    `load_type`           int(2) DEFAULT NULL COMMENT '上下料方式 1、自动上下料 2、人工上下料',
    `load_equipment_id`   bigint(16) DEFAULT NULL COMMENT '上料设备号',
    `unload_equipment_id` bigint(16) DEFAULT NULL COMMENT '下料设备号',
    `code`                varchar(255)  DEFAULT NULL COMMENT '辊筒agv--容器号，顶升agv--货架号',
    `create_time`         datetime      DEFAULT NULL,
    `update_time`         datetime      DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY                   `index_robot_id` (`robot_job_id`,`status`),
    KEY                   `index_job_id` (`status`),
    KEY                   `index_agvtype` (`job_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务记录表';