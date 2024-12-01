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
-- Table structure for table `system_notifications`
--

DROP TABLE IF EXISTS `system_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_notifications`
--

LOCK TABLES `system_notifications` WRITE;
/*!40000 ALTER TABLE `system_notifications` DISABLE KEYS */;
INSERT INTO `system_notifications` VALUES (1,'Thông báo về sách mới','Thư viện vừa nhập về một số đầu sách mới thuộc các thể loại văn học, khoa học, và công nghệ. Hãy ghé thăm thư viện để tìm hiểu thêm!','2024-11-01 10:00:00'),(2,'Cập nhật giờ mở cửa','Thư viện sẽ mở cửa từ 7:00 sáng đến 9:00 tối mỗi ngày, áp dụng từ ngày 1 tháng 12.','2024-11-05 08:30:00'),(3,'Quy định trả sách','Vui lòng trả sách đúng hạn để tránh bị phạt. Mức phạt là 5,000 VND mỗi ngày cho mỗi cuốn sách quá hạn.','2024-11-07 15:45:00'),(4,'Thông báo sự kiện','Thư viện tổ chức hội thảo \"Kỹ năng nghiên cứu hiệu quả\" vào ngày 25 tháng 11. Đăng ký ngay tại quầy lễ tân.','2024-11-10 09:00:00'),(5,'Tri ân bạn đọc','Nhân dịp cuối năm, thư viện tặng 5 phiếu mượn sách miễn phí cho 10 bạn đọc mượn nhiều sách nhất trong năm qua.','2024-11-12 18:00:00'),(6,'Bảo trì hệ thống','Hệ thống mượn sách trực tuyến sẽ được bảo trì vào ngày 27 tháng 11 từ 12:00 đêm đến 6:00 sáng. Vui lòng không truy cập trong thời gian này.','2024-11-15 23:30:00'),(7,'Khuyến khích đánh giá sách','Thư viện khuyến khích bạn đọc đánh giá sách đã mượn để nhận điểm thưởng tích lũy.','2024-11-18 11:15:00'),(8,'Thông báo thay đổi vị trí','Một số kệ sách đã được sắp xếp lại để tối ưu không gian. Vui lòng hỏi nhân viên nếu cần hỗ trợ.','2024-11-20 14:20:00'),(9,'Khóa tài khoản tạm thời','Các tài khoản không hoạt động trong 6 tháng qua sẽ bị khóa tạm thời. Vui lòng liên hệ thư viện để kích hoạt lại.','2024-11-21 16:45:00'),(10,'Cảnh báo sách quá hạn','Hiện có nhiều bạn đọc chưa trả sách đúng hạn. Vui lòng kiểm tra lại danh sách sách đã mượn và hoàn trả sớm nhất.','2024-11-22 09:10:00');
/*!40000 ALTER TABLE `system_notifications` ENABLE KEYS */;
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
