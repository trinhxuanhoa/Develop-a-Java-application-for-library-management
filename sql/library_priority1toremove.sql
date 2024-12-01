-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: library
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `priority1toremove`
--

DROP TABLE IF EXISTS `priority1toremove`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `priority1toremove` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `title` varchar(255) NOT NULL DEFAULT '',
  `author` varchar(255) NOT NULL DEFAULT '',
  `cover_image` longblob,
  `genre` varchar(255) DEFAULT NULL,
  `downloads` int DEFAULT NULL,
  `date_added` timestamp NULL DEFAULT NULL,
  `priority` bigint NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `priority1toremove`
--

LOCK TABLES `priority1toremove` WRITE;
/*!40000 ALTER TABLE `priority1toremove` DISABLE KEYS */;
INSERT INTO `priority1toremove` VALUES ('bvcbvc','xcv','bdfb',_binary '‰PNG\r\n\Z\n\0\0\0\rIHDR\0\0\0\Í\0\0\0>\0\0\0ˆºŸ\Þ\0\0>IDATx^\íš\ËK[Y\ÇýOºp£¤…P„R„Š% \ÐR\É\Â (H#³8%¸\à\Â]@HÛ… ´ D\n¥ˆ! \"R±•Œ±)e6¿9\Ïû<·\ÉQú\Ðù.>ˆ÷<rî¹¿\ïù=\îmûò\åŠ\0\0­\Óvvö7\0Z§\íŸoÿ\0 u \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢Àˆ\0K \Z\0,h\0°¢À’Ÿ š=z•~A/\Ë5C\Û\ï\Ì%m/\æ(3ðz\Ú;\è^{=JN\Ó\Â\Öy¤o½Z¢\Âx?%¾Ó¯q¸NÅ©Q\Z\ì\æ}\Ýý”É¯\ÒÇ«ð\ï‚ß/š\êkz–~M{\á\ë¿=;T\è|H\Ã\ã9*\Î\ÎQ.\å‰\'÷þ«×¯:\'®÷<¢ÌŒCÙ¤\î\×O…ª\×\ïóRš}£”\Í\ÏQ1?MÏµx’o\é8ò\Û1\\\Ðò,;µJŸ\Ãm\à§ñ\ÃE³WxA\Ï\n{‘ë·F\È3\Ã†ž\ßñ®W(»t@uS¿©u\ïú•Oh\âÿ\n9½\\8iZ®E\ÛHÕ‘ó¦J\Í/\äÇŠ\æ\Ó:½L;ô\×\'C\ÛmdK\ZmÏ¬O4&j%\ZkjÜ§´üTŠ¦\Ñ\Ü*„h¶óq‚…(\ì!…µ\ÊYÙ¡g®Ó™¡­9—ôq\Å	\æ9Ú¸\Ð\í_e{\ß}7G›)\ÑG·]Q«\ÐB(—[<’m,\Ü)\ç\'ƒy\ÆÒ©a-\Ì\ëœT¨\ì¢{\Ì3œD\Û\ì\é‰0\î˜0\ê\êœv—²4\Èú2¯\Õ·G8§\å”Zc\0‡¶uqŸ#ô¨S\ï\Õ$ýy•ÿ½Ã·”yÐ¥ž¹ž›\ß\×9mÏ§\å,4˜¯HOyò\n:<\í¡\Ü\ÒQk¾»HO#64\Z&ð8œ?˜ð Ö€W\ÕðõV8¥ò¸x3,˜\åIù´:•¿2¡÷\ãžrD\ÎQœ•‰x_‘v\Õ<\r–o#\ê K\Î\Ö\Ï\áš\çžâ€Š}\ÒÀô3º„?ô\ÒC18¾@›M½ó ©.\Ñ?³rh‡“ž\ïÁ9+G°.^”`÷\È\Ö\'\Æö¦Å½g\×e>tR¢	~ŸÝ£”\×ùa\Ã\×\ÐE\Ù5µ-\Z–wDX\Ø\ÍM¤†\èQŠ\åp3J8¬\Ïóù\"\å:\ï³‰\ïÏ:ÀòµÿmR(j\ãüc¼fÁ\r\n\0õµiùp’L\0†\êRƒ\0?\É\ÇB^awþ±x\Ð2Q×¢xL\Ùƒ÷Ð‚xZ\n$\âõŸ1\\\ì\Ð;n„,qS­g K±ž\æœ6gúe¿Tp^\Îñ\Z7\è9Ê)£d\Æø\æ õS\Ûž©\ç\ÔË¼Ž¯.V)\Ãû¨Bƒ\ËYô\ç`ž{’¯¸ki¼Ï¹~Â·\Ïz›†¨w˜¶±%\éÂµWñ\Ü=7ª¨÷i•\ë\0.icJ\Z{q?\Ü&Ù•\'y\âžtˆ4¾j>\Íy\"®\r7_¢\í“\æ\'g’˜\ÓTñº:¢w\Ê;öÄˆ=\ÜAk9[ú·e8¸¿@L¢©­\ÒDxL\0õLõ\Ø^\ÏK´hBû\íz\Ùmú\ïE\åu\×>L\ï\0®hô&T8%Dd\ÌsZ\àF\0_Œ#X\æd\ÞThs+\Ê.€/†\×4K”Kª0°‡p9*\Çzó^¦Š\×\Ëw„Wc!\Ü*dj\îQƒk<¥\r^ùX\ÜñDo2\î‡Ê†½\Ø\ÜR! »•\Ð:bö\Û\ï \r\Ì¿¯wO4ß”1ŠÍ\éo³\áf€˜“Ï‡+šP\Î@?\\\Ù7†\Æ\É•gGd8s0o260/7šX´Kyde€\ßMÄƒÄŒüDc‹\ÊibC\Ø&é¿†Í¹I@r¼(^S^ÀqOè¾°_\Ò\îZE\Z”~õSa\Ë ®\Úih\îSz—ôŒ\çsõC¤g\n\Ï\ê+“b-þ| \Êm¾@\Æð¬	\Ú`y˜ö~Á\ÜCp²J‡¡±c‡hl	½§‘1õX*}ýM¹AÀåŠ­CT:d8R=ó*T¼*Þ²³ö	·Œ*\ç9f\íÿ´%P=\ã\ß\ë«)Aˆð´ó!=•5ß›þ\Î!_\åH\ç_ò‹€\á0Y\Ú\ë•ûšè›”Õ­¯Ì`9—\é`0\â\Z²¬ø9SÂ»ð\Â/_‹û\ä\Õ/]	l÷\Â\íxc‡hl‰¼Ü”\ßf[rý@˜sÚœŸ¤a.a¬ü“–\"m»§¿©\ÅõûA¯R\ßgy\ËSý]7\ÜQYM«} ‚[\ÅRŸÁø¿3½ß™\n—œ\ãÞŸh´!ªwJwN“TX	~I\Ð\n\Çk9÷œDŸM\ì}¾Q^—k\ì-f\Ñü’\0\0·sxv\Í€ð2\×.\0\0p;ˆ&ò®\0AˆF\ç1\0Í‰\ä4\0€\ï\Ñ\0`	D€%\r\0–@4\0X\Ñ\0`	D€%\r\0–@4\0X\Ñ\0`	D€%ÿ\ë\Ë*—Œ\Ì=\0\0\0\0IEND®B`‚','434',4,'2024-11-24 08:28:20',1),('cxbcx','vcbc','xzbx',NULL,'434',2,'2024-11-25 13:15:21',2),('bfvb','fbdf','hÃ´as',NULL,'434',1,'2024-11-25 13:14:43',1),('dvdfv','dfv','VÃ´ danh',NULL,'434',1,'2024-11-25 13:13:04',1),('B001','VÄƒn há»c Cá»• Ä‘iá»ƒn','TÃ¡c giáº£ A',NULL,'VÄƒn há»c',1,'2024-11-24 08:28:20',1);
/*!40000 ALTER TABLE `priority1toremove` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-01 22:26:47
