# Guía de Ejecución - ChroniCare Backend

Este proyecto ha sido configurado para ejecutarse con **Java 21** y **Maven**.
La base de datos se creará automáticamente gracias a la configuración `createDatabaseIfNotExist=true` en `application.properties`.

## Requisitos Previos

1. **Java 21**: Asegúrate de tener instalado el JDK 21.
2. **MySQL**: Debe estar ejecutándose en el puerto 3306.
   - Usuario: `root`
   - Contraseña: `1234` (o la que hayas configurado en `application.properties`)

## Cómo Ejecutar

Hemos preparado scripts para facilitar la ejecución:

### 1. Verificar MySQL
Ejecuta el script para confirmar que MySQL está listo:
```powershell
.\check-mysql.ps1
```

### 2. Iniciar la Aplicación
Ejecuta el script de arranque. Este script limpiará el proyecto y lo iniciará:
```powershell
.\run.ps1
```

O manualmente:
```bash
mvn clean spring-boot:run -DskipTests
```

## Verificación de la Base de Datos

Una vez que la aplicación inicie (verás logs como `Tomcat started on port 11083`), la base de datos `dbchronicaree_backend` se habrá creado automáticamente.
Puedes verificarlo en MySQL Workbench.

## Swagger UI

La documentación de la API estará disponible en:
http://localhost:11083/swagger-ui.html
