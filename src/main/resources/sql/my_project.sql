-- MySQL dump 10.13  Distrib 5.6.27, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: my_project
-- ------------------------------------------------------
-- Server version	5.6.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `ID` varchar(32) NOT NULL,
  `CODE` varchar(20) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `REMARK` varchar(200) DEFAULT NULL,
  `CREATE_USER` varchar(32) DEFAULT NULL,
  `CREATE_DATE` char(19) DEFAULT NULL,
  `UPDATE_USER` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` char(19) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES ('0597bac6f47e48f2a88bad1745b83690','guest','游客',NULL,NULL,NULL,NULL,NULL),('1567618ce84c4d07b5564e5a077faa09','admin','管理员',NULL,NULL,NULL,NULL,NULL),('4994a9eb5ef34e748487252682c66cce','user','普通用户',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `ID` varchar(32) NOT NULL,
  `USER_NAME` varchar(20) DEFAULT NULL,
  `LOGIN_NAME` varchar(20) DEFAULT NULL,
  `PASSWORD` varchar(32) DEFAULT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(40) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `IS_UPDATE_PWD` varchar(10) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `CREATE_USER` varchar(32) DEFAULT NULL,
  `CREATE_DATE` varchar(20) DEFAULT NULL,
  `UPDATE_USER` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` varchar(20) DEFAULT NULL,
  `ROLE_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES ('133b3ffdbe994bd68396a96fe42a6118','管理员','admin','4QrcOUm6Wau+VuBX8g+IPg==','18613213275',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1567618ce84c4d07b5564e5a077faa09'),('3ffdbe994bd68396a9b33b6fe42a6118','客户','customer','4QrcOUm6Wau+VuBX8g+IPg==',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4994a9eb5ef34e748487252682c66cce'),('dbe994bd68396a96b33b3fffe42a6118','游客','guest','4QrcOUm6Wau+VuBX8g+IPg==',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0597bac6f47e48f2a88bad1745b83690'),('fe42a6118b33b3ffdbe994bd68396a96','测试','test','4QrcOUm6Wau+VuBX8g+IPg==',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0597bac6f47e48f2a88bad1745b83690');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-24 16:45:38
