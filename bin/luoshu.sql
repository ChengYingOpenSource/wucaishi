SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ep_data_interface
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_interface`;
CREATE TABLE `ep_data_interface`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `uri` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '路径',
  `method` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求方式(GET\\POST\\DELETE...)',
  `data_packager_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组装服务编码',
  `status` int(1) NOT NULL COMMENT '状态(0:待上线,1:已上线)',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`, `version`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_packager
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_packager`;
CREATE TABLE `ep_data_packager`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组装服务名称',
  `req_datastructure_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求数据结构编码',
  `resp_datastructure_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '响应数据结构编码',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_code`(`code`, `version`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_packager_script
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_packager_script`;
CREATE TABLE `ep_data_packager_script`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_packager_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据组装服务编码',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `script_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '脚本类型',
  `script_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '脚本内容',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_data_packager_code`(`data_packager_code`, `version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_packager_view
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_packager_view`;
CREATE TABLE `ep_data_packager_view`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_packager_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据组装服务编码',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `data_view_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据视图编码',
  `data_view_alias` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据视图别名',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_data_packager_code`(`data_packager_code`, `version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_source_v2
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_source_v2`;
CREATE TABLE `ep_data_source_v2`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '自增，唯一id',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源名称，不能有中文，唯一',
  `ds_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源类型',
  `conf` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据源配置，不同数据源内含字段不同',
  `remark` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`code`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_structure
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_structure`;
CREATE TABLE `ep_data_structure`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段名称',
  `field` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字段key',
  `data_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据类型',
  `is_required` bit(1) NOT NULL COMMENT '是否必须',
  `parent_code` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父编码',
  `target_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标编码',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_code`(`code`) USING BTREE,
  INDEX `idx_parent_code`(`parent_code`) USING BTREE,
  INDEX `idx_target_code`(`target_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_data_view
-- ----------------------------
DROP TABLE IF EXISTS `ep_data_view`;
CREATE TABLE `ep_data_view`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '视图名称',
  `params` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '视图参数',
  `datasource_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源编码',
  `req_datastructure_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求数据结构编码',
  `resp_datastructure_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '响应数据结构编码',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_code`(`code`, `version`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_project_datasource
-- ----------------------------
DROP TABLE IF EXISTS `ep_project_datasource`;
CREATE TABLE `ep_project_datasource`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目编码',
  `datasource_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据源编码',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`project_code`, `datasource_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_project_module
-- ----------------------------
DROP TABLE IF EXISTS `ep_project_module`;
CREATE TABLE `ep_project_module`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `project_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目编码',
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE,
  INDEX `idx_project_code`(`project_code`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_project_module_gateway
-- ----------------------------
DROP TABLE IF EXISTS `ep_project_module_gateway`;
CREATE TABLE `ep_project_module_gateway`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目编码',
  `module_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块编码',
  `data_interface_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口编码',
  `version` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口版本',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`project_code`, `module_code`, `data_interface_code`, `version`) USING BTREE,
  INDEX `idx_project_code`(`project_code`) USING BTREE,
  INDEX `idx_module_interface`(`module_code`, `data_interface_code`, `version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_project_v2
-- ----------------------------
DROP TABLE IF EXISTS `ep_project_v2`;
CREATE TABLE `ep_project_v2`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `gmt_created` datetime(0) NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ep_pipeline
-- ----------------------------
DROP TABLE IF EXISTS `ep_pipeline`;
CREATE TABLE `ep_pipeline` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(128) NOT NULL COMMENT '编码',
  `status` int(2) NOT NULL COMMENT '状态',
  `data` blob NOT NULL COMMENT '暂存数据',
  `data_interface_code` varchar(64) DEFAULT NULL COMMENT '生成的接口编码',
  `version` varchar(16) DEFAULT NULL COMMENT '接口版本',
  `gmt_created` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `gmt_creator` bigint(32) NOT NULL COMMENT '创建人',
  `gmt_modifier` bigint(32) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_data_interface_code` (`data_interface_code`)
) ENGINE=InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
