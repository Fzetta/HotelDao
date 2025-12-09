# 🏨 Sistema de Gestión Hotelera

Sistema de gestión integral para hoteles desarrollado en Java con arquitectura DAO y patrón Singleton para gestión de conexiones a base de datos PostgreSQL. Incluye interfaz gráfica de usuario (GUI) y operaciones CRUD completas para todas las entidades del sistema.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración de la Base de Datos](#configuración-de-la-base-de-datos)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Uso](#uso)
- [Módulos y Funcionalidades](#módulos-y-funcionalidades)
- [Arquitectura](#arquitectura)
- [Diagrama de Clases](#diagrama-de-clases)
- [Contribuir](#contribuir)
- [Licencia](#licencia)
- [Autores](#autores)

---

## ✨ Características

- ✅ **Gestión de Personas y Clientes**: Registro completo de información personal y contacto
- ✅ **Administración de Habitaciones**: Control de disponibilidad, categorías y precios
- ✅ **Sistema de Reservas**: Gestión de reservas con fechas y tiempos de cancelación
- ✅ **Servicios Adicionales**: Catálogo de servicios del hotel con costos
- ✅ **Gestión de Empleados**: Control de personal y áreas de trabajo
- ✅ **Consumos Adicionales**: Registro de servicios consumidos por cliente
- ✅ **Interfaz Gráfica**: GUI intuitiva con pestañas para cada módulo
- ✅ **Modo Consola**: Programa de prueba con operaciones CRUD automatizadas
- ✅ **Arquitectura Escalable**: Patrón DAO y Singleton implementados

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 (OpenJDK Temurin) | Lenguaje de programación |
| **Maven** | 3.x | Gestión de dependencias |
| **PostgreSQL** | 18.0 | Base de datos relacional |
| **JDBC** | 42.7.5 | Conectividad con base de datos |
| **Swing** | Java SE | Interfaz gráfica de usuario |
| **IntelliJ IDEA** | 2024.3.5 | IDE de desarrollo |

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- ☕ **Java JDK 17** o superior
  - Verificar: `java -version`
  
- 🗄️ **PostgreSQL 18** o superior
  - Verificar: `psql --version`
  
- 📦 **Maven 3.x**
  - Verificar: `mvn -version`
  
- 💻 **Git** (opcional)
  - Verificar: `git --version`

---

## 🚀 Instalación

### 1. Clonar o Descargar el Proyecto

```bash
# Opción 1: Clonar con Git
git clone https://github.com/tu-usuario/hotel-management.git
cd hotel-management

# Opción 2: Descargar ZIP y extraer
# Navegar a la carpeta del proyecto
```

### 2. Compilar el Proyecto

```bash
# Compilar con Maven
mvn clean compile

# O compilar y empaquetar
mvn clean package
```

### 3. Descargar Dependencias

Maven descargará automáticamente:
- PostgreSQL JDBC Driver (42.7.5)
- Checker Framework (3.48.3)

---

## 🗃️ Configuración de la Base de Datos

### 1. Crear la Base de Datos

```sql
-- Conectar a PostgreSQL
psql -U postgres

-- Crear la base de datos
CREATE DATABASE hotel;

-- Conectar a la base de datos
\c hotel
```

### 2. Ejecutar Script de Creación de Tablas

```bash
# Desde la línea de comandos
psql -U postgres -d hotel -f HotelTablesDDL.sql
```

O ejecutar el contenido del archivo `HotelTablesDDL.sql` en tu cliente PostgreSQL.

### 3. Cargar Datos Iniciales (Opcional)

```bash
# Cargar datos desde archivos CSV
psql -U postgres -d hotel -f CargaArchivos.sql
```

### 4. Configurar Credenciales

Editar el archivo `src/main/java/db/PostgreSQLConnection.java`:

```java
private static final String HOST = "localhost";
private static final String PORT = "5432";
private static final String USER = "postgres";       // ← Tu usuario
private static final String PASSWORD = "tu_password"; // ← Tu contraseña
private static final String DATABASE = "hotel";       // ← Tu base de datos
```

> ⚠️ **Nota de Seguridad**: En producción, utiliza variables de entorno o archivos de configuración externos.

---

## 📁 Estructura del Proyecto

```
HotelProyecto/
├── pom.xml                          # Configuración Maven
├── README.md                        # Este archivo
├── HotelTablesDDL.sql              # Script de creación de tablas
├── CargaArchivos.sql               # Script de carga de datos
├── Consultas.sql                   # Consultas de ejemplo
├── diccionario_datos.pdf           # Diccionario de datos
├── relacional_hotel.pdf            # Diagrama relacional
├── CSV/                            # Datos de prueba en CSV
│   ├── Cliente.csv
│   ├── Reserva.csv
│   └── ...
└── src/
    └── main/
        └── java/
            ├── db/                     # Conexión y configuración
            │   ├── DBConnection.java          # Clase abstracta base
            │   ├── PostgreSQLConnection.java  # Singleton de conexión
            │   ├── Main.java                  # Programa de prueba (consola)
            │   └── HotelApp.java             # Interfaz gráfica (GUI)
            │
            ├── dao/                    # Data Access Objects
            │   ├── PersonaDAO.java
            │   ├── ClienteDAO.java
            │   ├── HabitacionDAO.java
            │   ├── ReservaDAO.java
            │   ├── ServicioDAO.java
            │   ├── AreaDAO.java
            │   ├── EmpleadoDAO.java
            │   ├── TelefonoPerDAO.java
            │   ├── CorreoDAO.java
            │   └── ConsumoAdicionalDAO.java
            │
            └── modelo/                 # Clases de modelo
                ├── Persona.java
                ├── Cliente.java
                ├── Habitacion.java
                ├── Reserva.java
                ├── Servicio.java
                ├── Area.java
                ├── Empleado.java
                ├── TelefonoPer.java
                ├── Correo.java
                └── ConsumoAdicional.java
```

---

## 🎮 Uso

### Opción 1: Ejecutar con Interfaz Gráfica (GUI)

```bash
# Desde la raíz del proyecto
mvn clean compile exec:java -Dexec.mainClass="db.HotelApp"
```

O en IntelliJ IDEA:
1. Abrir `src/main/java/db/HotelApp.java`
2. Click derecho → `Run 'HotelApp.main()'`
3. O presionar `Shift + F10`

### Opción 2: Ejecutar Programa de Prueba (Consola)

```bash
# Desde la raíz del proyecto
mvn clean compile exec:java -Dexec.mainClass="db.Main"
```

O en IntelliJ IDEA:
1. Abrir `src/main/java/db/Main.java`
2. Click derecho → `Run 'Main.main()'`

### Compilar a JAR Ejecutable

```bash
# Crear JAR
mvn clean package

# Ejecutar JAR
java -jar target/hotel-management-1.0-SNAPSHOT.jar
```

---

## 📚 Módulos y Funcionalidades

### 👤 Gestión de Personas

**DAO**: `PersonaDAO.java`  
**Modelo**: `Persona.java`

**Operaciones**:
- ✅ Insertar nueva persona con información completa
- ✅ Actualizar datos personales
- ✅ Eliminar persona por cédula
- ✅ Buscar persona por cédula
- ✅ Listar todas las personas
- ✅ Buscar por apellido

**Atributos**:
- Cédula (PK)
- Nombres (primer y segundo)
- Apellidos (primer y segundo)
- Dirección (calle, carrera, número, complemento)

---

### 🧑‍💼 Gestión de Clientes

**DAO**: `ClienteDAO.java`  
**Modelo**: `Cliente.java`

**Operaciones**:
- ✅ Insertar cliente completo (hereda de Persona + correos)
- ✅ Actualizar información del cliente
- ✅ Eliminar cliente
- ✅ Buscar cliente por cédula
- ✅ Listar todos los clientes
- ✅ Gestión de correos electrónicos múltiples

**Relaciones**:
- Hereda de `Persona` (Generalización/Especialización)
- Tiene múltiples `Correo` (1:N)

---

### 🛏️ Gestión de Habitaciones

**DAO**: `HabitacionDAO.java`  
**Modelo**: `Habitacion.java`

**Operaciones**:
- ✅ Insertar nueva habitación
- ✅ Actualizar información de habitación
- ✅ Eliminar habitación
- ✅ Buscar por número
- ✅ Actualizar estado (Disponible/No disponible)
- ✅ Listar habitaciones disponibles
- ✅ Buscar por categoría
- ✅ Buscar por rango de precios

**Atributos**:
- Número de habitación (PK)
- Categoría (Simple, Doble, Suite, etc.)
- Estado (Disponible/No disponible)
- Precio por noche

---

### 📅 Gestión de Reservas

**DAO**: `ReservaDAO.java`  
**Modelo**: `Reserva.java`

**Operaciones**:
- ✅ Crear nueva reserva
- ✅ Actualizar fechas de reserva
- ✅ Eliminar reserva
- ✅ Buscar reserva específica
- ✅ Buscar reservas por cliente
- ✅ Buscar reservas por habitación
- ✅ Listar reservas activas
- ✅ Buscar reservas por rango de fechas
- ✅ Obtener reservas con detalles completos (Cliente + Habitación)

**Atributos**:
- Cédula del cliente (PK, FK)
- Número de habitación (PK, FK)
- Fecha de llegada (PK)
- Fecha de salida
- Tiempo máximo de cancelación (horas)

---

### 🛎️ Gestión de Servicios

**DAO**: `ServicioDAO.java`  
**Modelo**: `Servicio.java`

**Operaciones**:
- ✅ Insertar nuevo servicio
- ✅ Actualizar servicio
- ✅ Eliminar servicio
- ✅ Buscar por ID
- ✅ Buscar por nombre
- ✅ Listar todos los servicios
- ✅ Buscar por rango de costos

**Atributos**:
- ID del servicio (PK)
- Nombre del servicio
- Descripción/contenido
- Costo del servicio

**Ejemplos de Servicios**:
- Spa y masajes
- Gimnasio
- Room service
- Lavandería
- Transporte

---

### 🏢 Gestión de Áreas

**DAO**: `AreaDAO.java`  
**Modelo**: `Area.java`

**Operaciones**:
- ✅ Insertar nueva área
- ✅ Actualizar área
- ✅ Eliminar área
- ✅ Buscar por ID
- ✅ Buscar por nombre
- ✅ Listar todas las áreas

**Atributos**:
- ID del área (PK)
- Nombre del área

**Ejemplos de Áreas**:
- Recepción
- Limpieza
- Cocina
- Mantenimiento
- Administración

---

### 👷 Gestión de Empleados

**DAO**: `EmpleadoDAO.java`  
**Modelo**: `Empleado.java`

**Operaciones**:
- ✅ Insertar empleado completo (hereda de Persona)
- ✅ Actualizar información del empleado
- ✅ Eliminar empleado
- ✅ Buscar por cédula
- ✅ Buscar por cargo
- ✅ Buscar por área
- ✅ Listar empleados con detalles de área
- ✅ Obtener empleados con información completa

**Relaciones**:
- Hereda de `Persona`
- Pertenece a un `Area` (N:1)

**Atributos Adicionales**:
- Cargo
- ID del área

---

### 📞 Gestión de Teléfonos

**DAO**: `TelefonoPerDAO.java`  
**Modelo**: `TelefonoPer.java`

**Operaciones**:
- ✅ Insertar teléfono único
- ✅ Insertar múltiples teléfonos
- ✅ Actualizar teléfonos de una persona
- ✅ Eliminar teléfono específico
- ✅ Eliminar todos los teléfonos de una persona
- ✅ Buscar teléfonos por persona
- ✅ Verificar existencia de teléfono

**Relación**: Persona 1:N Teléfono

---

### 📧 Gestión de Correos

**DAO**: `CorreoDAO.java`  
**Modelo**: `Correo.java`

**Operaciones**:
- ✅ Insertar correo único
- ✅ Insertar múltiples correos
- ✅ Actualizar correos de un cliente
- ✅ Eliminar correo específico
- ✅ Eliminar todos los correos de un cliente
- ✅ Buscar correos por cliente
- ✅ Buscar clientes por dominio de correo

**Relación**: Cliente 1:N Correo

---

### 🍽️ Gestión de Consumos Adicionales

**DAO**: `ConsumoAdicionalDAO.java`  
**Modelo**: `ConsumoAdicional.java`

**Operaciones**:
- ✅ Registrar nuevo consumo
- ✅ Eliminar consumo
- ✅ Buscar consumos por reserva
- ✅ Buscar consumos por cliente
- ✅ Buscar consumos por servicio
- ✅ Buscar consumos por fecha
- ✅ Obtener consumos con detalles de servicio
- ✅ Calcular total de consumos de una reserva
- ✅ Obtener estadísticas de consumo

**Atributos**:
- Fecha de consumo (PK)
- Hora de consumo (PK)
- Fecha de llegada de reserva (PK, FK)
- Número de habitación (PK, FK)
- Cédula del cliente (PK, FK)
- ID del servicio (FK)

**Relación**: Consumo es la tabla de relación entre Reserva y Servicio (N:M)

---

## 🏗️ Arquitectura

### Patrón DAO (Data Access Object)

```
┌─────────────┐
│   Main.java │  (Capa de Presentación)
│ HotelApp.java│
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PersonaDAO  │  (Capa de Acceso a Datos)
│ ClienteDAO  │
│ ReservaDAO  │
│    ...      │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│DBConnection │  (Capa de Conexión)
│PostgreSQL   │
│ Connection  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │  (Base de Datos)
│   Database  │
└─────────────┘
```

### Patrón Singleton

La clase `PostgreSQLConnection` implementa el patrón Singleton para garantizar una única instancia de conexión:

```java
public class PostgreSQLConnection extends DBConnection {
    private static PostgreSQLConnection instancia;
    
    private PostgreSQLConnection() { /* Constructor privado */ }
    
    public static PostgreSQLConnection getConnector() {
        if (instancia == null) {
            instancia = new PostgreSQLConnection();
        }
        return instancia;
    }
}
```

### Herencia y Generalización

```
        Persona
          △
          │
    ┌─────┴─────┐
    │           │
 Cliente    Empleado
```

- `Cliente` y `Empleado` heredan de `Persona`
- Implementación en base de datos usando generalización/especialización

---

## 📊 Diagrama de Clases

### Clases de Modelo (Principales)

```
┌──────────────┐
│   Persona    │
├──────────────┤
│ - cedulaPer  │
│ - primerNom  │
│ - primerApell│
│ - dirección  │
└──────────────┘
       △
       │
   ┌───┴───┐
   │       │
┌──────┐  ┌──────────┐
│Cliente│  │ Empleado │
├──────┤  ├──────────┤
│correos│  │ - cargo  │
└──────┘  │ - idArea │
          └──────────┘

┌──────────────┐     ┌──────────────┐
│  Habitacion  │     │   Servicio   │
├──────────────┤     ├──────────────┤
│ - numeroHab  │     │ - idServicio │
│ - categoria  │     │ - nombre     │
│ - estado     │     │ - costo      │
│ - precio     │     └──────────────┘
└──────────────┘

┌──────────────┐
│   Reserva    │
├──────────────┤
│ - cedulaPer  │───┐
│ - numeroHab  │   │
│ - fechaLleg  │   │
│ - fechaSalid │   │
└──────────────┘   │
                   │
                   ▼
         ┌──────────────────┐
         │ConsumoAdicional  │
         ├──────────────────┤
         │ - fechaConsumo   │
         │ - horaConsumo    │
         │ - idServicio     │
         └──────────────────┘
```

---

## 🎨 Interfaz Gráfica

### Pantalla Principal

La aplicación GUI (`HotelApp.java`) presenta una ventana con pestañas (JTabbedPane) que permite navegar entre los diferentes módulos:

```
┌─────────────────────────────────────────┐
│ 🏨 Sistema de Gestión Hotelera          │
├─────────────────────────────────────────┤
│ [👤Personas][🧑‍💼Clientes][🛏️Habitaciones]│
│ [📅Reservas][🛎️Servicios][🏢Áreas]     │
│ [👷Empleados]                            │
├─────────────────────────────────────────┤
│                                         │
│  Formulario de entrada                  │
│  ┌─────────────┬──────────────┐        │
│  │ Campo 1:    │ [         ]  │        │
│  │ Campo 2:    │ [         ]  │        │
│  └─────────────┴──────────────┘        │
│                                         │
│  Tabla de datos                         │
│  ┌────────────────────────────┐        │
│  │ Col1 │ Col2 │ Col3 │ Col4  │        │
│  ├────────────────────────────┤        │
│  │ Dato │ Dato │ Dato │ Dato  │        │
│  └────────────────────────────┘        │
│                                         │
│  [Crear][Actualizar][Eliminar][Refrescar]│
└─────────────────────────────────────────┘
```

### Características de la GUI

- ✅ **Navegación por pestañas**: Cada entidad tiene su propia pe
