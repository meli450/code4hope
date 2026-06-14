-- Base de Datos destinada a RRHH de la empresa Code4Hope
CREATE DATABASE bd_code4hope_ipe;
USE bd_code4hope_ipe;

CREATE TABLE categoria (
    id_categoria        INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    nivel               VARCHAR(50),
    descripcion         TEXT
);

CREATE TABLE tipo_contrato (
    id_tipo_contrato    INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         TEXT,
    jornada             ENUM('COMPLETA','PARCIAL') NOT NULL,
    horas_semanales     DECIMAL(5,2)
);

-- Información personal

CREATE TABLE empleado (
    id_empleado         INT AUTO_INCREMENT PRIMARY KEY,
    codigo_empleado     VARCHAR(20) NOT NULL UNIQUE,
    dni_nie             VARCHAR(15) NOT NULL UNIQUE,
    nss                 VARCHAR(20) NOT NULL UNIQUE,
    nombre              VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(150) NOT NULL,
    fecha_nacimiento    DATE NOT NULL,
    telefono            VARCHAR(20) NOT NULL,
    telefono_alt        VARCHAR(20),
    direccion           VARCHAR(255) NOT NULL,
    email               VARCHAR(150) NOT NULL UNIQUE,
    puesto_trabajo      VARCHAR(100) NOT NULL,
    id_categoria        INT NOT NULL,
    id_tipo_contrato    INT NOT NULL,
    fecha_alta          DATE NOT NULL,
    fecha_baja          DATE,
    estado              ENUM('ACTIVO','BAJA_ENFERMEDAD','EXCEDENCIA','INACTIVO') NOT NULL
                        DEFAULT 'ACTIVO',
    observaciones       TEXT,
    CONSTRAINT fk_empleado_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),
    CONSTRAINT fk_empleado_tipo_contrato FOREIGN KEY (id_tipo_contrato) REFERENCES tipo_contrato(id_tipo_contrato)
);

CREATE TABLE historial_estado_empleado (
    id_historial        INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    estado_anterior     ENUM('ACTIVO','BAJA_ENFERMEDAD','EXCEDENCIA','INACTIVO'),
    estado_nuevo        ENUM('ACTIVO','BAJA_ENFERMEDAD','EXCEDENCIA','INACTIVO') NOT NULL,
    fecha_cambio        DATETIME NOT NULL,
    motivo              VARCHAR(255),
    usuario_responsable VARCHAR(100),
    CONSTRAINT fk_hist_estado_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE historial_datos_empleado (
    id_historial        INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    campo_modificado    VARCHAR(100) NOT NULL,
    valor_anterior      TEXT,
    valor_nuevo         TEXT,
    fecha_cambio        DATETIME NOT NULL,
    usuario_responsable VARCHAR(100),
    CONSTRAINT fk_hist_datos_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

-- Horarios, fichaje entrada/salida, actividades, desempeño

CREATE TABLE horario_tipo (
    id_horario_tipo     INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(50) NOT NULL,
    hora_entrada        TIME NOT NULL,
    hora_salida         TIME NOT NULL,
    horas_teoricas      DECIMAL(5,2) NOT NULL
);

CREATE TABLE asignacion_horario (
    id_asignacion       INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    id_horario_tipo     INT NOT NULL,
    fecha_inicio        DATE NOT NULL,
    fecha_fin           DATE,
    CONSTRAINT fk_asig_horario_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
    CONSTRAINT fk_asig_horario_tipo FOREIGN KEY (id_horario_tipo) REFERENCES horario_tipo(id_horario_tipo)
);

CREATE TABLE fichaje (
    id_fichaje          INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    fecha               DATE NOT NULL,
    hora_entrada        TIME NOT NULL,
    hora_salida         TIME NOT NULL,
    horas_trabajadas    DECIMAL(5,2) NOT NULL,
    tipo_hora           ENUM('ORDINARIA','EXTRAORDINARIA') NOT NULL,
    observaciones       TEXT,
    CONSTRAINT fk_fichaje_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE actividad_empleado (
    id_actividad        INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    fecha               DATE NOT NULL,
    hora_inicio         TIME NOT NULL,
    hora_fin            TIME NOT NULL,
    descripcion         VARCHAR(255) NOT NULL,
    tipo_actividad      VARCHAR(100),
    disponible          BOOLEAN NOT NULL DEFAULT TRUE,
    productividad_relativa DECIMAL(5,2),
    CONSTRAINT fk_actividad_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE evaluacion_desempeno (
    id_evaluacion       INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    periodo_inicio      DATE NOT NULL,
    periodo_fin         DATE NOT NULL,
    puntuacion_global   DECIMAL(4,2) NOT NULL,
    comentarios         TEXT,
    evaluador           VARCHAR(150),
    objetivos_cumplidos DECIMAL(5,2),
    CONSTRAINT fk_eval_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

-- Salarios, complementos/incentivos y nómina

CREATE TABLE salario_base (
    id_salario              INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado             INT NOT NULL,
    fecha_inicio            DATE NOT NULL,
    fecha_fin               DATE,
    salario_bruto_mensual   DECIMAL(10,2) NOT NULL,
    prorrata_pagas_extra    BOOLEAN NOT NULL DEFAULT TRUE,
    observaciones           TEXT,
    CONSTRAINT fk_salario_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);

CREATE TABLE complemento_salarial_tipo (
    id_complemento_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         TEXT,
    criterio            VARCHAR(100)   -- fijo, porcentaje, por_hora_extra, por_venta, etc.
);

CREATE TABLE complemento_salarial (
    id_complemento          INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado             INT NOT NULL,
    id_complemento_tipo     INT NOT NULL,
    periodo_inicio          DATE NOT NULL,
    periodo_fin             DATE,
    importe_fijo_mensual    DECIMAL(10,2),
    porcentaje_sobre_salario DECIMAL(5,2),
    observaciones           TEXT,
    CONSTRAINT fk_comp_emp FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
    CONSTRAINT fk_comp_tipo FOREIGN KEY (id_complemento_tipo) REFERENCES complemento_salarial_tipo(id_complemento_tipo)
);

CREATE TABLE nomina (
    id_nomina           INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado         INT NOT NULL,
    anio                INT NOT NULL,
    mes                 INT NOT NULL,
    salario_base        DECIMAL(10,2) NOT NULL,
    horas_extra         DECIMAL(6,2) DEFAULT 0,
    importe_horas_extra DECIMAL(10,2) DEFAULT 0,
    complementos        DECIMAL(10,2) DEFAULT 0,
    otros_devengos      DECIMAL(10,2) DEFAULT 0,
    deducciones         DECIMAL(10,2) DEFAULT 0,
    salario_bruto       DECIMAL(10,2) NOT NULL,
    salario_neto        DECIMAL(10,2) NOT NULL,
    fecha_generacion    DATE NOT NULL,
    CONSTRAINT fk_nomina_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado),
    CONSTRAINT uq_nomina_empleado_mes UNIQUE (id_empleado, anio, mes)
);
