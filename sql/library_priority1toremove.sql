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
INSERT INTO `priority1toremove` VALUES ('bvcbvc','xcv','bdfb',_binary '�PNG\r\n\Z\n\0\0\0\rIHDR\0\0\0\�\0\0\0>\0\0\0���\�\0\0>IDATx^\�\�K[Y\��O�p���P�R��%�\�R\�\� (H#�8%�\�\�]@Hۅ � D\n��!�\"R����)e6�9\��<�\�Q�\��.>��<r\��=\�m��\�\0\0�\�vv�7\0Z�\�o�\0�u \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����\0K \Z\0,�h\0����� �=z�~A/\�5C\�\�\�%m/\�(3�z\�;\�^{=JN\�\�\�y�o�Z�\�x?%�ӯq�NũQ\Z\�\�}\���ɯ\�ǫ�\�ߝ/�\�kz�~M{\�\�=;T\�|H\�\�9*\�\�Q.\�\'���ׯ:\'��<�̌C٤\�\�O��\�\��R�}��\�\�Q1?Mϵx�o\�8�\�1\\\��,;�J�\�m\��\�E�WxA\�\n{�뷁F\�3\���\��W(�t@uS��u\���Oh\��\n9�\\8iZ�E\�HՑ�J\�/\�Ǌ\�\�:�L;�\�\'C\�mdK\ZmϬO4&j%\Zkjܧ��T��\�\�*�h��q��(\�!��\�Y١g�ә��9��q\�	\�9ڸ\�\�_e{\�}7G�)\�G�]Q�\�B(�[<�m,\�)\�\'�y\�ҩa-\�\�T��\�{�\�3�D\�\�\�0\�0\�\�v��4\��2�\��G8�\�Zc\0��uq�#��S\�\�$�y���÷�yХ����\�\�9mϧ\�,4���HOy�\n:<\��\�\�Qk��HO#64\Z&�8�?��֐�W\���V8��x3,�\�I��:��2��\��rD\�Q���x_�v\�<\r�o#\�K\�\�\�\��\� }\���3��?�\�C18�@�M�� �.\�?�rh���\��9+G��.^�`�\�\�\'\���Žg\�e>tR�	~�ݣ�\��a\�\�\�E\�5�-\Z�wDX\�\�M��\�Q�\�p3J8�\���\"\�:\��\�ϐ:���mR(j\��c�f�\r\n\0��i�p�L\0�\�R�\0?\�\�B^aw��x\�2QעxL\���ЂxZ\n$\���1\\\�\�;n�,qS�g K��\�6g�e�Tp^\��\Z7\�9ʍ)�d\��\��S\���\�\�˼��.V)\���B�\��Y�\�`�{���ki�Ϲ~·\�z����w���%\�µW�\�=7���i�\�\0.icJ\Z{q?\�&ٝ�\'y\�t�4�j>\�y\"�\r7_�\�\�\'g���\�T�:�w\�;�Ĉ=\�Ak9[��e8��@L���\�DxL\0�L�\�^\�K�hB�\�z\�m�\�E\�u\�>L\�\0�h�&T8%Dd\�sZ\�F\0_�#X\�d\�Ths+\�.�/��\�4K�K�0���p9*\�z�^��\�\�w�Wc!\�*dj\�Q�k<�\r^�X\��Do�2\��ʆ�\�\�R!���\�:b�\��\� \r\���wO4ߔ1�͐\�o�\�f���χ+�P\�@?\\\�7�\�\��gGd8s0o260/7�X�K�yde�\�MăČ��Dc�\�ibC\�&鿆͹I@r�(^S^�qO辰�_\�\�ZE\Z�~�Sa\� �\�ih\�Sz��\�s�C�g\n\�\�+�b-�| \�m�@\��	\�`y��~�\�Cp�J���c�hl	���1�X*}�M�A�劭CT:d8R=�*T�*޲��	��*\�9f\���%P=\�\�\�)A���!=�5ߛ�\�!_\�H\�_�\�0Y\�\���蛔խ�̝`9�\�`0\�\Z���9S»�\�/_��\�\�/]	l�\�\�xc�hl��ܔ\�f[r�@�sڜ��a.a����\"m�����\���A�R\�gy\�S�]7\�QYM�}��[\�R����3�ߙ\n��\�ޟh�!�wJ�wN�TX	~I\�\n\�k9��D�M\�}�Q^�k\��-f\���\0\0�sxv\���2\�.\0\0p;�&�\0A�F\�1\0͉\�4\0�\�\�\0`	D�%\r\0�@4\0X\�\0`	D�%\r\0�@4\0X\�\0`	D�%�\�\�*��\�=\0\0\0\0IEND�B`�','434',4,'2024-11-24 08:28:20',1),('cxbcx','vcbc','xzbx',NULL,'434',2,'2024-11-25 13:15:21',2),('bfvb','fbdf','hôas',NULL,'434',1,'2024-11-25 13:14:43',1),('dvdfv','dfv','Vô danh',NULL,'434',1,'2024-11-25 13:13:04',1),('B001','Văn học Cổ điển','Tác giả A',NULL,'Văn học',1,'2024-11-24 08:28:20',1);
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

-- Dump completed on 2024-12-02 23:33:30
