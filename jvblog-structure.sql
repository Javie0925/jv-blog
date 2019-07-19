/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.36 : Database - myblog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`myblog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `myblog`;

/*Table structure for table `tb_about_me` */

DROP TABLE IF EXISTS `tb_about_me`;

CREATE TABLE `tb_about_me` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) DEFAULT NULL,
  `avator` varchar(200) DEFAULT NULL,
  `job` varchar(50) DEFAULT NULL,
  `contact` varchar(500) DEFAULT NULL COMMENT 'json格式',
  `summary` varchar(100) DEFAULT NULL,
  `html` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_administrator` */

DROP TABLE IF EXISTS `tb_administrator`;

CREATE TABLE `tb_administrator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(50) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `current_login_time` datetime DEFAULT NULL,
  `current_login_ip` varchar(100) DEFAULT NULL,
  `last_login_ip` varchar(100) DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_reason` varchar(1000) DEFAULT NULL,
  `avilable` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_blog` */

DROP TABLE IF EXISTS `tb_blog`;

CREATE TABLE `tb_blog` (
  `blog_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tb_blog_detail` */

DROP TABLE IF EXISTS `tb_blog_detail`;

CREATE TABLE `tb_blog_detail` (
  `blog_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `author` varchar(20) DEFAULT NULL,
  `profile_image` varchar(1000) DEFAULT NULL,
  `summary` varchar(1000) NOT NULL,
  `content` mediumtext,
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  `watch` int(11) DEFAULT '0',
  `tag_id` int(11) DEFAULT '1',
  `visible` tinyint(1) DEFAULT NULL,
  `comment_num` int(11) DEFAULT '0',
  `like_num` int(11) DEFAULT NULL,
  `Original` tinyint(1) DEFAULT '1',
  `original_url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_blogpage_info` */

DROP TABLE IF EXISTS `tb_blogpage_info`;

CREATE TABLE `tb_blogpage_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_name` varchar(100) DEFAULT NULL,
  `header_logo` varchar(200) DEFAULT NULL,
  `topping_blog_id` int(11) DEFAULT NULL,
  `links` varchar(500) DEFAULT NULL,
  `header_msg` varchar(500) DEFAULT NULL,
  `footer_msg` varchar(200) DEFAULT NULL,
  `visit` int(11) DEFAULT NULL,
  `admin_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_category` */

DROP TABLE IF EXISTS `tb_category`;

CREATE TABLE `tb_category` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `cname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_comment` */

DROP TABLE IF EXISTS `tb_comment`;

CREATE TABLE `tb_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `text` mediumtext CHARACTER SET utf8mb4,
  `create_time` datetime DEFAULT NULL,
  `blog_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `ip` varchar(200) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_index_info` */

DROP TABLE IF EXISTS `tb_index_info`;

CREATE TABLE `tb_index_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topping_blog_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_message` */

DROP TABLE IF EXISTS `tb_message`;

CREATE TABLE `tb_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) DEFAULT '匿名',
  `email` varchar(50) DEFAULT NULL,
  `text` varchar(10000) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_slide_pics` */

DROP TABLE IF EXISTS `tb_slide_pics`;

CREATE TABLE `tb_slide_pics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL,
  `text` varchar(200) DEFAULT NULL,
  `link` varchar(200) DEFAULT NULL,
  `visible` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_tag` */

DROP TABLE IF EXISTS `tb_tag`;

CREATE TABLE `tb_tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `cid` (`cid`),
  CONSTRAINT `cid` FOREIGN KEY (`cid`) REFERENCES `tb_category` (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `avator` varchar(100) DEFAULT NULL,
  `about` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_visitor` */

DROP TABLE IF EXISTS `tb_visitor`;

CREATE TABLE `tb_visitor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(100) DEFAULT NULL,
  `address` varchar(1000) DEFAULT NULL,
  `user_agent` varchar(1000) DEFAULT NULL,
  `visit_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2808 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
