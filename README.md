# Sistema de Gestión de Ayuda Humanitaria (SGAH)
### Proyecto Intermodular - 1º DAW

---

## Sobre la Empresa: Code4Hope

**Code4Hope** es un equipo de desarrollo de software comprometido con la creación de soluciones tecnológicas robustas y de alto impacto social. Nuestro objetivo en este proyecto es transformar las líneas de código en herramientas eficientes, seguras y fiables para optimizar la ayuda humanitaria en zonas de conflicto.

### Equipo de Desarrollo
* **Melisa Vafaeva** - Desarrolladora Software
* **Jose Manuel Morato** - Desarrollador Software
* **Marc Nacher** - Desarrollador Software
* **Julio Linares** - Desarrollador Software

**Tutor del Proyecto:** Pascual Queralt  
**Centro Educativo:** IES Font de Sant Lluís

---

## Escenario del Proyecto

El sistema nace de la necesidad de una organización internacional dedicada a la ayuda humanitaria en zonas de conflicto, la cual requiere un software integral para gestionar y planificar sus patrullas semanales. Estas patrullas son esenciales para el transporte de suministros vitales (comida y medicinas), la organización de talleres educativos y la monitorización sobre el terreno.

---

## Arquitectura de los Subsistemas

El software se compone de 4 módulos principales interconectados de forma transversal con servicios de mapas y geolocalización:

### 1. Gestión de Patrullas
* **Tripulación y Vehículos:** Registro exhaustivo de personal (civil o uniformado) y control de flotas (vehículos blindados, refrigerados, capacidad de combustible).
* **Rutas e Incidencias:** Planificación de trayectos con puntos intermedios y cálculo de necesidad de repostaje, permitiendo el control de estados (Planificada, En curso, Hecha, Cancelada) junto a reportes de incidencias.
* **Seguridad:** Confirmación horaria de estado y geolocalización activa en tiempo real.

### 2. Gestión de Alimentos
* **Inventario y Caducidad:** Registro detallado por tipo de grano, lote, fecha de vencimiento y temperatura requerida.
* **Trazabilidad:** Control de stock con alertas críticas y seguimiento desde el donante hasta el beneficiario final en los puntos de distribución.

### 3. Gestión de Medicamentos
* **Stock Sanitario:** Control estricto de medicamentos, principios activos, condiciones de conservación y requerimientos de receta médica.
* **Pacientes y Recetas:** Vinculación de las prescripciones médicas con las entregas seguras realizadas mediante las patrullas.

### 4. Gestión de Talleres de Formación
* **Catálogo Educativo:** Planificación de talleres (alfabetización, apoyo psicosocial, etc.) dirigidos a colectivos vulnerables.
* **Logística y Evaluación:** Control de aforo, asignación de materiales didácticos a las patrullas y medición del impacto social mediante encuestas.

---

## Stack Tecnológico

El proyecto se ha diseñado siguiendo un desarrollo iterativo e incremental (metodologías ágiles) bajo una **arquitectura en 3 capas** (Presentación, Lógica de Negocio y Datos):

* **Backend:** Java con Spring Boot (APIs REST para la comunicación entre módulos).
* **Frontend:** HTML5, CSS3 y JavaScript.
* **Base de Datos:** PostgreSQL o MySQL (Modelo relacional normalizado con transacciones ACID).
* **Comunicaciones:** HTTPS para garantizar transferencias seguras e integración con Google Maps API / OpenStreetMap.
* **Control de Versiones y Despliegue:** Git y entorno empaquetado mediante Docker.

---

## Metodología y Ciclo de Vida

Para asegurar la calidad del código, la usabilidad y la fiabilidad del sistema en entornos reales, el equipo divide el trabajo siguiendo las fases del ciclo de vida del software:

1. **Análisis:** Redacción de *User Stories* orientadas al Product Owner.
2. **Diseño:** Modelado de datos y arquitectura de servicios.
3. **Implementación:** Desarrollo estructurado dividido en Sprints.
4. **Pruebas y Despliegue:** Integración continua en entornos diferenciados (Desarrollo, Pruebas, Producción).

---
*Financiado por el Plan de Recuperación, Transformación y Resiliencia - NextGenerationEU.*
