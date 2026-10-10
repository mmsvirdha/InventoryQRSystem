-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventory_db
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `application_settings`
--

DROP TABLE IF EXISTS `application_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_settings` (
  `setting_id` int NOT NULL AUTO_INCREMENT,
  `company_name` varchar(255) NOT NULL,
  `business_type` varchar(50) DEFAULT NULL,
  `business_address` varchar(255) DEFAULT NULL,
  `phone_number` varchar(30) DEFAULT NULL,
  `email_address` varchar(100) DEFAULT NULL,
  `website` varchar(150) DEFAULT NULL,
  `tax_id` varchar(50) DEFAULT NULL,
  `currency` varchar(10) DEFAULT 'USD',
  `time_zone` varchar(50) DEFAULT 'GMT+5:30',
  `date_format` varchar(50) DEFAULT 'yyyy-MM-dd',
  `time_format` varchar(50) DEFAULT 'HH:mm:ss',
  `language` varchar(50) DEFAULT 'English',
  `number_format` varchar(50) DEFAULT '1,234.56',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`setting_id`),
  UNIQUE KEY `company_name` (`company_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_settings`
--

LOCK TABLES `application_settings` WRITE;
/*!40000 ALTER TABLE `application_settings` DISABLE KEYS */;
INSERT INTO `application_settings` VALUES (1,'Default Company','Retail','123 Main St','+1234567890','info@default.com','www.default.com','TAX123','USD','GMT+5:30','yyyy-MM-dd','HH:mm:ss','English','1,234.56','2025-12-02 08:10:13','2025-12-02 08:10:13'),(2,'VM Company','Service','Colombo123','89798287','vm@gmail.com','http://www.VM.com','TAX124','LKR','GMT+5:30','dd/MM/yyyy','HH:mm:ss','English','1,234.56','2025-12-02 08:12:49','2025-12-02 08:12:49'),(3,'ABC Company','Manufacturing','cbvhd84784728','1234565768','abc@gmail.com','http://www.ABC.com','Tax1235','USD','GMT+8','yyyy-MM-dd','hh:mm a','Tamil','1234.56','2025-12-02 10:15:19','2025-12-02 10:15:19');
/*!40000 ALTER TABLE `application_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_items`
--

DROP TABLE IF EXISTS `bill_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` varchar(50) DEFAULT NULL,
  `product_id` varchar(50) DEFAULT NULL,
  `product_name` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `invoice_id` (`invoice_id`),
  CONSTRAINT `bill_items_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`invoice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_items`
--

LOCK TABLES `bill_items` WRITE;
/*!40000 ALTER TABLE `bill_items` DISABLE KEYS */;
INSERT INTO `bill_items` VALUES (3,'PAY-1765461562430','P007','Pen',10,35.00,350.00),(4,'PAY-1765463071345','P008','Notebook',2,150.00,300.00),(5,'PAY-1765629190401','P007','Pen',10,35.00,350.00),(6,'PAY-1765773316582','P009','Batteries',5,30.00,150.00),(7,'PAY-1765775078279','P011','mdnvj',6,10000.00,60000.00);
/*!40000 ALTER TABLE `bill_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `invoice_id` varchar(50) NOT NULL,
  `invoice_date` date DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(20) DEFAULT NULL,
  `amount_paid` decimal(10,2) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `payment_status` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES ('PAY-1765461562430','2025-12-11',350.00,350.00,'QR',200.00,150.00,'Partial','2025-12-11 14:05:37'),('PAY-1765463071345','2025-12-11',300.00,300.00,'Card',300.00,0.00,'Paid','2025-12-11 14:25:26'),('PAY-1765471643496','2025-12-11',175.00,175.00,'Cash ',175.00,0.00,'Paid','2025-12-11 16:47:56'),('PAY-1765502257838','2025-12-12',1125000.00,1125000.00,'QR',1125000.00,0.00,'Paid','2025-12-12 01:18:15'),('PAY-1765502776788','2025-12-12',350.00,350.00,'QR',350.00,0.00,'Paid','2025-12-12 01:26:51'),('PAY-1765502998944','2025-12-12',350.00,350.00,'QR',350.00,0.00,'Paid','2025-12-12 01:30:22'),('PAY-1765503371365','2025-12-12',1850000.00,1850000.00,'QR',1850000.00,0.00,'Paid','2025-12-12 01:36:34'),('PAY-1765504071500','2025-12-12',360000.00,360000.00,'Cash ',360000.00,0.00,'Paid','2025-12-12 01:48:17'),('PAY-1765504204161','2025-12-12',360000.00,360000.00,'QR',360000.00,0.00,'Paid','2025-12-12 01:50:29'),('PAY-1765504508960','2025-12-12',360000.00,360000.00,'QR',360000.00,0.00,'Paid','2025-12-12 01:55:28'),('PAY-1765504743221','2025-12-12',360000.00,360000.00,'Card',360000.00,0.00,'Paid','2025-12-12 01:59:26'),('PAY-1765505071364','2025-12-12',1750.00,1750.00,'Cash ',1750.00,0.00,'Paid','2025-12-12 02:04:54'),('PAY-1765505564645','2025-12-12',600000.00,600000.00,'QR',600000.00,0.00,'Paid','2025-12-12 02:13:03'),('PAY-1765505924798','2025-12-12',5700.00,5700.00,'Cash ',5700.00,0.00,'Paid','2025-12-12 02:20:44'),('PAY-1765506308521','2025-12-12',6650.00,6650.00,'Card',6650.00,0.00,'Paid','2025-12-12 02:25:31'),('PAY-1765507535364','2025-12-12',150000.00,150000.00,'QR',150000.00,0.00,'Paid','2025-12-12 02:45:55'),('PAY-1765507738352','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 02:49:15'),('PAY-1765508050319','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 02:54:25'),('PAY-1765508448934','2025-12-12',6650.00,6650.00,'Cash ',6650.00,0.00,'Paid','2025-12-12 03:01:08'),('PAY-1765508603415','2025-12-12',6650.00,6650.00,'Cash ',6650.00,0.00,'Paid','2025-12-12 03:03:43'),('PAY-1765509045521','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 03:11:00'),('PAY-1765509277605','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 03:15:00'),('PAY-1765509554644','2025-12-12',6650.00,6650.00,'Cash ',6650.00,0.00,'Paid','2025-12-12 03:19:40'),('PAY-1765509696221','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 03:21:50'),('PAY-1765510272045','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 03:31:33'),('PAY-1765510343091','2025-12-12',6650.00,6650.00,'QR',6650.00,0.00,'Paid','2025-12-12 03:32:44'),('PAY-1765629190401','2025-12-13',350.00,350.00,'Cash ',350.00,0.00,'Paid','2025-12-13 12:33:48'),('PAY-1765773316582','2025-12-15',150.00,150.00,'QR',150.00,0.00,'Paid','2025-12-15 04:35:41'),('PAY-1765775078279','2025-12-15',60000.00,60000.00,'Cash ',50000.00,10000.00,'Partial','2025-12-15 05:05:03'),('PAY-1765785920014','2025-12-15',150000.00,150000.00,'Cash ',150000.00,0.00,'Paid','2025-12-15 08:05:58'),('PAY-1765786014406','2025-12-15',150000.00,150000.00,'Cash ',150000.00,0.00,'Paid','2025-12-15 08:07:20'),('PAY-1767071581676','2025-12-30',250000.00,250000.00,'Cash ',250000.00,0.00,'Paid','2025-12-30 05:13:37');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `payment_id` varchar(20) NOT NULL,
  `product_id` varchar(20) NOT NULL,
  `quantity` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) NOT NULL,
  `payment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `transaction_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES ('PAY-1764522801811-4','P006',13,0.00,'QR','2025-11-30 22:44:05','TXN-PAY-1764522801811-4'),('PAY-1765441334324-0','P004',1,0.00,'Cash ','2025-12-11 13:53:37','TXN-PAY-1765441334324-0');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` varchar(10) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT '0',
  `unit_price` decimal(10,2) DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `qr_code_path` varchar(255) DEFAULT NULL,
  `date_added` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_by` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES ('P001','Laptop HP EliteBook','Electronics',10,185000.00,'TechZone Pvt Ltd','Shelf A1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Laptop_HP_EliteBook.png','2025-11-04 00:00:00','Admin'),('P002','Wireless Mouse','Accessories',60,2500.00,'TechZone Pvt Ltd','Shelf B2','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Wireless_Mouse.png','2025-11-04 00:00:00','Admin'),('P003','Office Chair','Furniture',20,18000.00,'Comfort Seating','Shelf D1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Office_Chair.png','2025-11-04 00:00:00','Admin'),('P004','CCTV Camera Set','Security',10,125000.00,'SafeVision Pvt Ltd','Shelf G1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\CCTV_Camera_Set.png','2025-11-04 00:00:00','Admin'),('P005','Router TP-Link AC750','Networking',20,8500.00,'NetConnect Ltd','Shelf F1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Router_TP-Link_AC750.png','2025-11-04 00:00:00','Admin'),('P006','hbjfsg','fhhjgf',110,15000.00,'ABC','Colombo123','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\hbjfsg.png','2025-11-30 00:00:00','Admin'),('P007','Pen','Stationery',180,35.00,'OfficeSupplier','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Pen.png','2025-12-03 00:00:00','Admin'),('P008','Notebook','Stationery',40,150.00,'OfficeSupplier','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Notebook_P008.png','2025-12-03 00:00:00','Admin'),('P009','Batteries','Consumables',15,30.00,'ABC Company','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Batteries_P009.png','2025-12-04 00:00:00','Admin'),('P010','Servers','IT Hardware',10,30000.00,'ABC Company','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Servers_P010.png','2025-12-12 00:00:00','Admin'),('P011','mdnvj','mndvbjf',4,10000.00,'lkj','A1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\mdnvj_P011.png','2025-12-12 00:00:00','Admin'),('P012','Laptop dell','eloctric',20,15000.00,'ABC','A1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Laptop_dell_P012.png','2025-12-15 13:35:02','Admin'),('P013','vchzgch','Stationaries',20,450.00,'Atlas','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\vchzgch_P013.png','2025-12-15 13:47:11','Admin'),('P014','Book','Stationaries',15,450.00,'mnb','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Book_P014.png','2025-12-15 14:09:19','Admin'),('P015','Eraser','Stationaries',10,25.00,'ABC','B1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Eraser_P015.png','2025-12-24 05:34:25','Admin'),('P016','Charger','Eloctronic',8,10000.00,'KLM','G1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Charger_P016.png','2025-12-24 05:41:27','Admin'),('P017','Headset','Accessories',15,11000.00,'jkl','A1','C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes\\Headset_P017.png','2025-12-24 06:10:49','Admin');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qr_users`
--

DROP TABLE IF EXISTS `qr_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qr_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fullname` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `secret` varchar(100) NOT NULL,
  `qr_image` longblob,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qr_users`
--

LOCK TABLES `qr_users` WRITE;
/*!40000 ALTER TABLE `qr_users` DISABLE KEYS */;
INSERT INTO `qr_users` VALUES (4,'admin','admin123','Admin User','Admin','1234',_binary 'âPNG\r\n\Z\n\0\0\0\rIHDR\0\0,\0\0,\0\0\0\0Q≥\ÿ\0\0dIDATx^\Ì\–\€q\√0DQv\∆\÷YR:Ppî¨\·í?3ôÖh>Ä†\ﬂ\ƒO[3\€0[\¬l	≥%Ãñ¯˚l4Eè[≠≥\≈\Îì5+WH|\ŸC\‚Ãö\’l0\ŒL7M8\‡-kˆ\»\Ê~\"≥/Ÿ°\‹\⁄fV≥≥\»\‚9@3kV≤¶P\ÈˆÕ¨Y≈Ædoc=\√l\À4Hç≥Se∂π\ÁÕ¨bÛ≠3¨˙tgô=0jâ\Â\"\—Õ¨bó\À2eÆ§Ÿñënçt¸∫<wÄŸûÒàz\ÊÚ:\Ë0+XJ£<ªÆÕ¨dCµPL4kCm,≥G¶$å˜¨\”\»\€l\À\‚•\'*g<˜NÉY\…\"ù´Q (\Ít1´«≠Å\Ÿf\∆\ÏÅ)\÷]ê!k\‡f;c&Qá\Z\Ái∂g\Ze¨\n	Ω\‘eV1Ω¥\«\r\“i\◊aV≤´§jßÆâ7)≥\∆˛i\–\'hˆ é\ÿf$òl\‚8«¨\∆˘)ô\Ì\Ÿ9N~ÄÀß2€≥\◊0[\¬l	≥%Ãñ¯\Ï¡ã¡?©o\›\Ì\0\0\0\0IENDÆB`Ç','2025-12-15 01:36:57'),(6,'virdha','virdha123','Virdha Musawwir','Admin','6026',_binary 'âPNG\r\n\Z\n\0\0\0\rIHDR\0\0,\0\0,\0\0\0\0Q≥\ÿ\0\0[IDATx^\Ì\–Yí!Pn\Ê´s§πAè%ô,N\ÁsjJ\Õ\"?Ú¡∏æ©ü±\'\«2\€\ l+≥≠Ã∂˙˚lV\‰&rdØ©Y«∞≈å\Õcy§f-ÀßD\\{˛îö›∞\Ï\√\\u2˚ñf˛ºfˆô≠πZ˘Ω¶f\r¨ò\ÔøJ\Õ:ˆ,¥/vevdß\ﬁWoäm\Âf=\√Iy6&˘:ôıùr\Ëg;\Â`…ô5LB)h\ﬁ\„lôùYÜlí≤É¢ºivf\"kã>À¨g\n\„Ea\≈eïY«òT\\D+6˚¿_•WÄ%\È\Ã\Œ,<*\Ê-X\\c√¨c\’H>±\\ú1°e÷∞\ƒ\À-˙≥ûâ&DûM\Ï8õµlUB¥&up1kü2õ-.†\Î\…\ÕZ\∆CJ±ïõıLœ©\‹◊É5ªaì¢|\Âf˜L0èëdTf[∏ÜSó\Õ\Œl∞Çè\ÃeI˝ÅŸô›ñ\ŸVf[ôme∂\’`øÜ§\·#zd\‡\0\0\0\0IENDÆB`Ç','2025-12-15 04:25:56'),(7,'ilma','ilma123','Ilma Musawwir','Staff','1759',_binary 'âPNG\r\n\Z\n\0\0\0\rIHDR\0\0,\0\0,\0\0\0\0Q≥\ÿ\0\0_IDATx^\Ì\–[í\√ DQÔå≠≥§ŸÅá€ícáB$üSS-l\Õ\·G\«˘M˝s≤,≥©Ã¶2õ\ l™ø\œ˙°j±cio©Y¡\ÿ2˜≤HæR≥íçVfÃëΩ∆ïömŸàb ìŸó\Ï∂y6€≥\«\Ã\’¯\ﬂR≥5;Tç\ﬁ>F¶fª´èév›æ\ l\…\"¢©]Qb≥%;ïAZ~©\Ãˆ©å[∂\»X\Õ6¨è\„h))ùe≥Y\≈$Ü\È˙∏\·\'ù\ÕJÜºDøõUIåæ∂\⁄A(≥%êi\r«Ü˛4[≤H∏\‘zu˙~h∂d\\∑XbÂùû†\ÕJ6\Œ=WL\Ã\“\Ãf#ßv\Íc2€±3˙⁄¥ú\\^=6´\ŸU\‹\Í∫i1+òZ9.Ø;/zo∂f:D8~-Ò\"\Ô\Ã\n\÷iÆ@\Ó≥\·¡\Õ>0R∂\ ≥=\”\·A¶≠Y\≈\'\◊9\'-fkv®h(ñ»¨`\Àl*≥©Ã¶2õ\Í?∞_\“\—¡?sæ\‚û\0\0\0\0IENDÆB`Ç','2025-12-15 04:33:41');
/*!40000 ALTER TABLE `qr_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_history`
--

DROP TABLE IF EXISTS `stock_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_history` (
  `history_id` int NOT NULL AUTO_INCREMENT,
  `product_id` varchar(10) DEFAULT NULL,
  `action_type` varchar(10) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `date_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `user` varchar(50) DEFAULT NULL,
  `remarks` text,
  `issued_to` varchar(100) DEFAULT NULL,
  `issued_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`history_id`),
  KEY `stock_history_ibfk_1` (`product_id`),
  CONSTRAINT `stock_history_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_history`
--

LOCK TABLES `stock_history` WRITE;
/*!40000 ALTER TABLE `stock_history` DISABLE KEYS */;
INSERT INTO `stock_history` VALUES (2,'P008','IN',50,'2025-12-03 16:26:41','Admin','Initial stock added',NULL,NULL),(3,'P009','IN',20,'2025-12-04 00:55:41','Admin','Initial stock added',NULL,NULL),(4,'P004','IN',5,'2025-12-06 23:56:21','Admin','Stock added via product form',NULL,NULL),(5,'P008','ISSUE',10,'2025-12-08 20:42:26','virdha','Issued 10 of Notebook','Production Floor','virdha'),(6,'P008','Sale',2,'2025-12-11 19:55:26','System','Sold via invoice PAY-1765463071345','Customer','System'),(7,'P010','IN',2,'2025-12-12 18:12:40','Admin','Initial stock added',NULL,NULL),(8,'P008','IN',2,'2025-12-12 18:42:36','Admin','Stock updated via product update',NULL,NULL),(9,'P011','IN',10,'2025-12-12 18:48:17','Admin','Initial stock added',NULL,NULL),(11,'P010','IN',2,'2025-12-12 19:48:20','Admin','Stock updated via product update',NULL,NULL),(12,'P004','IN',1,'2025-12-12 19:57:41','Admin','Stock updated via product update',NULL,NULL),(13,'P010','IN',1,'2025-12-12 20:06:19','Admin','Stock updated via product update',NULL,NULL),(14,'P006','IN',3,'2025-12-12 20:08:48','Admin','Stock updated via product update',NULL,NULL),(15,'P010','IN',1,'2025-12-12 20:17:50','Admin','Stock updated via product update',NULL,NULL),(16,'P007','Sale',10,'2025-12-13 18:03:48','System','Sold via invoice PAY-1765629190401','Customer','System'),(17,'P010','IN',2,'2025-12-13 20:16:20','Admin','Stock updated via product update',NULL,NULL),(18,'P010','IN',1,'2025-12-13 20:35:47','Admin','Stock updated via product update',NULL,NULL),(19,'P010','IN',1,'2025-12-13 20:37:07','Admin','Stock updated via product update',NULL,NULL),(20,'P010','OUT',2,'2025-12-13 20:38:50','Admin','Stock updated via product update',NULL,NULL),(21,'P009','Sale',5,'2025-12-15 10:05:41','System','Sold via invoice PAY-1765773316582','Customer','System'),(22,'P010','OUT',8,'2025-12-15 10:33:37','Admin','Stock updated via product update',NULL,NULL),(23,'P011','Sale',6,'2025-12-15 10:35:03','System','Sold via invoice PAY-1765775078279','Customer','System'),(24,'P010','IN',10,'2025-12-15 13:28:07','Admin','Stock updated via product update',NULL,NULL),(25,'P016','ISSUE',2,'2025-12-30 10:46:01','Virdha','Issued 2 of Charger','Production Floor','Virdha');
/*!40000 ALTER TABLE `stock_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Admin','Staff','Viewer') NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `contact_no` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Active',
  `can_generate_qr` tinyint(1) DEFAULT '0',
  `can_scan_qr` tinyint(1) DEFAULT '0',
  `can_manage_users` tinyint(1) DEFAULT '0',
  `can_manage_products` tinyint(1) DEFAULT '0',
  `can_view_scan_history` tinyint(1) DEFAULT '0',
  `can_view_user_logs` tinyint(1) DEFAULT '0',
  `can_access_settings` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'admin','admin123','Admin','Admin User','admin@mail.com','0771234567','Active',1,1,1,1,1,1,1),(8,'','','Admin','','','','Active',0,0,0,0,0,0,0),(9,'virdha','virdha123','Admin','Virdha Musawwir','','','Active',1,1,1,1,1,1,1),(11,'ilma','ilma123','Staff','Ilma Musawwir','ilma@gmail.com','76471561','Active',1,1,0,1,1,0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 17:25:38
