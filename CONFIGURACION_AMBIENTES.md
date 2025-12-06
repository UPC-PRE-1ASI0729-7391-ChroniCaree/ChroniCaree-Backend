# Configuración de Ambientes - ChroniCare Backend

Este proyecto tiene configuraciones separadas para **Desarrollo** y **Producción**.

## Archivos Creados

1. **ENDPOINTS.txt** - Lista completa de todos los endpoints (~190+ endpoints)
2. **application-dev.properties** - Configuración para desarrollo
3. **application-prod.properties** - Configuración para producción

---

## 🔧 DESARROLLO (Local)

### Activar el perfil de desarrollo

**Opción 1: Variable de entorno**
```bash
# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="dev"
mvn spring-boot:run

# Linux/Mac
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

**Opción 2: Parámetro de Maven**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Opción 3: IntelliJ IDEA / Eclipse**
- En "Run Configuration" agrega: `-Dspring.profiles.active=dev`

### Variables de entorno necesarias (Development)

```properties
# Base de datos (opcional, usa valores por defecto si no están presentes)
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=chronicare_dev
MYSQLUSER=root
MYSQLPASSWORD=tu_password

# JWT (opcional, usa valor por defecto)
JWT_SECRET=dev-secret-key-chronicare-2024-change-in-production

# CORS (opcional)
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,http://localhost:5173

# Puerto (opcional, por defecto 8080)
PORT=8080
```

### Características del ambiente de desarrollo:
- ✅ SQL queries visibles en logs
- ✅ Logs detallados (DEBUG level)
- ✅ Todos los endpoints de actuator expuestos
- ✅ Stack traces completos en errores
- ✅ Hot reload con DevTools
- ✅ Base de datos actualizada automáticamente (ddl-auto=update)
- ✅ CORS permisivo para localhost

---

## 🚀 PRODUCCIÓN (Railway/Cloud)

### Activar el perfil de producción

**Railway (configuración recomendada)**

En Railway, agrega estas variables de entorno:

```properties
# Perfil activo
SPRING_PROFILES_ACTIVE=prod

# Base de datos (REQUERIDO)
MYSQLHOST=your-mysql-host.railway.app
MYSQLPORT=3306
MYSQLDATABASE=chronicare_prod
MYSQLUSER=root
MYSQLPASSWORD=your-secure-password

# JWT Secret (REQUERIDO - generar uno seguro)
JWT_SECRET=tu-secret-key-super-segura-aqui-usa-un-generador

# CORS (REQUERIDO)
CORS_ALLOWED_ORIGINS=https://tu-dominio.com,https://www.tu-dominio.com

# Puerto (Railway lo proporciona automáticamente)
PORT=8080
```

### Cómo generar JWT_SECRET seguro:
```bash
# Opción 1: OpenSSL
openssl rand -base64 64

# Opción 2: PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))

# Opción 3: Online
# Usar: https://generate-random.org/api-key-generator
```

### Características del ambiente de producción:
- ✅ Sin logs SQL (optimizado)
- ✅ Logs mínimos (WARN/INFO level)
- ✅ Solo endpoints críticos de actuator
- ✅ Errores sin stack traces (seguridad)
- ✅ Pool de conexiones optimizado (20 conexiones)
- ✅ DDL validation only (no cambios automáticos)
- ✅ CORS restrictivo (solo dominios específicos)
- ✅ Cookies seguras (secure, httpOnly, sameSite)
- ✅ Compresión habilitada

---

## 📋 Archivo application.properties Principal

Tu `src/main/resources/application.properties` actual debe quedarse como está o actualizarlo a:

```properties
# Perfil activo por defecto
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}

# Resto de configuración común...
```

---

## 🧪 Verificar el ambiente actual

**Endpoint de health:**
```bash
curl http://localhost:8080/actuator/health
```

**Ver logs al inicio:**
Busca en la consola:
```
The following 1 profile is active: "dev"
# o
The following 1 profile is active: "prod"
```

---

## 📁 Estructura de archivos

```
ChroniCaree-Backend/
├── application-dev.properties          # Desarrollo
├── application-prod.properties         # Producción
├── ENDPOINTS.txt                       # Lista de endpoints
├── CONFIGURACION_AMBIENTES.md         # Este archivo
└── src/main/resources/
    └── application.properties          # Configuración base
```

---

## 🔄 Cambiar entre ambientes

### Local (desarrollo → producción):
```bash
# Cambiar a producción localmente (no recomendado)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Railway (desarrollo → producción):
1. Ve a tu proyecto en Railway
2. Settings → Variables
3. Cambia `SPRING_PROFILES_ACTIVE=prod`
4. Redeploy automático

---

## ⚠️ IMPORTANTE

### Para Desarrollo:
- ✅ Usa `application-dev.properties`
- ✅ Puedes usar valores por defecto
- ✅ Base de datos local MySQL
- ✅ JWT_SECRET puede ser simple

### Para Producción:
- ❌ **NUNCA** uses los valores por defecto
- ✅ **SIEMPRE** configura JWT_SECRET seguro
- ✅ **SIEMPRE** usa CORS restrictivo
- ✅ **SIEMPRE** usa SSL/TLS en base de datos
- ✅ Valida que `ddl-auto=validate` (no `update`)

---

## 📞 Endpoints de Prueba

### Health Check:
```bash
# Desarrollo
curl http://localhost:8080/actuator/health

# Producción (Railway)
curl https://tu-app.up.railway.app/actuator/health
```

### Login:
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "password123"
  }'
```

---

## 🛠️ Troubleshooting

### "No active profile set"
- Agrega `SPRING_PROFILES_ACTIVE=dev` o `prod`

### "Cannot load JDBC driver"
- Verifica que las variables de BD estén correctas

### "JWT signature does not match"
- JWT_SECRET cambió entre despliegues
- Usa el mismo secret o regenera tokens

### "CORS policy blocked"
- Agrega tu frontend a `CORS_ALLOWED_ORIGINS`

---

## 📚 Referencias

- Spring Profiles: https://docs.spring.io/spring-boot/reference/features/profiles.html
- Actuator: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html
- Railway Docs: https://docs.railway.app/

---

**Creado el:** 5 de Diciembre, 2025
**Proyecto:** ChroniCare Backend
**Versión:** 1.0.0
