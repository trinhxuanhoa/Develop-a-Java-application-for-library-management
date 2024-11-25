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
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` bigint NOT NULL,
  `book_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `comment` text,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `comments_ibfk_1` (`book_id`),
  KEY `comments_ibfk_2` (`user_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,'dfb','123','dsfsdgdsg','2024-11-17 15:42:51'),(2,'dfb','123','dsfsdgdsg','2024-11-17 15:42:53'),(3,'dfb','123','dsfsdgdsg','2024-11-17 15:42:53'),(4,'dfb','123','dsfsdgdsg','2024-11-17 15:42:54'),(5,'dfb','123','dsfsdgdsg','2024-11-17 15:42:54'),(6,'dfb','123','dsfsdgdsg','2024-11-17 15:42:56'),(7,'dfb','123','dsfsdgdsg','2024-11-17 15:42:56'),(8,'dfb','123','dsg','2024-11-17 15:42:59'),(9,'dfb','123','sgdsg','2024-11-17 15:43:02'),(10,'dfb','123','gsdggg','2024-11-17 15:43:04'),(11,'dfb','123','sdsdv','2024-11-17 15:43:06'),(12,'dfb','123','rgreg','2024-11-17 15:43:08'),(13,'dfb','123','dsgs','2024-11-17 15:43:10'),(14,'dfb','123','trrgrg','2024-11-17 15:53:06'),(15,'dfb','123','èd','2024-11-17 15:55:12'),(16,'6767','123','hgugg','2024-11-17 22:14:37'),(17,'vdfv','123','vsdsd','2024-11-18 12:13:24'),(18,'bvcbvc','123','kuiui8\n','2024-11-20 20:10:53');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-20 21:14:21
