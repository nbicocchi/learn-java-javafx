-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (x86_64)
--
-- Host: 127.0.0.1    Database: jdbc_schema
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `planes`
--

DROP TABLE IF EXISTS `planes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planes` (
  `uuid` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `length` double DEFAULT NULL,
  `wingspan` double DEFAULT NULL,
  `firstFlight` date DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planes`
--

LOCK TABLES `planes` WRITE;
/*!40000 ALTER TABLE `planes` DISABLE KEYS */;
INSERT INTO `planes` VALUES ('0081f3c8-68ae-4407-bb0b-54514f1100a4','Airbus A340-600',75.4,63.5,'2001-04-23','Airliner'),('01f7ea0a-e520-4f13-86a6-f1099c5e81a6','Boeing B-52',48.5,56.4,'1952-04-15','Bomber'),('033a8dfd-49fe-45fe-9e12-8b094d5abb28','Boeing 777-9',76.7,71.8,'2020-01-25','Airliner'),('0a78c3b8-fffa-4d4a-a000-6c1ede9ad0e2','Hughes H-4 Hercules',66.7,97.8,'1947-11-02','Flying boat'),('14414fbc-034a-49d2-91f4-1a5db404630f','Boeing 747-8',76.4,68.4,'2010-02-08','Airliner'),('2d75a13d-59c2-48d3-9773-e909941a946d','Caspian Seaonster',92,37.6,'1966-10-16','Ekranoplan'),('2e4af168-c3fb-47a2-b458-ee98112a810b','Antonov An-124',69.1,73.3,'1982-12-26','Transport'),('33741fd3-b09c-4e97-8802-1bd0f3c80ffa','Tupolev ANT-20',32.9,63,'1934-05-19','Transport'),('33e186f5-92a5-4c90-95a8-1e14d22f3ca9','Douglas XB-19',40.3,64.6,'1941-06-27','Bomber'),('385579a0-05eb-477c-b531-9a045bdac192','Junkers Ju 390',34.2,50.3,'1943-10-20','Bomber'),('38b99b66-d1a1-4308-a198-517215a37804','Antonov An-225riya',84,88.4,'1988-12-21','Transport'),('3bca66bb-1940-425a-a6fb-c1fe709d59d6','Lockheed C-5 Galaxy',75.3,67.9,'1968-06-30','Transport'),('488faba9-42ea-4d91-8da7-e9cce4f70476','Boeing 747',70.7,59.6,'1969-02-09','Airliner'),('58c363e8-f451-4405-b00e-3efc54067a95','Kalinin K-7',28,53,'1933-08-11','Transport'),('5b1ba32a-0d24-43ab-aaf6-457d33cc1d85','Martin JRMars',35.7,61,'1942-06-23','Flying boat'),('5dc26449-ca10-401f-a36d-cb590aaad0e6','Convair B-36',49.4,70.1,'1946-08-08','Bomber'),('66128a60-e512-416a-af83-38bd3f88d676','Messerschmitte 323',28.2,55.2,'1942-01-20','Transport'),('6ea2303d-1d64-4d62-a1af-c8141a107ef2','Convair XC-99',55.6,70.1,'1947-11-23','Transport'),('7aadc45b-320b-44af-b6d4-247bdb036a9c','Airbus Beluga',56.2,44.8,'1994-09-13','Outsize cargo'),('7bba0220-7f74-4421-ac6d-b2f0e0f24636','Boeing Dreamlifter',71.7,64.4,'2006-09-09','Outsize cargo'),('c47e38f6-4777-4674-a6dd-823591dcc966','Latécoère 631',43.5,57.4,'1942-11-04','Flying boat'),('cdb3f3e9-cab4-4e5a-bd37-b4b893df4725','Airbus A380',72.7,79.8,'2005-04-27','Airliner'),('d8aa3aed-08f0-4e04-8889-33123d8b298d','North American XB-70',56.4,32,'1964-09-21','Bomber'),('e0c5eaec-5493-49d3-a417-5ff01d772b91','﻿Dornier Do X',40,47.8,'1929-07-12','Flying boat'),('ee4fa8a2-46a5-4c65-ae13-d3b71a197e62','Antonov An-22',57.9,64.4,'1965-02-27','Transport'),('f46cec6d-9b21-4a35-bc91-9bf54adada19','Airbus Beluga XL',63.1,60.3,'2018-07-19','Outsize cargo');
/*!40000 ALTER TABLE `planes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-10 19:15:42
