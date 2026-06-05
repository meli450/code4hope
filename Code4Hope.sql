CREATE DATABASE  IF NOT EXISTS `code4hope` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `code4hope`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: code4hope
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `alimentos`
--

DROP TABLE IF EXISTS `alimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alimentos` (
  `id_producto` int NOT NULL,
  `calorias` int DEFAULT '0',
  `tipo_dieta` varchar(50) DEFAULT NULL,
  `necesita_refrigeracion` tinyint(1) NOT NULL DEFAULT '0',
  `temperatura_min` decimal(5,2) DEFAULT NULL,
  `temperatura_max` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  CONSTRAINT `fk_alimentos_productos` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alimentos`
--

LOCK TABLES `alimentos` WRITE;
/*!40000 ALTER TABLE `alimentos` DISABLE KEYS */;
INSERT INTO `alimentos` VALUES (1,350,'Sin restricciones',0,NULL,NULL);
/*!40000 ALTER TABLE `alimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `almacen`
--

DROP TABLE IF EXISTS `almacen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `almacen` (
  `id_almacen` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(36) NOT NULL,
  `ubicacion` varchar(200) DEFAULT NULL,
  `stock_minimo` int NOT NULL DEFAULT '0',
  `stock_maximo` int NOT NULL DEFAULT '0',
  `codigo_camara` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `uk_almacen_id` (`id_almacen`),
  KEY `fk_almacen_camara` (`codigo_camara`),
  CONSTRAINT `fk_almacen_camara` FOREIGN KEY (`codigo_camara`) REFERENCES `camara_refrigeracion` (`codigo`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `almacen`
--

LOCK TABLES `almacen` WRITE;
/*!40000 ALTER TABLE `almacen` DISABLE KEYS */;
INSERT INTO `almacen` VALUES (1,'ALM-ALI-001','Almacén Central Bamako - Zona A',50,2000,NULL),(2,'ALM-MED-001','Almacén Central Bamako - Zona B',20,500,'CAM-001-BAMAKO');
/*!40000 ALTER TABLE `almacen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `almacen_alimentos`
--

DROP TABLE IF EXISTS `almacen_alimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `almacen_alimentos` (
  `codigo` varchar(36) NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `fk_almacenAlimentos_almacen` FOREIGN KEY (`codigo`) REFERENCES `almacen` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `almacen_alimentos`
--

LOCK TABLES `almacen_alimentos` WRITE;
/*!40000 ALTER TABLE `almacen_alimentos` DISABLE KEYS */;
INSERT INTO `almacen_alimentos` VALUES ('ALM-ALI-001');
/*!40000 ALTER TABLE `almacen_alimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `almacen_medicamentos`
--

DROP TABLE IF EXISTS `almacen_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `almacen_medicamentos` (
  `codigo` varchar(36) NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `fk_almacenMedicamentos_almacen` FOREIGN KEY (`codigo`) REFERENCES `almacen` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `almacen_medicamentos`
--

LOCK TABLES `almacen_medicamentos` WRITE;
/*!40000 ALTER TABLE `almacen_medicamentos` DISABLE KEYS */;
INSERT INTO `almacen_medicamentos` VALUES ('ALM-MED-001');
/*!40000 ALTER TABLE `almacen_medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asignacion_lote`
--

DROP TABLE IF EXISTS `asignacion_lote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignacion_lote` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `id_lote` int NOT NULL,
  `id_patrulla` int NOT NULL,
  `cantidad_asignada` int NOT NULL DEFAULT '0',
  `fecha_asignacion` date NOT NULL,
  `estado` enum('PENDIENTE','EN_REPARTO','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_asignacion`),
  KEY `fk_asignacion_lote` (`id_lote`),
  KEY `fk_asignacion_patrulla` (`id_patrulla`),
  CONSTRAINT `fk_asignacion_lote` FOREIGN KEY (`id_lote`) REFERENCES `lote` (`id_lote`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_asignacion_patrulla` FOREIGN KEY (`id_patrulla`) REFERENCES `patrulla` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion_lote`
--

LOCK TABLES `asignacion_lote` WRITE;
/*!40000 ALTER TABLE `asignacion_lote` DISABLE KEYS */;
INSERT INTO `asignacion_lote` VALUES (1,1,1,200,'2026-06-04','EN_REPARTO'),(2,2,1,500,'2026-06-04','EN_REPARTO');
/*!40000 ALTER TABLE `asignacion_lote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `camara_refrigeracion`
--

DROP TABLE IF EXISTS `camara_refrigeracion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `camara_refrigeracion` (
  `codigo` varchar(36) NOT NULL,
  `capacidad` decimal(8,2) NOT NULL DEFAULT '0.00',
  `temperatura_minima` decimal(5,2) NOT NULL DEFAULT '-5.00',
  `temperatura_maxima` decimal(5,2) NOT NULL DEFAULT '8.00',
  `temperatura_actual` decimal(5,2) DEFAULT '4.00',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `camara_refrigeracion`
--

LOCK TABLES `camara_refrigeracion` WRITE;
/*!40000 ALTER TABLE `camara_refrigeracion` DISABLE KEYS */;
INSERT INTO `camara_refrigeracion` VALUES ('CAM-001-BAMAKO',500.00,2.00,8.00,4.50,1);
/*!40000 ALTER TABLE `camara_refrigeracion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encuesta`
--

DROP TABLE IF EXISTS `encuesta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `encuesta` (
  `cod` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `enlace` varchar(500) DEFAULT NULL,
  `informe` text,
  `codt` int NOT NULL,
  PRIMARY KEY (`cod`),
  KEY `fk_encuesta_taller` (`codt`),
  CONSTRAINT `fk_encuesta_taller` FOREIGN KEY (`codt`) REFERENCES `taller` (`cod`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encuesta`
--

LOCK TABLES `encuesta` WRITE;
/*!40000 ALTER TABLE `encuesta` DISABLE KEYS */;
INSERT INTO `encuesta` VALUES (1,'Encuesta de satisfacción - Apoyo Psicosocial','https://forms.google.com/d/code4hope-psico-2026',NULL,1);
/*!40000 ALTER TABLE `encuesta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipocomunicacion`
--

DROP TABLE IF EXISTS `equipocomunicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipocomunicacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estadoEquipo` enum('Operativo','Mantenimiento','Averiado','EnReparacion') NOT NULL DEFAULT 'Operativo',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipocomunicacion`
--

LOCK TABLES `equipocomunicacion` WRITE;
/*!40000 ALTER TABLE `equipocomunicacion` DISABLE KEYS */;
INSERT INTO `equipocomunicacion` VALUES (1,'Radio HF Alpha - Kenwood TS-480','Operativo'),(2,'Radio VHF Bravo - Motorola DM4601','Operativo');
/*!40000 ALTER TABLE `equipocomunicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipolog`
--

DROP TABLE IF EXISTS `equipolog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipolog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `equipo_id` int NOT NULL,
  `fechaHora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `entrada` text,
  PRIMARY KEY (`id`),
  KEY `fk_equipoLog_equipo` (`equipo_id`),
  CONSTRAINT `fk_equipoLog_equipo` FOREIGN KEY (`equipo_id`) REFERENCES `equipocomunicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipolog`
--

LOCK TABLES `equipolog` WRITE;
/*!40000 ALTER TABLE `equipolog` DISABLE KEYS */;
INSERT INTO `equipolog` VALUES (1,1,'2026-06-05 20:57:59','Equipo activado. Comprobación de frecuencias completada. Señal óptima.'),(2,1,'2026-06-05 20:57:59','Contacto establecido con base central. Patrulla Alpha en ruta.'),(3,2,'2026-06-05 20:57:59','Equipo activado y listo para misión Bamako-Gao.');
/*!40000 ALTER TABLE `equipolog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lote`
--

DROP TABLE IF EXISTS `lote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lote` (
  `id_lote` int NOT NULL AUTO_INCREMENT,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL DEFAULT '0',
  `fecha_caducidad` date DEFAULT NULL,
  `fecha_entrada` date NOT NULL,
  `estado` varchar(50) DEFAULT 'Disponible',
  PRIMARY KEY (`id_lote`),
  KEY `fk_lote_productos` (`id_producto`),
  CONSTRAINT `fk_lote_productos` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lote`
--

LOCK TABLES `lote` WRITE;
/*!40000 ALTER TABLE `lote` DISABLE KEYS */;
INSERT INTO `lote` VALUES (1,1,500,'2027-01-15','2026-01-15','Disponible'),(2,2,2000,'2028-03-01','2026-03-01','Disponible');
/*!40000 ALTER TABLE `lote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lote_alimentos`
--

DROP TABLE IF EXISTS `lote_alimentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lote_alimentos` (
  `id_lote` int NOT NULL,
  `codigo_almacen` varchar(36) DEFAULT NULL,
  `temperatura_control` decimal(5,2) DEFAULT NULL,
  `humedad_control` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id_lote`),
  KEY `fk_loteAlimentos_almacen` (`codigo_almacen`),
  CONSTRAINT `fk_loteAlimentos_almacen` FOREIGN KEY (`codigo_almacen`) REFERENCES `almacen_alimentos` (`codigo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_loteAlimentos_lote` FOREIGN KEY (`id_lote`) REFERENCES `lote` (`id_lote`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lote_alimentos`
--

LOCK TABLES `lote_alimentos` WRITE;
/*!40000 ALTER TABLE `lote_alimentos` DISABLE KEYS */;
INSERT INTO `lote_alimentos` VALUES (1,'ALM-ALI-001',22.00,55.00);
/*!40000 ALTER TABLE `lote_alimentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lote_medicamentos`
--

DROP TABLE IF EXISTS `lote_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lote_medicamentos` (
  `id_lote` int NOT NULL,
  `codigo_almacen` varchar(36) DEFAULT NULL,
  `numero_lote_fabricante` varchar(100) DEFAULT NULL,
  `condiciones_almacenamiento` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_lote`),
  KEY `fk_loteMedicamentos_almacen` (`codigo_almacen`),
  CONSTRAINT `fk_loteMedicamentos_almacen` FOREIGN KEY (`codigo_almacen`) REFERENCES `almacen_medicamentos` (`codigo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_loteMedicamentos_lote` FOREIGN KEY (`id_lote`) REFERENCES `lote` (`id_lote`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lote_medicamentos`
--

LOCK TABLES `lote_medicamentos` WRITE;
/*!40000 ALTER TABLE `lote_medicamentos` DISABLE KEYS */;
INSERT INTO `lote_medicamentos` VALUES (2,'ALM-MED-001','FAB-2026-PCT-001','Conservar entre 2°C y 8°C. Proteger de la luz.');
/*!40000 ALTER TABLE `lote_medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos`
--

DROP TABLE IF EXISTS `medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos` (
  `id_producto` int NOT NULL,
  `principio_activo` varchar(100) DEFAULT NULL,
  `dosis` varchar(100) DEFAULT NULL,
  `via_administracion` varchar(50) DEFAULT NULL,
  `necesita_receta` tinyint(1) NOT NULL DEFAULT '0',
  `temperatura_almacenamiento` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  CONSTRAINT `fk_medicamentos_productos` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos`
--

LOCK TABLES `medicamentos` WRITE;
/*!40000 ALTER TABLE `medicamentos` DISABLE KEYS */;
INSERT INTO `medicamentos` VALUES (2,'Paracetamol','500mg cada 8 horas','Oral',0,6.00);
/*!40000 ALTER TABLE `medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monitor_a`
--

DROP TABLE IF EXISTS `monitor_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monitor_a` (
  `nif` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(250) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`nif`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monitor_a`
--

LOCK TABLES `monitor_a` WRITE;
/*!40000 ALTER TABLE `monitor_a` DISABLE KEYS */;
INSERT INTO `monitor_a` VALUES ('FR-1978-5522','Jean-Paul','Dupont','+223 66 50 60 70','Barrio ACI 2000, Bamako',1),('ML-1990-0011','Amina','Traoré','+223 77 10 20 30','Calle Indépendance 14, Bamako',1);
/*!40000 ALTER TABLE `monitor_a` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paciente` (
  `id_paciente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `alergias` text,
  `historial_medico` text,
  PRIMARY KEY (`id_paciente`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paciente`
--

LOCK TABLES `paciente` WRITE;
/*!40000 ALTER TABLE `paciente` DISABLE KEYS */;
INSERT INTO `paciente` VALUES (1,'Fatima','Koné','1985-07-12','Ninguna conocida','Fiebre recurrente. Tratamiento con paracetamol.');
/*!40000 ALTER TABLE `paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participa`
--

DROP TABLE IF EXISTS `participa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participa` (
  `codt` int NOT NULL,
  `iduser` int NOT NULL,
  `fecha_ini` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`codt`,`iduser`),
  KEY `fk_participa_participante` (`iduser`),
  CONSTRAINT `fk_participa_participante` FOREIGN KEY (`iduser`) REFERENCES `participante` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_participa_taller` FOREIGN KEY (`codt`) REFERENCES `taller` (`cod`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participa`
--

LOCK TABLES `participa` WRITE;
/*!40000 ALTER TABLE `participa` DISABLE KEYS */;
INSERT INTO `participa` VALUES (1,1,'2026-06-02',NULL),(1,2,'2026-06-02',NULL),(2,3,'2026-06-03',NULL);
/*!40000 ALTER TABLE `participa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participante`
--

DROP TABLE IF EXISTS `participante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `genero` varchar(30) DEFAULT NULL,
  `edad` int DEFAULT NULL,
  `perfil` enum('MENOR_DE_EDAD','JUVENTUD','ADULTO','MUJER','DESEMPLEADO','DIVERSIDAD_FUNCIONAL','TODOS') NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participante`
--

LOCK TABLES `participante` WRITE;
/*!40000 ALTER TABLE `participante` DISABLE KEYS */;
INSERT INTO `participante` VALUES (1,'Mariama','Diallo','Femenino',34,'MUJER',1),(2,'Aissata','Barry','Femenino',28,'MUJER',1),(3,'Kofi','Mensah','Masculino',45,'ADULTO',1);
/*!40000 ALTER TABLE `participante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patrulla`
--

DROP TABLE IF EXISTS `patrulla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patrulla` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `estado` enum('Inactiva','Preparada','EnMision','Completada','Abortada') NOT NULL,
  `vehiculo_id` int DEFAULT NULL,
  `ruta_id` int DEFAULT NULL,
  `equipoComunicacion_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `fk_patrulla_veh` (`vehiculo_id`),
  KEY `fk_patrulla_ruta` (`ruta_id`),
  KEY `fk_patrulla_equipo` (`equipoComunicacion_id`),
  CONSTRAINT `fk_patrulla_equipo` FOREIGN KEY (`equipoComunicacion_id`) REFERENCES `equipocomunicacion` (`id`),
  CONSTRAINT `fk_patrulla_ruta` FOREIGN KEY (`ruta_id`) REFERENCES `ruta` (`id`),
  CONSTRAINT `fk_patrulla_veh` FOREIGN KEY (`vehiculo_id`) REFERENCES `vehiculo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patrulla`
--

LOCK TABLES `patrulla` WRITE;
/*!40000 ALTER TABLE `patrulla` DISABLE KEYS */;
INSERT INTO `patrulla` VALUES (1,'ALPHA-2026-01','Patrulla de distribución de suministros Norte - Tombuctú','EnMision',1,1,1),(2,'BRAVO-2026-01','Patrulla de distribución de suministros Este - Gao','Preparada',2,2,2);
/*!40000 ALTER TABLE `patrulla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patrullarecurso`
--

DROP TABLE IF EXISTS `patrullarecurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patrullarecurso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patrulla_id` int NOT NULL,
  `recurso` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_patRecurso_patrulla` (`patrulla_id`),
  CONSTRAINT `fk_patRecurso_patrulla` FOREIGN KEY (`patrulla_id`) REFERENCES `patrulla` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patrullarecurso`
--

LOCK TABLES `patrullarecurso` WRITE;
/*!40000 ALTER TABLE `patrullarecurso` DISABLE KEYS */;
INSERT INTO `patrullarecurso` VALUES (1,1,'Kit primeros auxilios avanzado'),(2,1,'Bidones agua potable 20L x10'),(3,2,'Generador portatil 2000W');
/*!40000 ALTER TABLE `patrullarecurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescripcion`
--

DROP TABLE IF EXISTS `prescripcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescripcion` (
  `id_prescripcion` int NOT NULL AUTO_INCREMENT,
  `id_paciente` int NOT NULL,
  `id_producto` int NOT NULL,
  `dosis` varchar(100) DEFAULT NULL,
  `frecuencia` varchar(100) DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Activa',
  PRIMARY KEY (`id_prescripcion`),
  KEY `fk_prescripcion_paciente` (`id_paciente`),
  KEY `fk_prescripcion_productos` (`id_producto`),
  CONSTRAINT `fk_prescripcion_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_prescripcion_productos` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescripcion`
--

LOCK TABLES `prescripcion` WRITE;
/*!40000 ALTER TABLE `prescripcion` DISABLE KEYS */;
INSERT INTO `prescripcion` VALUES (1,1,2,'500mg','Cada 8 horas',7,'2026-06-01','2026-06-08','Activa');
/*!40000 ALTER TABLE `prescripcion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `unidad_medida` varchar(20) DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `proveedor` varchar(100) DEFAULT NULL,
  `tipo` enum('ALIMENTO','MEDICAMENTO') NOT NULL,
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'Arroz Basmati','Arroz de grano largo para distribución humanitaria','kg',0.80,'Cereales',NULL,'ALIMENTO'),(2,'Paracetamol 500mg','Analgésico y antipirético de uso general','comprimido',0.05,'Analgésicos',NULL,'MEDICAMENTO');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puntoruta`
--

DROP TABLE IF EXISTS `puntoruta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puntoruta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ruta_id` int NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `descripcion` text,
  `tipo` enum('Inicio','Control','Incidencia','Fin','Gasolinera') NOT NULL DEFAULT 'Control',
  `latitud` decimal(9,6) DEFAULT NULL,
  `longitud` decimal(9,6) DEFAULT NULL,
  `estado` enum('Pendiente','Alcanzado','Omitido') NOT NULL DEFAULT 'Pendiente',
  `horaEstimada` time DEFAULT NULL,
  `horaRealLlegada` time DEFAULT NULL,
  `notasIncidencia` text,
  `esGasolinera` tinyint(1) NOT NULL DEFAULT '0',
  `posicion` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_puntoRuta_ruta` (`ruta_id`),
  CONSTRAINT `fk_puntoRuta_ruta` FOREIGN KEY (`ruta_id`) REFERENCES `ruta` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puntoruta`
--

LOCK TABLES `puntoruta` WRITE;
/*!40000 ALTER TABLE `puntoruta` DISABLE KEYS */;
INSERT INTO `puntoruta` VALUES (1,1,'Bamako - Base Central','Punto de salida, base de operaciones','Inicio',12.639200,-8.002900,'Alcanzado','06:00:00','06:00:00',NULL,0,1),(2,1,'Ségou - Control Norte','Punto de control y repostaje en Ségou','Gasolinera',13.431700,-6.267300,'Alcanzado','09:00:00','09:25:00','Leve retraso por control policial',1,2),(3,1,'Tombuctú - Zona Distribución','Destino final, punto de entrega de suministros','Fin',16.773500,-3.007400,'Pendiente','20:00:00',NULL,NULL,0,3),(4,2,'Bamako - Base Central','Punto de salida','Inicio',12.639200,-8.002900,'Pendiente','07:00:00',NULL,NULL,0,1),(5,2,'Mopti - Control Centro','Punto de control intermedio','Control',14.494300,-4.196600,'Pendiente','12:00:00',NULL,NULL,0,2),(6,2,'Gao - Centro Humanitario','Destino final','Fin',16.266600,-0.044400,'Pendiente','22:00:00',NULL,NULL,0,3);
/*!40000 ALTER TABLE `puntoruta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso`
--

DROP TABLE IF EXISTS `recurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recurso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(100) DEFAULT NULL,
  `disponibilidad` enum('DISPONIBLE','EN_USO','PENDIENTE_DEVOLUCION','AGOTADO') NOT NULL,
  `cantidad` int DEFAULT '0',
  `es_fungible` tinyint(1) NOT NULL DEFAULT '0',
  `idp` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_recurso_patrulla` (`idp`),
  CONSTRAINT `fk_recurso_patrulla` FOREIGN KEY (`idp`) REFERENCES `patrulla` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso`
--

LOCK TABLES `recurso` WRITE;
/*!40000 ALTER TABLE `recurso` DISABLE KEYS */;
INSERT INTO `recurso` VALUES (1,'Cuadernos y bolígrafos','EN_USO',50,1,2),(2,'Proyector portátil','EN_USO',1,0,2);
/*!40000 ALTER TABLE `recurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrocomunicacion`
--

DROP TABLE IF EXISTS `registrocomunicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registrocomunicacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `equipo_id` int NOT NULL,
  `hora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo` enum('Texto','Alerta','Confirmacion','Emergencia') NOT NULL DEFAULT 'Texto',
  `mensaje` text,
  `emisor` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_regCom_equipo` (`equipo_id`),
  CONSTRAINT `fk_regCom_equipo` FOREIGN KEY (`equipo_id`) REFERENCES `equipocomunicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrocomunicacion`
--

LOCK TABLES `registrocomunicacion` WRITE;
/*!40000 ALTER TABLE `registrocomunicacion` DISABLE KEYS */;
INSERT INTO `registrocomunicacion` VALUES (1,1,'2026-06-05 20:57:59','Confirmacion','Patrulla Alpha saliendo de base. Todo en orden.','Jefe Carlos Mendoza'),(2,1,'2026-06-05 20:57:59','Alerta','Control policial en Ségou. Retraso estimado de 25 minutos.','Jefe Carlos Mendoza'),(3,1,'2026-06-05 20:57:59','Confirmacion','Control superado. Continuamos hacia Tombuctú.','Base Central Bamako');
/*!40000 ALTER TABLE `registrocomunicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ruta`
--

DROP TABLE IF EXISTS `ruta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ruta` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estado` enum('Pendiente','EnCurso','Completada','Abortada') NOT NULL DEFAULT 'Pendiente',
  `fechaMision` date DEFAULT NULL,
  `horaInicio` time DEFAULT NULL,
  `horaFin` time DEFAULT NULL,
  `indicePuntoActual` int NOT NULL DEFAULT '0',
  `gradoPeligrosidad` varchar(50) DEFAULT NULL,
  `numKm` decimal(8,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ruta`
--

LOCK TABLES `ruta` WRITE;
/*!40000 ALTER TABLE `ruta` DISABLE KEYS */;
INSERT INTO `ruta` VALUES (1,'Bamako - Tombuctú','EnCurso','2026-06-05','06:00:00',NULL,2,'Alto',1020.50),(2,'Bamako - Gao','Pendiente','2026-06-07','07:00:00',NULL,0,'Medio',1270.00);
/*!40000 ALTER TABLE `ruta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taller`
--

DROP TABLE IF EXISTS `taller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taller` (
  `cod` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(50) NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `perfil_dest` enum('MENOR_DE_EDAD','JUVENTUD','ADULTO','MUJER','DESEMPLEADO','DIVERSIDAD_FUNCIONAL','TODOS') NOT NULL,
  `etiqueta` varchar(100) DEFAULT NULL,
  `espacio` varchar(50) DEFAULT NULL,
  `aforo_maximo` int DEFAULT '0',
  `nif` varchar(20) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_cancelacion` date DEFAULT NULL,
  `incidencia` varchar(250) DEFAULT NULL,
  `estado` enum('ACTIVO','CANCELADO','FINALIZADO') NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`cod`),
  KEY `fk_taller_monitor` (`nif`),
  CONSTRAINT `fk_taller_monitor` FOREIGN KEY (`nif`) REFERENCES `monitor_a` (`nif`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taller`
--

LOCK TABLES `taller` WRITE;
/*!40000 ALTER TABLE `taller` DISABLE KEYS */;
INSERT INTO `taller` VALUES (1,'Apoyo Psicosocial para Mujeres','Sesiones de apoyo emocional y resiliencia para mujeres afectadas por el conflicto','MUJER','Psicología','Sala A - Centro Comunitario Bamako',20,'ML-1990-0011','2026-06-02','2026-06-30',NULL,NULL,'ACTIVO'),(2,'Alfabetización Básica para Adultos','Curso de lectura y escritura básica en francés y bambara','ADULTO','Educación','Sala B - Centro Comunitario Bamako',25,'FR-1978-5522','2026-06-03','2026-07-15',NULL,NULL,'ACTIVO');
/*!40000 ALTER TABLE `taller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tiene`
--

DROP TABLE IF EXISTS `tiene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tiene` (
  `codt` int NOT NULL,
  `idr` int NOT NULL,
  `fecha_ini` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`codt`,`idr`),
  KEY `fk_tiene_recurso` (`idr`),
  CONSTRAINT `fk_tiene_recurso` FOREIGN KEY (`idr`) REFERENCES `recurso` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tiene_taller` FOREIGN KEY (`codt`) REFERENCES `taller` (`cod`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tiene`
--

LOCK TABLES `tiene` WRITE;
/*!40000 ALTER TABLE `tiene` DISABLE KEYS */;
INSERT INTO `tiene` VALUES (1,1,'2026-06-02',NULL),(2,2,'2026-06-03',NULL);
/*!40000 ALTER TABLE `tiene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tripulante`
--

DROP TABLE IF EXISTS `tripulante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tripulante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nif` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `telefonoContacto` varchar(20) DEFAULT NULL,
  `rol` enum('Conductor','Agente','Jefe','Soporte') NOT NULL,
  `estadoOperativo` enum('Activo','DeBaja','Disponible','Asignado') NOT NULL,
  `patrulla_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nif` (`nif`),
  KEY `fk_tripulante_patrulla` (`patrulla_id`),
  CONSTRAINT `fk_tripulante_patrulla` FOREIGN KEY (`patrulla_id`) REFERENCES `patrulla` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tripulante`
--

LOCK TABLES `tripulante` WRITE;
/*!40000 ALTER TABLE `tripulante` DISABLE KEYS */;
INSERT INTO `tripulante` VALUES (1,'ES-78432109A','Carlos','Mendoza','+34 612 345 678','Jefe','Asignado',1),(2,'ML-1990-0055','Ana','García','+223 70 11 22 33','Conductor','Asignado',1),(3,'ML-1988-1123','Mohamed','Diallo','+223 65 44 55 66','Agente','Asignado',1),(4,'FR-1985-7741','Lucia','Fernández','+33 6 77 88 99 00','Jefe','Asignado',2),(5,'ML-1993-3310','Ibrahim','Coulibaly','+223 76 33 22 11','Conductor','Asignado',2);
/*!40000 ALTER TABLE `tripulante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `tipo` enum('Coche','Moto','Furgoneta','Camion') NOT NULL DEFAULT 'Furgoneta',
  `refrigerado` tinyint(1) NOT NULL DEFAULT '0',
  `matricula` varchar(20) NOT NULL,
  `disponible` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`),
  UNIQUE KEY `matricula` (`matricula`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculo`
--

LOCK TABLES `vehiculo` WRITE;
/*!40000 ALTER TABLE `vehiculo` DISABLE KEYS */;
INSERT INTO `vehiculo` VALUES (1,'VEH-001-ALPHA','Furgoneta',1,'BKO-1142-A',0),(2,'VEH-002-BRAVO','Camion',0,'BKO-0873-B',0),(3,'VEH-003-RESERVA','Coche',0,'BKO-0021-C',1);
/*!40000 ALTER TABLE `vehiculo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'code4hope'
--

--
-- Dumping routines for database 'code4hope'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-05 21:01:34
