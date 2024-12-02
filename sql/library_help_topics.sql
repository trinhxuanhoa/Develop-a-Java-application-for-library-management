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
-- Table structure for table `help_topics`
--

DROP TABLE IF EXISTS `help_topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `help_topics` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` enum('admin','user','all') NOT NULL,
  `question` text NOT NULL,
  `answer` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `help_topics`
--

LOCK TABLES `help_topics` WRITE;
/*!40000 ALTER TABLE `help_topics` DISABLE KEYS */;
INSERT INTO `help_topics` VALUES (20,'admin','Làm thế nào để thêm sách mới vào thư viện?','Để thêm sách mới, truy cập vào phần \"Quản lý Tài liệu\".'),(21,'admin','Làm thế nào để quản lý tài khoản người dùng?','Để quản lý tài khoản người dùng, vào phần \"Quản lý Người Dùng\".'),(22,'admin','Làm thế nào để cấu hình cài đặt hệ thống?','Cài đặt hệ thống có thể cấu hình trong tab \"Cài Đặt\".'),(23,'admin','Làm thế nào để tạo báo cáo?','Để tạo báo cáo, truy cập vào phần \"Báo Cáo\".'),(24,'admin','Làm thế nào để xử lý sách quá hạn và các khoản phạt?','Sách quá hạn có thể được quản lý trong phần \"Quản lý Quá Hạn\".'),(25,'admin','Làm thế nào để cập nhật kho sách của thư viện?','Cập nhật kho sách bao gồm việc thêm sách mới và loại bỏ sách cũ.'),(26,'admin','Làm thế nào để sao lưu và phục hồi cơ sở dữ liệu?','Việc sao lưu và phục hồi cơ sở dữ liệu có thể thực hiện qua menu \"Sao Lưu & Phục Hồi\".'),(27,'user','Làm thế nào để mượn sách?','Để mượn sách, đăng nhập vào tài khoản và truy cập vào phần \"Mượn Sách\".'),(28,'user','Làm thế nào để gia hạn sách đã mượn?','Để gia hạn sách đã mượn, vào \"Tài Khoản Của Tôi\" > \"Sách Đã Mượn\" và nhấn \"Gia Hạn\".'),(29,'user','Làm thế nào để xem lịch sử mượn sách của tôi?','Lịch sử mượn sách của bạn có thể xem trong phần \"Tài Khoản Của Tôi\" > \"Lịch Sử Mượn Sách\".'),(30,'user','Làm thế nào để cập nhật thông tin cá nhân của tôi?','Để cập nhật thông tin cá nhân, vào \"Tài Khoản Của Tôi\" > \"Cài Đặt Hồ Sơ\".'),(31,'user','Làm thế nào để tìm sách trong thư viện?','Sử dụng thanh tìm kiếm ở phía trên của cổng thư viện.'),(32,'user','Làm thế nào để đặt trước sách?','Nếu sách đang được mượn, bạn có thể đặt trước sách bằng cách nhấn nút \"Đặt Trước\".'),(33,'user','Làm thế nào để báo cáo sách bị hỏng hoặc mất?','Để báo cáo sách bị hỏng hoặc mất, vào \"Tài Khoản Của Tôi\" > \"Báo Cáo Vấn Đề\".'),(34,'all','Làm thế nào để liên hệ với hỗ trợ?','Bạn có thể liên hệ với hỗ trợ bằng cách nhấn vào liên kết \"Liên Hệ\" ở dưới cùng trang.'),(35,'all','Làm thế nào để đặt lại mật khẩu của tôi?','Để đặt lại mật khẩu, truy cập trang đăng nhập và nhấn \"Quên Mật Khẩu\".'),(36,'all','Làm thế nào để xem các chính sách của thư viện?','Chính sách thư viện có thể được xem trong phần \"Thông Tin Thư Viện\" > \"Chính Sách\".'),(37,'all','Làm thế nào để thay đổi email tài khoản của tôi?','Để thay đổi email tài khoản, vào \"Tài Khoản Của Tôi\" > \"Cài Đặt Email\".'),(38,'all','Làm thế nào để cập nhật trạng thái thành viên của tôi?','Trạng thái thành viên có thể được cập nhật bằng cách liên hệ với bộ phận quản lý thư viện.');
/*!40000 ALTER TABLE `help_topics` ENABLE KEYS */;
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
