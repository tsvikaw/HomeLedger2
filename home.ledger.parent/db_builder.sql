-- MySQL dump 10.13  Distrib 8.0.16, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: hl
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `category` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('00406071-03a8-4db7-9432-73352a07a43c','2019-12-24 18:58:27','clothing',_binary '','clothing','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('016ccb94-c0d5-4443-9957-01eb5da02fd7','2019-12-24 18:58:27','home',_binary '','home','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('01efa786-16f3-4571-a384-4258ffc9c099','2019-12-24 18:58:27','pragnency',_binary '','pragnency','9c0f8030-f96d-4e9b-aa25-4eb3a4984f6e'),('04debcc4-48cc-4958-857e-eb6fd7e714d7','2019-12-24 18:58:26','meat',_binary '','meat','e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c'),('092b35a4-e8e4-4623-a5df-81246285b713','2019-12-24 18:58:26','gas',_binary '','gas','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('0ae48f85-ba34-4e87-9a44-795a714c03a8','2019-12-24 18:58:27','online supermarket',_binary '','online supermarket','4e0c9e15-47cb-417c-8e5c-349cc44f0ecb'),('0ed10f55-38d7-4f23-ba43-3525940b323e','2019-12-24 18:58:27','dental',_binary '','dental','9c0f8030-f96d-4e9b-aa25-4eb3a4984f6e'),('0f1a3d88-2e69-4855-84ed-1a6900aa63a3','2019-12-24 18:58:27','health special',_binary '','health special','9c0f8030-f96d-4e9b-aa25-4eb3a4984f6e'),('10ef762b-4f55-4122-a387-353c483d4fa3','2019-12-24 18:58:27','cleaning lady',_binary '','cleaning lady','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('19801854-2de6-4364-96d4-7dfb14aa7ce9','2019-12-24 18:58:26','luna',_binary '','luna','e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c'),('19c2249b-71bf-4456-8d6e-a37291a67d7d','2019-12-24 18:58:27','eating out',_binary '','eating out','bcaa6980-8d27-4ad3-b1b7-5943110b6eb1'),('1b3e8801-5322-4f14-8f18-28bdefe788bc','2019-12-24 18:58:27','buy a car',_binary '','buy a car','5b6f8218-798e-48f7-aad6-56e2e74b7078'),('1f27cdb7-4f55-4373-8227-2c4312518886','2019-12-24 18:58:27','kupat holim',_binary '','kupat holim','9c0f8030-f96d-4e9b-aa25-4eb3a4984f6e'),('21b0a755-a331-4b03-a738-3a4a4abb1bc0','2019-12-24 18:58:26','water',_binary '','water','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('21f63571-77de-46b7-89f4-12d2dddf090f','2019-12-24 18:58:27','gifts',_binary '','gifts','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('24c858cd-52fc-4f36-9833-ebb33c9e8002','2019-12-24 18:58:26','vegtables',_binary '','vegtables','e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c'),('32c8ddc4-277e-4260-86bb-ca2e53858bdc','2019-12-24 18:58:27','vacations',_binary '','vacations','5b6f8218-798e-48f7-aad6-56e2e74b7078'),('3b49717d-8cbc-45c9-b22e-5f52a79aae3f','2019-12-24 18:57:16','not sorted',_binary '','not sorted',NULL),('431a11ef-98e6-46da-a23a-9fb0b7e16551','2019-12-24 18:58:26','einat',_binary '','einat','5112cd86-ee4c-43a9-be41-e94999cf1207'),('435e8bed-f0b1-42e1-b523-29f5517be01e','2019-12-24 18:58:27','fuel',_binary '','fuel','78d07314-16ba-4cec-9347-9de3f51d599b'),('4e0c9e15-47cb-417c-8e5c-349cc44f0ecb','2019-12-24 18:58:26','supermarket',_binary '','supermarket','e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c'),('4e1d0e97-4f52-4267-b339-f1cf70c978be','2019-12-24 18:58:26','bills',_binary '','bills','5112cd86-ee4c-43a9-be41-e94999cf1207'),('5112cd86-ee4c-43a9-be41-e94999cf1207','2019-12-24 18:58:26','day to day',_binary '','day to day','517da810-c2e2-4a04-ae8f-282331f8263d'),('517da810-c2e2-4a04-ae8f-282331f8263d','2019-12-24 18:58:26','mine',_binary '','mine','f81b13b0-591d-4a4b-b4e9-6a47ee5403ae'),('5b392b68-cd25-4982-a3ff-7a7f1ebf9e88','2019-12-24 18:58:26','shopping',_binary '','shopping','5112cd86-ee4c-43a9-be41-e94999cf1207'),('5b6f8218-798e-48f7-aad6-56e2e74b7078','2019-12-24 18:58:26','big purchases',_binary '','big purchases','517da810-c2e2-4a04-ae8f-282331f8263d'),('5fe936b5-0f1a-4a1a-be01-4a32ea585259','2019-12-24 18:58:27','first born supplies',_binary '','first born supplies','d22c3ffa-bb8f-4066-ab12-cc513ab0cb95'),('6067e2e2-3e55-4073-a8b7-97d6f7ce77b9','2019-12-24 18:58:26','cellular communication',_binary '','cellular communication','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('652efddb-458f-4b19-a121-c02695861539','2019-12-24 18:58:27','kid cloths',_binary '','kid cloths','d22c3ffa-bb8f-4066-ab12-cc513ab0cb95'),('66fab6d0-094b-4ebf-ae30-0120c088cdf3','2019-12-24 18:58:27','house and ranovation',_binary '','house and ranovation','5b6f8218-798e-48f7-aad6-56e2e74b7078'),('78d07314-16ba-4cec-9347-9de3f51d599b','2019-12-24 18:58:26','transportation',_binary '','transportation','5112cd86-ee4c-43a9-be41-e94999cf1207'),('7a8ac0bb-9a5d-45aa-9c5c-375488187c2b','2019-12-24 18:58:26','cables and internet',_binary '','cables and internet','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('7e0ff487-d2c8-4ad3-ab21-f09448c8fa6b','2019-12-24 18:58:26','building vaad',_binary '','building vaad','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('85eb3b23-a5bf-4e96-9aa8-9e4e0df5e957','2019-12-24 18:58:26','commisions',_binary '','commisions','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('87aca237-1b9c-429f-8ace-bdbdf61c90d8','2019-12-24 18:58:26','power',_binary '','power','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('904a21eb-5017-43ec-abdb-414b0a26cade','2019-12-24 18:58:26','rent',_binary '','rent','5112cd86-ee4c-43a9-be41-e94999cf1207'),('965b728c-9137-42e1-883c-f67711b12855','2019-12-24 18:58:27','large supermarket',_binary '','large supermarket','4e0c9e15-47cb-417c-8e5c-349cc44f0ecb'),('9c0f8030-f96d-4e9b-aa25-4eb3a4984f6e','2019-12-24 18:58:26','health',_binary '','health','5112cd86-ee4c-43a9-be41-e94999cf1207'),('9ffe8810-3198-470a-9a95-eb9b0483f47a','2019-12-24 18:58:27','fun',_binary '','fun','bcaa6980-8d27-4ad3-b1b7-5943110b6eb1'),('aa18e2c9-8220-48b0-bfff-86d8d08bf05f','2019-12-24 18:58:26','dad assistance',_binary '','dad assistance','b774dd44-ad16-4e06-8f44-d6d8a6ed5268'),('b42c9189-34fe-4bc0-abc6-b8d24f4d6825','2019-12-24 18:58:27','celebrations',_binary '','celebrations','d22c3ffa-bb8f-4066-ab12-cc513ab0cb95'),('b774dd44-ad16-4e06-8f44-d6d8a6ed5268','2019-12-24 18:58:26','dad',_binary '','dad','f81b13b0-591d-4a4b-b4e9-6a47ee5403ae'),('b862265f-f81b-4133-b813-573c50ccfce3','2019-12-24 18:58:16','internal transactions',_binary '','internal transactions','3b49717d-8cbc-45c9-b22e-5f52a79aae3f'),('bcaa6980-8d27-4ad3-b1b7-5943110b6eb1','2019-12-24 18:58:26','free time',_binary '','free time','5112cd86-ee4c-43a9-be41-e94999cf1207'),('cb3b0968-a604-44bc-b80f-53559cf51214','2019-12-24 18:58:27','small supermarket',_binary '','small supermarket','4e0c9e15-47cb-417c-8e5c-349cc44f0ecb'),('ccca7277-6783-487f-83cb-fff84356443d','2019-12-24 18:58:27','paypal',_binary '','paypal','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('d22c3ffa-bb8f-4066-ab12-cc513ab0cb95','2019-12-24 18:58:26','kids',_binary '','kids','5112cd86-ee4c-43a9-be41-e94999cf1207'),('d29de9ef-4fe3-4eb7-9cb2-bc05128db6d8','2019-12-24 18:58:27','pharm',_binary '','pharm','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('d40ac052-63c9-4af2-b6bc-c6823a498761','2019-12-24 18:58:26','house insurance',_binary '','house insurance','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('d442d417-f1cc-43c6-82de-85a6e526883d','2019-12-24 18:58:27','technology and computer',_binary '','technology and computer','5b392b68-cd25-4982-a3ff-7a7f1ebf9e88'),('d6f51d8e-03c0-462f-8bd3-675fa716cb6f','2019-12-24 18:58:27','public transportation',_binary '','public transportation','78d07314-16ba-4cec-9347-9de3f51d599b'),('e154fd62-b97e-44ca-bfe0-f67e70f82aab','2019-12-24 18:58:26','city tax',_binary '','city tax','4e1d0e97-4f52-4267-b339-f1cf70c978be'),('e1e998fd-2b89-436b-91a0-14954f78969c','2019-12-24 18:58:26','cash miscellenious',_binary '','cash miscellenious','5112cd86-ee4c-43a9-be41-e94999cf1207'),('e9198223-cbd9-47ce-878d-7b387fab4504','2019-12-24 18:58:26','pastries',_binary '','pastries','e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c'),('e9d4ba8e-a1ec-4e58-b3af-fdda29a1d47c','2019-12-24 18:58:26','groceries',_binary '','groceries','5112cd86-ee4c-43a9-be41-e94999cf1207'),('ebbb5f3d-ad64-437e-b23e-b7e63696c86c','2019-12-24 18:58:27','car',_binary '','car','78d07314-16ba-4cec-9347-9de3f51d599b'),('f81b13b0-591d-4a4b-b4e9-6a47ee5403ae','2019-12-24 18:58:26','root',_binary '','root',NULL),('fcd5d24e-b64f-4401-b473-c47d7dbea261','2019-12-24 18:58:27','kindergarden',_binary '','kindergarden','d22c3ffa-bb8f-4066-ab12-cc513ab0cb95'),('fdf81859-185c-4346-bdf4-1d955d9f1b9b','2019-12-24 18:58:26','dad other',_binary '','dad other','b774dd44-ad16-4e06-8f44-d6d8a6ed5268');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `line`
--

DROP TABLE IF EXISTS `line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `line` (
  `id` varchar(255) NOT NULL,
  `account` varchar(255) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `category_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe8ob0b2tqtmimaupbptbkvel4` (`category_id`),
  CONSTRAINT `FKe8ob0b2tqtmimaupbptbkvel4` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `line`
--

LOCK TABLES `line` WRITE;
/*!40000 ALTER TABLE `line` DISABLE KEYS */;
INSERT INTO `line` VALUES ('0007da9d-6d97-492b-810c-d128d70f41e5','694-01133849-9194',249.06,'2015-05-20 02:30:00','דלק מנטה נהלל מנשית זבדה','435e8bed-f0b1-42e1-b523-29f5517be01e'),('000f9087-9016-4493-bd6b-a79c49b333ed','Credit Card Einat',89.8,'2018-05-17 02:30:00','תמנון ביג יוקנעם-גמא','00406071-03a8-4db7-9432-73352a07a43c'),('0012b8e7-9b78-4cc0-a4a1-1b8930397eee','Credit card Tsvika',27.71,'2018-05-02 02:30:00','מנהרות הכרמל-הו\"ק','ebbb5f3d-ad64-437e-b23e-b7e63696c86c'),('0013eedb-bcbb-4633-9ec1-b21076633e69','694-01133849-9194',271.62,'2015-01-14 03:30:00','פזYELLOW/ יוקנעם','435e8bed-f0b1-42e1-b523-29f5517be01e'),('00195227-5e84-4ac7-bc45-a7b31c1c99f6','Credit Card Einat',78.75,'2019-06-04 02:30:00','שופרסל דיל יוקנעם','965b728c-9137-42e1-883c-f67711b12855'),('0020af18-43c8-47fe-b755-845fe0ee211e','Bank transactions',2450,'2018-04-01 02:30:00','הוראת קבע י','fcd5d24e-b64f-4401-b473-c47d7dbea261'),('00223550-56af-4be4-90e8-c89ca5d4949c','Credit card Tsvika',20,'2019-05-26 02:30:00','רכבת ישראל- מרכזית המפרץ','d6f51d8e-03c0-462f-8bd3-675fa716cb6f'),('002c25e1-1af4-4167-9972-be9a651fa75b','Credit card Tsvika',592,'2019-09-27 02:30:00','מוסך זיוד','ebbb5f3d-ad64-437e-b23e-b7e63696c86c'),('003c667c-40f1-45ba-a601-9784d7aad550','cash',550,'2013-06-21 02:30:00','בוקסי בוקס','d442d417-f1cc-43c6-82de-85a6e526883d'),('00435ac2-9cf3-4d41-aa1c-495b95142e1d','694-01133849-9194',174,'2017-02-24 03:30:00','קליגולה- יוקנעם','00406071-03a8-4db7-9432-73352a07a43c'),('004b3e44-f392-4815-95b7-1419e96b837a','Credit Card Einat',40.52,'2019-09-17 02:30:00','גולף-יוקנעם','21f63571-77de-46b7-89f4-12d2dddf090f'),('0056c1e9-3abf-407d-aba2-3ea3d6d00c55','694-01133849',1600,'2016-09-01 02:30:00','שיק 12722','fcd5d24e-b64f-4401-b473-c47d7dbea261'),('00595a7f-4cc9-48e2-ab23-8995cf21cb6c','694-01133849-9194',109,'2013-01-10 03:30:00','L.D.H ק. קרית ביאל -','00406071-03a8-4db7-9432-73352a07a43c'),('005dde2b-9733-44ed-b69a-1f5c185b1691','694-01133849-4498',365,'2015-10-23 02:30:00','הכל לבנין','016ccb94-c0d5-4443-9957-01eb5da02fd7');
/*!40000 ALTER TABLE `line` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-28 12:38:14
