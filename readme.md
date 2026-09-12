# 🎬 Plataforma de Streaming (Java + SQLite + OMDb)

![OpenJDK](https://img.shields.io/badge/Java-11%2B-ED8B00?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-FF6C37)
![SQLite](https://img.shields.io/badge/SQLite-003B57?logo=sqlite&logoColor=white)
![MVC](https://img.shields.io/badge/MVC-Modelo--Vista--Controlador-6f42c1)
![DAO](https://img.shields.io/badge/DAO-Acceso%20a%20datos-28a745)
![OMDb](https://img.shields.io/badge/API-OMDb-14a0c4)
![GitHub last commit](https://img.shields.io/github/last-commit/alandprieto/plataforma-streaming-java)

Aplicación de escritorio de una plataforma de streaming desarrollada en **Java Swing**, con persistencia en **SQLite** e integración con la **API de OMDb**. Corresponde al Entregable 2 del *Taller de Lenguajes II* (UNLP, 2025) como prueba de concepto.

Permite registrar e iniciar sesión, explorar un catálogo de ~10.000 películas precargadas, ver detalles y sinopsis en tiempo real desde OMDb, calificar películas con reseñas y ordenar el catálogo por género o título.

## ✨ Funcionalidades

- **Autenticación:** registro de clientes e inicio de sesión con validación de credenciales.
- **Exploración del catálogo:** Top 10 mejor calificadas, exploración aleatoria y búsqueda en OMDb.
- **Detalles en vivo:** sinopsis, año y rating IMDb obtenidos de la API OMDb (consulta asincrónica).
- **Reseñas:** calificación del 1 al 10 con comentario obligatorio y prevención de reseñas duplicadas por usuario.
- **Ordenamiento:** por género o título sobre el listado visible.
- **Interfaz no bloqueante:** carga de posteres y consultas HTTP en hilos separados (`SwingWorker` / `invokeLater`).
- **Persistencia:** SQLite con patrón DAO y precarga automática del catálogo desde un CSV si la base está vacía.

## 🧱 Arquitectura

El proyecto sigue una arquitectura **MVC** (Modelo-Vista-Controlador) combinada con el patrón **DAO** para el acceso a datos:

- `src/vista/` → Interfaces gráficas con Java Swing.
- `src/controlador/` → Manejo de eventos de UI y coordinación con la lógica de negocio.
- `src/modelo/` → Entidades del dominio (`Usuario`, `Pelicula`, `Resenia`, `Contenido`, ...).
- `src/servicio/` → Lógica de negocio (`AppImple`) e integración con OMDb.
- `src/dao/` → Interfaces e implementaciones DAO (`Usuario`, `Pelicula`, `Resenia`).
- `src/database/` → Gestión de la conexión SQLite, creación de tablas y precarga de datos.

## 📁 Estructura del Proyecto

```
src/
├── comparador/     # Comparadores para ordenar películas y usuarios
├── controlador/    # Controladores MVC + AppGUI (punto de entrada)
├── dao/            # Interfaces e implementaciones DAO
├── database/       # ConexionBD, SetupBD, AutoCargaPeliculas y movies_database.csv
├── enums/          # GeneroPelicula
├── excepciones/    # Excepciones propias
├── modelo/         # Entidades del dominio
├── servicio/       # AppImple y ConsultaPeliculasOMDb
└── vista/          # Vistas Swing
lib/                # Dependencias externas (sqlite-jdbc, json)
streaming.db        # Base SQLite (se genera la primera vez que se ejecuta; está en .gitignore)
omdb.properties     # API key de OMDb (opcional, local, está en .gitignore)
```

## ✅ Requisitos

- **JDK 11 o superior** (usa `java.net.http` y lambdas).
- Las dependencias ya están incluidas en `lib/`:
  - `sqlite-jdbc-3.50.3.0.jar`
  - `json-20231013.jar`
- Conexión a internet para consultar la API de OMDb (opcional; el resto funciona offline).

## 🚀 Ejecución

### Opción 1: con el script (Windows)

```bat
compile.bat
java -cp "bin;lib/*" controlador.AppGUI
```

### Opción 2: manual

```bat
REM Compilar (genera la lista de fuentes y la compila)
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -cp "lib/*;src" -d "bin" @sources.txt

REM Ejecutar
java -cp "bin;lib/*" controlador.AppGUI
```

> La base de datos `streaming.db` y el catálogo se crean/cargan automáticamente en el primer inicio si no existen.

### 🔑 Credenciales de prueba

| Rol     | Email                  | Contraseña  |
| ------- | ---------------------- | ----------- |
| Cliente | `alan@gmail.com`       | `12345678`  |
| Admin   | `admin1@streaming.com` | `admin123`  |

También podés registrar un nuevo usuario desde la pantalla de login.

## 🔌 API de OMDb

Las consultas a OMDb necesitan una API key. Para configurarla, a elección:

1. **Variable de entorno:** definir `OMDB_API_KEY=tu_clave`.
2. **Archivo local** `omdb.properties` (ignorado por git) con:
   `omdb.api.key=tu_clave`

Si la clave no está configurada, las búsquedas de OMDb muestran "sin resultados" pero el resto de la app funciona normal. Obtené una clave gratuita en [omdbapi.com](https://www.omdbapi.com/apikey.aspx).

## 🧪 Notas de diseño

- Las reseñas se consideran aprobadas al momento de guardarse (el flujo de moderación de administradores no se expone en la UI actual).
- `streaming.db` y los posteres se almacenan de forma local; el catálogo fuente es `src/database/movies_database.csv`.
- La base de datos, los archivos compilados (`bin/`) y la documentación generada (`doc/`) no se versionan (ver `.gitignore`).

## 📌 Proyecto académico

Desarrollado como trabajo práctico del Taller de Lenguajes II. No es un producto comercial: prioriza la demostración de conceptos de persistencia, patrones de diseño y concurrencia en Java.
