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
-- Table structure for table `borrow`
--

DROP TABLE IF EXISTS `borrow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow` (
  `borrow_id` mediumint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(36) NOT NULL,
  `book_id` varchar(255) NOT NULL,
  `borrow_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('đã mượn','trả đúng hạn','trả quá hạn','đang chờ duyệt') DEFAULT 'đã mượn',
  `due_date` date DEFAULT NULL,
  PRIMARY KEY (`borrow_id`),
  KEY `borrow_ibfk_1` (`user_id`),
  KEY `borrow_ibfk_2` (`book_id`),
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrow`
--

LOCK TABLES `borrow` WRITE;
/*!40000 ALTER TABLE `borrow` DISABLE KEYS */;
INSERT INTO `borrow` VALUES (2,'123','bvcbvc','2024-11-18','2024-11-20','trả đúng hạn','2024-12-18'),(6,'123','bvcbvc','2024-11-20','2024-11-23','trả đúng hạn','2024-12-20'),(9,'123','fdgfd','2024-11-20','2024-11-23','trả đúng hạn','2024-12-20'),(11,'123','bvcbvc','2024-11-23','2024-11-24','trả đúng hạn','2024-12-23'),(44,'123','123','2024-11-27','2024-11-27','trả đúng hạn','2024-12-27'),(45,'123','B001','2024-11-28','2024-11-29','trả đúng hạn','2024-12-28'),(46,'123','123','2024-11-29','2024-11-29','trả đúng hạn','2024-12-29'),(50,'123','32','2024-11-29','2024-12-01','trả đúng hạn','2024-12-29'),(51,'123','123','2024-12-01','2024-12-01','trả đúng hạn','2024-12-31'),(52,'123','32','2024-12-01','2024-12-01','trả đúng hạn','2024-12-31'),(53,'123','123','2024-12-01','2024-12-01','trả đúng hạn','2024-12-31');
/*!40000 ALTER TABLE `borrow` ENABLE KEYS */;
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
