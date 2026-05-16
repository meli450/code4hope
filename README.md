# Sistema de Gestión de Ayuda Humanitaria (SGAH)
### Proyecto Intermodular - 1º DAW

---

## Sobre la Empresa: Code4Hope

**Code4Hope** es un equipo de desarrollo de software comprometido con la creación de soluciones tecnológicas robustas y de alto impacto social. [cite_start]Nuestro objetivo en este proyecto es transformar las líneas de código en herramientas eficientes, seguras y fiables para optimizar la ayuda humanitaria en zonas de conflicto[cite: 9].

### Equipo de Desarrollo
* **Melisa Vafaeva** - Desarrolladora Software
* **Jose Manuel Morato** - Desarrolladora Software
* **Marc Nacher** - Desarrolladora Software
* **Julio Linares** - Desarrolladora Software

**Tutor del Proyecto:** Pascual Queralt
[cite_start]**Centro Educativo:** IES Font de Sant Lluís [cite: 6]

---

## Escenario del Proyecto

[cite_start]El sistema nace de la necesidad de una organización internacional dedicada a la ayuda humanitaria en zonas de conflicto, la cual requiere un software integral para gestionar y planificar sus patrullas semanales[cite: 9]. [cite_start]Estas patrullas son esenciales para el transporte de suministros vitales (comida y medicinas), la organización de talleres educativos y la monitorización sobre el terreno[cite: 10].

---

## Arquitectura de los Subsistemas

[cite_start]El software se compone de 4 módulos principales interconectados de forma transversal con servicios de mapas y geolocalización[cite: 124, 125]:

### 1. Gestión de Patrullas
* [cite_start]**Tripulación y Vehículos:** Registro exhaustivo de personal (civil o uniformado) [cite: 15, 16] [cite_start]y control de flotas (vehículos blindados, refrigerados, capacidad de combustible)[cite: 17].
* [cite_start]**Rutas e Incidencias:** Planificación de trayectos con puntos intermedios y cálculo de necesidad de repostaje [cite: 18, 19][cite_start], permitiendo el control de estados (Planificada, En curso, Hecha, Cancelada) [cite: 29] [cite_start]junto a reportes de incidencias[cite: 34].
* [cite_start]**Seguridad:** Confirmación horaria de estado y geolocalización activa en tiempo real[cite: 32, 33].

### 2. Gestión de Alimentos
* [cite_start]**Inventario y Caducidad:** Registro detallado por tipo de grano, lote, fecha de vencimiento y temperatura requerida[cite: 40].
* [cite_start]**Trazabilidad:** Control de stock con alertas críticas y seguimiento desde el donante hasta el beneficiario final en los puntos de distribución[cite: 41, 42].

### 3. Gestión de Medicamentos
* [cite_start]**Stock Sanitario:** Control estricto de medicamentos, principios activos, condiciones de conservación y requerimientos de receta médica[cite: 44, 46].
* [cite_start]**Pacientes y Recetas:** Vinculación de las prescripciones médicas con las entregas seguras realizadas mediante las patrullas[cite: 58, 59].

### 4. Gestión de Talleres de Formación
* [cite_start]**Catálogo Educativo:** Planificación de talleres (alfabetización, apoyo psicosocial, etc.) dirigidos a colectivos vulnerables[cite: 67, 69].
* [cite_start]**Logística y Evaluación:** Control de aforo, asignación de materiales didácticos a las patrullas [cite: 73, 76] [cite_start]y medición del impacto social mediante encuestas[cite: 79, 81].

---

## Stack Tecnológico

[cite_start]El proyecto se ha diseñado siguiendo un desarrollo iterativo e incremental (metodologías ágiles) [cite: 144] [cite_start]bajo una **arquitectura en 3 capas** (Presentación, Lógica de Negocio y Datos)[cite: 128]:

* [cite_start]**Backend:** Java con Spring Boot [cite: 129] [cite_start](APIs REST para la comunicación entre módulos [cite: 132]).
* [cite_start]**Frontend:** HTML5, CSS3 y JavaScript[cite: 130].
* [cite_start]**Base de Datos:** PostgreSQL o MySQL [cite: 131] [cite_start](Modelo relacional normalizado con transacciones ACID [cite: 134, 135]).
* [cite_start]**Comunicaciones:** HTTPS para garantizar transferencias seguras [cite: 137] [cite_start]e integración con Google Maps API / OpenStreetMap[cite: 138].
* [cite_start]**Control de Versiones y Despliegue:** Git [cite: 155] [cite_start]y entorno empaquetado mediante Docker[cite: 142].

---

## Metodología y Ciclo de Vida

[cite_start]Para asegurar la calidad del código, la usabilidad y la fiabilidad del sistema en entornos reales [cite: 162][cite_start], el equipo divide el trabajo siguiendo las fases del ciclo de vida del software[cite: 146]:
1. [cite_start]**Análisis:** Redacción de *User Stories* orientadas al Product Owner[cite: 154, 160].
2. [cite_start]**Diseño:** Modelado de datos y arquitectura de servicios[cite: 135, 146].
3. [cite_start]**Implementación:** Desarrollo estructurado dividido en Sprints[cite: 145, 146].
4. [cite_start]**Pruebas y Despliegue:** Integración continua en entornos diferenciados (Desarrollo, Pruebas, Producción)[cite: 141, 146].

---
[cite_start]Financiado por el Plan de Recuperación, Transformación y Resiliencia - NextGenerationEU. [cite: 1, 4]