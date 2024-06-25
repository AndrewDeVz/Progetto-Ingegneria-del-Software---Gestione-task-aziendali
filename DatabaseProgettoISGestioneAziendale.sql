CREATE DATABASE  IF NOT EXISTS `azienda` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `azienda`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: azienda
-- ------------------------------------------------------
-- Server version	8.0.30

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
-- Table structure for table `dipendente`
--

DROP TABLE IF EXISTS `dipendente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dipendente` (
  `id` int NOT NULL,
  `password` varchar(100) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `cognome` varchar(100) DEFAULT NULL,
  `team` varchar(50) DEFAULT NULL,
  `livello` enum('JUNIOR','SENIOR','PRINCIPAL','DIRECTOR') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_team_dipendente_idx` (`team`),
  CONSTRAINT `fk_team_dipendente` FOREIGN KEY (`team`) REFERENCES `team` (`nominativo`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dipendente`
--

LOCK TABLES `dipendente` WRITE;
/*!40000 ALTER TABLE `dipendente` DISABLE KEYS */;
INSERT INTO `dipendente` VALUES (111111,'tesla','Elon','Musk',NULL,'SENIOR'),(121212,'apple','Steve','Jobs',NULL,'DIRECTOR'),(123456,'meta','Mark','Zuckerberg',NULL,'JUNIOR'),(123457,'linux','Linus','Torvalds',NULL,'SENIOR'),(123458,'qwerty','Steve','Wozniak',NULL,'DIRECTOR'),(575757,'microsoft','Bill','Gates',NULL,'PRINCIPAL');
/*!40000 ALTER TABLE `dipendente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `id` int NOT NULL,
  `password` varchar(45) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES (737373,'enigma','Alan','Turing');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `responsabileteam`
--

DROP TABLE IF EXISTS `responsabileteam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `responsabileteam` (
  `id` int NOT NULL,
  `password` varchar(45) NOT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `responsabileteam`
--

LOCK TABLES `responsabileteam` WRITE;
/*!40000 ALTER TABLE `responsabileteam` DISABLE KEYS */;
INSERT INTO `responsabileteam` VALUES (856371,'modello','John','Von Neumann');
/*!40000 ALTER TABLE `responsabileteam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `codice` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `priorita` enum('BASSA','MEDIA','ALTA') NOT NULL DEFAULT 'BASSA',
  `stato` enum('DA_ASSEGNARE','ASSEGNATO','IN_LAVORAZIONE','TERMINATO') NOT NULL DEFAULT 'DA_ASSEGNARE',
  `scadenza` date DEFAULT NULL,
  `descrizione` text,
  `dipendente_assegnato` int DEFAULT NULL,
  PRIMARY KEY (`codice`),
  KEY `fk_task_dipendente1_idx` (`dipendente_assegnato`),
  CONSTRAINT `fk_task_dipendente1` FOREIGN KEY (`dipendente_assegnato`) REFERENCES `dipendente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10009 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (10001,'pollo','BASSA','DA_ASSEGNARE','2024-06-19','',NULL);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `nominativo` varchar(50) NOT NULL,
  PRIMARY KEY (`nominativo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-17 18:57:41
