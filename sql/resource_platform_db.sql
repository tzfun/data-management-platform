/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.7.20-log : Database - resource_platform_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`resource_platform_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `resource_platform_db`;

/*Table structure for table `article_tb` */

DROP TABLE IF EXISTS `article_tb`;

CREATE TABLE `article_tb` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `title` varchar(128) NOT NULL COMMENT '文章标题',
  `summary` varchar(320) NOT NULL COMMENT '文章摘要',
  `content` longtext NOT NULL COMMENT '文章主体内容',
  `tags` varchar(1024) DEFAULT NULL COMMENT '标签',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `see_num` int(16) DEFAULT '0' COMMENT '阅读量',
  `has_pass` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否审核通过',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `article_tb_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article_tb` */

/*Table structure for table `notice_tb` */

DROP TABLE IF EXISTS `notice_tb`;

CREATE TABLE `notice_tb` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL COMMENT '发布者id',
  `title` varchar(128) DEFAULT NULL COMMENT '公告标题',
  `content` longtext COMMENT '公告主体内容',
  `long_term` tinyint(1) DEFAULT '0' COMMENT '是否是长期公告，默认false',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `see_num` int(11) DEFAULT '0',
  `summary` varchar(128) DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知公告表';

/*Data for the table `notice_tb` */

/*Table structure for table `permission_tb` */

DROP TABLE IF EXISTS `permission_tb`;

CREATE TABLE `permission_tb` (
  `id` varchar(32) NOT NULL,
  `resource` varchar(16) NOT NULL,
  `action` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `permission_tb` */

insert  into `permission_tb`(`id`,`resource`,`action`) values ('05c6acdc8db548b09ac1e7a00db5','message','select'),('29c30519c11a45de9f41e953931e','article','update'),('2d3ef250c0144ca887a0ed1f4620','article','delete'),('2e742e94f6e2485789b08494d948','notice','select'),('3018d9b55ed64fcd988eb66e1934','message','add'),('3a7ba25f88f34ccfacaec06b6f542792','user','add'),('3dd02cce719a425db8354fac845f','system','add'),('3df43b6e0bab4179880b19cdaf22','system','delete'),('476160d11a3a4aa9bff9bb8a18c7','permission','add'),('4de0cf34944348bea763d3285cc5','resource','add'),('52775973ae344e6f8aaa7df326f6','permission','select'),('5bbdafe20cce4ee49be56157cc4d','message','update'),('63b6bf110e1a4b7baf915f2b9f99','notice','update'),('63d25bd49c73400c81c1b59e8b82','permission','delete'),('6cb643915e604c38b50cd5abf816','notice','delete'),('732a46f5b4d94cac9a06cf21b6f1','system','update'),('7d301dbb48b14c9abd8952cc242f','notice','add'),('8193c6521cbd45608649949ef2b2','message','delete'),('88da4826553f4f8c8ca4a12ea84fb584','user','delete'),('a0ef5ec8c02948e4bd85c60f828d3965','image','add'),('a11e3073d3d8463c94bced972143','resource','select'),('a510342e7d1947c98abf8cd0b1e3','resource','update'),('c558df2c2dc04f95aa02febda350','system','select'),('e2bd223df1e845ea870a076c862c','article','add'),('eb32fa1aeb894dae8161172a1bf3','permission','update'),('eb69b176c0a84192bdf728e50dff9a4d','user','update'),('f1e2083b3ae24b308e912586837f','article','select'),('fb133500c6154af4a4781c3a38966152','user','select'),('fdb8daeb9b70430c8ea8e5e29604','resource','delete');

/*Table structure for table `resource_tb` */

DROP TABLE IF EXISTS `resource_tb`;

CREATE TABLE `resource_tb` (
  `id` varchar(32) NOT NULL COMMENT '资源id',
  `title` varchar(128) NOT NULL COMMENT '资源标题',
  `summary` varchar(320) NOT NULL COMMENT '资源摘要',
  `introduction` longtext NOT NULL COMMENT '资源介绍',
  `type` varchar(32) NOT NULL COMMENT '资源类型',
  `img_url` varchar(128) NOT NULL COMMENT '资源图片地址',
  `file_url` varchar(128) NOT NULL COMMENT '资源文件地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `see_num` int(11) DEFAULT '0',
  `download_num` int(11) NOT NULL DEFAULT '0',
  `file_name` varchar(64) DEFAULT NULL COMMENT '文件名',
  `file_size` mediumtext COMMENT '文件大小，单位KB',
  `file_type` varchar(32) DEFAULT NULL COMMENT '文件类型',
  `file_full_type` varchar(256) DEFAULT NULL COMMENT '文件类型全称',
  `has_pass` tinyint(1) DEFAULT '1' COMMENT '是否审核通过，默认通过',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `resource_tb_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `resource_tb` */

/*Table structure for table `role_permission_tb` */

DROP TABLE IF EXISTS `role_permission_tb`;

CREATE TABLE `role_permission_tb` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) NOT NULL,
  `permission_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_permission_tb` (`permission_id`),
  KEY `FK_role_permission_tb` (`role_id`),
  CONSTRAINT `FK_permission_tb` FOREIGN KEY (`permission_id`) REFERENCES `permission_tb` (`id`),
  CONSTRAINT `FK_role_permission_tb` FOREIGN KEY (`role_id`) REFERENCES `role_tb` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_permission_tb` */

insert  into `role_permission_tb`(`id`,`role_id`,`permission_id`) values ('039ecf46e25f4d93a3874994a443','918de8f830674f429edfc21b11fa0765','6cb643915e604c38b50cd5abf816'),('09798f5dc38a48bab8356529b30d9300','918de8f830674f429edfc21b11fa0765','2d3ef250c0144ca887a0ed1f4620'),('0ee7025bf29d423ba7bdcb84fb03','918de8f830674f429edfc21b11fa0765','a510342e7d1947c98abf8cd0b1e3'),('118329c518ba411cbd2be55962db','918de8f830674f429edfc21b11fa0765','3dd02cce719a425db8354fac845f'),('3f815cd05f6c4fa8a866b3b9280a','918de8f830674f429edfc21b11fa0765','3018d9b55ed64fcd988eb66e1934'),('43d0b0ef0a974899a17619b800fe','918de8f830674f429edfc21b11fa0765','8193c6521cbd45608649949ef2b2'),('488d27dc6e9e451fb1ca84257906c33b','918de8f830674f429edfc21b11fa0765','3a7ba25f88f34ccfacaec06b6f542792'),('49f75b3f49f04234b392d11e47c4','918de8f830674f429edfc21b11fa0765','29c30519c11a45de9f41e953931e'),('573848aff4d64ebc97f1025fd15d355e','918de8f830674f429edfc21b11fa0765','eb69b176c0a84192bdf728e50dff9a4d'),('59435663c956434d97046a3c12df','918de8f830674f429edfc21b11fa0765','732a46f5b4d94cac9a06cf21b6f1'),('60718c35f981400dad1d914ac338','918de8f830674f429edfc21b11fa0765','eb32fa1aeb894dae8161172a1bf3'),('63803db72dec4e4ab624dc795e38','918de8f830674f429edfc21b11fa0765','a0ef5ec8c02948e4bd85c60f828d3965'),('646f14c414214925b24449641ca2','918de8f830674f429edfc21b11fa0765','a11e3073d3d8463c94bced972143'),('65f81a7e437e401a85c10b4d2dd6','918de8f830674f429edfc21b11fa0765','3df43b6e0bab4179880b19cdaf22'),('6ff7033a0d464ce29b54eefd977f','918de8f830674f429edfc21b11fa0765','5bbdafe20cce4ee49be56157cc4d'),('74efccaa9dc242be888c83eec7be','918de8f830674f429edfc21b11fa0765','7d301dbb48b14c9abd8952cc242f'),('7b7bf543976d482488c6e73b0abb','918de8f830674f429edfc21b11fa0765','e2bd223df1e845ea870a076c862c'),('9c972528988447848037516abdc7','918de8f830674f429edfc21b11fa0765','4de0cf34944348bea763d3285cc5'),('a4f7660945204593aec2a6dcc132','918de8f830674f429edfc21b11fa0765','05c6acdc8db548b09ac1e7a00db5'),('b0d3a747e47241e6a39189fa78a0','918de8f830674f429edfc21b11fa0765','63b6bf110e1a4b7baf915f2b9f99'),('b1d7592437ea4921a5916973608b','918de8f830674f429edfc21b11fa0765','63d25bd49c73400c81c1b59e8b82'),('b6bc45de33de4c70bd3c66d40ad2','918de8f830674f429edfc21b11fa0765','2e742e94f6e2485789b08494d948'),('c4c408e8b7964f18a4f787720151','918de8f830674f429edfc21b11fa0765','fdb8daeb9b70430c8ea8e5e29604'),('c67bb20214a3478787593abc87812f5d','918de8f830674f429edfc21b11fa0765','88da4826553f4f8c8ca4a12ea84fb584'),('c986daec19f348cf9b3ebb9cb9fc','918de8f830674f429edfc21b11fa0765','f1e2083b3ae24b308e912586837f'),('cf39b629d9854681b5b0d93a68d4','918de8f830674f429edfc21b11fa0765','52775973ae344e6f8aaa7df326f6'),('dfd3213ea10649c08976b5e9628ac36b','918de8f830674f429edfc21b11fa0765','fb133500c6154af4a4781c3a38966152'),('e1620f180fb8405e98a86212a894','918de8f830674f429edfc21b11fa0765','476160d11a3a4aa9bff9bb8a18c7'),('eba473a0c360482a89def91f3b8a','918de8f830674f429edfc21b11fa0765','c558df2c2dc04f95aa02febda350');

/*Table structure for table `role_tb` */

DROP TABLE IF EXISTS `role_tb`;

CREATE TABLE `role_tb` (
  `id` varchar(32) NOT NULL,
  `role_name` varchar(16) NOT NULL COMMENT '角色名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_tb` */

insert  into `role_tb`(`id`,`role_name`,`create_time`) values ('918de8f830674f429edfc21b11fa0765','sAdmin','2018-12-22 15:13:42');

/*Table structure for table `user_role_tb` */

DROP TABLE IF EXISTS `user_role_tb`;

CREATE TABLE `user_role_tb` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `role_id` varchar(32) NOT NULL COMMENT '角色id（不同角色对于不同权限）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色分配时间',
  PRIMARY KEY (`id`),
  KEY `FK_role_tb` (`role_id`),
  KEY `FK_user_role_tb` (`user_id`),
  CONSTRAINT `FK_role_tb` FOREIGN KEY (`role_id`) REFERENCES `role_tb` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_user_role_tb` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role_tb` */

insert  into `user_role_tb`(`id`,`user_id`,`role_id`,`create_time`) values ('5f0369bbab1948ed99acd84a18bc9dcb','48d4220c7ab24cdf8f72a02db2656fa6','918de8f830674f429edfc21b11fa0765','2018-12-23 16:53:14');

/*Table structure for table `user_tb` */

DROP TABLE IF EXISTS `user_tb`;

CREATE TABLE `user_tb` (
  `id` varchar(64) NOT NULL COMMENT '用户id',
  `account` varchar(32) NOT NULL COMMENT '用户名',
  `real_name` varchar(16) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `sicau_id` varchar(16) DEFAULT NULL COMMENT '学号',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `telephone` mediumtext COMMENT '手机号码',
  `website` varchar(64) DEFAULT NULL COMMENT '个人网站',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sex` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0男1女',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_tb` */

insert  into `user_tb`(`id`,`account`,`real_name`,`password`,`sicau_id`,`email`,`telephone`,`website`,`create_time`,`update_time`,`sex`) values ('48d4220c7ab24cdf8f72a02db2656fa6','beifeng','田智','e10adc3949ba59abbe56e057f20f883e','213123订单','','0','','2018-12-23 16:52:59','2018-12-23 16:58:42',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
