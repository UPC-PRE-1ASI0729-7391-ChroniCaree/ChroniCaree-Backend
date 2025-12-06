# Variables de Entorno para Railway

## ⚠️ IMPORTANTE: Configurar ANTES del Deploy

Ve a tu proyecto en Railway → **Settings → Variables** y agrega estas variables:

---

## 🔴 OBLIGATORIAS (Sin estas, el deploy fallará)

```bash
# Perfil de Spring Boot
SPRING_PROFILES_ACTIVE=prod

# JWT Secret (GENERAR UNO SEGURO)
JWT_SECRET=TU_SECRET_KEY_SUPER_SEGURA_AQUI

# CORS (Tus dominios de frontend)
CORS_ALLOWED_ORIGINS=https://tu-dominio.com,https://www.tu-dominio.com
```

---

## 🟢 Base de Datos MySQL (Railway proporciona estas automáticamente)

Si usas MySQL de Railway, estas variables ya están disponibles:

```bash
MYSQLHOST
MYSQLPORT
MYSQLDATABASE
MYSQLUSER
MYSQLPASSWORD
```

**No necesitas agregarlas manualmente** si usas el plugin de MySQL de Railway.

---

## 🟡 Opcionales (con valores por defecto)

```bash
# Puerto (Railway lo proporciona automáticamente)
PORT=8080

# Logging level (opcional)
LOGGING_LEVEL_ROOT=INFO
```

---

## 🔧 Generar JWT_SECRET Seguro

### Opción 1: PowerShell (Windows)
```powershell
$bytes = New-Object byte[] 64
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
[Convert]::ToBase64String($bytes)
```

### Opción 2: Online
1. Ve a: https://generate-random.org/api-key-generator
2. Selecciona 512-bit
3. Copia el resultado

### Opción 3: OpenSSL (si tienes Git Bash)
```bash
openssl rand -base64 64
```

---

## 📋 Checklist de Configuración

Antes de hacer deploy, verifica:

- [ ] ✅ `SPRING_PROFILES_ACTIVE=prod` está configurado
- [ ] ✅ `JWT_SECRET` tiene un valor largo y aleatorio (NO uses el de desarrollo)
- [ ] ✅ `CORS_ALLOWED_ORIGINS` tiene tus dominios reales (NO uses localhost)
- [ ] ✅ MySQL está conectado como plugin o las variables MYSQL* están configuradas
- [ ] ✅ El archivo `config/SecurityConfig.java` está eliminado (conflicto resuelto)

---

## 🚀 Orden de Deploy

1. **Commit los cambios locales:**
   ```bash
   git add .
   git commit -m "fix: Remove duplicate SecurityConfig, configure Railway for production"
   git push
   ```

2. **Verificar variables en Railway:**
   - Railway Dashboard → Tu proyecto → Settings → Variables
   - Agregar todas las variables OBLIGATORIAS

3. **Trigger deploy:**
   - Railway detectará el push automáticamente
   - O manualmente: Settings → Deploy Trigger → Deploy Now

4. **Ver logs:**
   - Railway Dashboard → Deployments → View Logs
   - Buscar: "Started ChronicareeBackendApplication"

---

## 🐛 Troubleshooting

### "ConflictingBeanDefinitionException: securityConfig"
✅ **Solucionado:** Eliminado `config/SecurityConfig.java`
- Hacer commit y push de este cambio

### "No active profile set, falling back to 1 default profile: 'default'"
❌ **Falta:** Variable `SPRING_PROFILES_ACTIVE=prod` en Railway
- Agregar en Settings → Variables

### "Healthcheck failed"
✅ **Optimizado:** 
- Healthcheck path cambiado a `/` (más simple)
- Timeout reducido a 180s
- Railway ahora revisa la raíz, que responde más rápido

### "Cannot load JDBC driver"
❌ **Falta:** Plugin MySQL o variables MYSQL*
- Agregar plugin MySQL en Railway
- O configurar variables MYSQLHOST, MYSQLPORT, etc. manualmente

---

## 📊 Verificar Deploy Exitoso

Una vez desplegado, prueba:

```bash
# Healthcheck (debe responder "ChroniCare Backend is running!")
curl https://tu-app.up.railway.app/

# Actuator health
curl https://tu-app.up.railway.app/actuator/health

# API test (debe dar 401 Unauthorized - es correcto, significa que el JWT funciona)
curl https://tu-app.up.railway.app/api/v1/users
```

---

## 🎯 Resultado Esperado

En los logs de Railway debes ver:

```
✅ The following 1 profile is active: "prod"
✅ Started ChronicareeBackendApplication in X.XXX seconds
✅ Tomcat started on port 8080
✅ Healthcheck passed
```

---

**Última actualización:** 5 de Diciembre, 2025
