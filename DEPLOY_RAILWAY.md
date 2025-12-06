# ========================================
# GUÍA DE DEPLOY RÁPIDA - RAILWAY
# ========================================

Este archivo contiene los comandos exactos para hacer deploy a Railway.

## 🚀 PASOS PARA DEPLOY

### 1. Commit los cambios (EN TU MÁQUINA LOCAL)

```powershell
# Ver cambios
git status

# Agregar todos los archivos
git add .

# Commit con mensaje descriptivo
git commit -m "fix: Remove duplicate SecurityConfig and configure Railway production environment"

# Push a Railway
git push
```

---

### 2. Configurar Variables de Entorno en Railway

**Railway Dashboard → Tu Proyecto → Settings → Variables**

Agregar estas variables (COPIAR Y PEGAR):

```
SPRING_PROFILES_ACTIVE=prod
```

```
JWT_SECRET=GENERA_UNO_SEGURO_AQUI
```

```
CORS_ALLOWED_ORIGINS=https://tu-dominio.com
```

**💡 Generar JWT_SECRET:** Ejecuta en PowerShell:
```powershell
$bytes = New-Object byte[] 64; [System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes); [Convert]::ToBase64String($bytes)
```

---

### 3. Conectar MySQL (Si no está conectado)

**Railway Dashboard → Tu Proyecto → New → Database → MySQL**

Railway creará automáticamente estas variables:
- `MYSQLHOST`
- `MYSQLPORT`
- `MYSQLDATABASE`
- `MYSQLUSER`
- `MYSQLPASSWORD`

---

### 4. Trigger Deploy

**Opción A:** Automático (push a git)
```powershell
git push
```

**Opción B:** Manual
1. Railway Dashboard
2. Tu proyecto
3. Settings → Deploy Trigger
4. Click "Deploy Now"

---

### 5. Ver Logs en Tiempo Real

**Railway Dashboard → Deployments → View Logs**

Buscar estas líneas (indican éxito):

```
✅ The following 1 profile is active: "prod"
✅ Started ChronicareeBackendApplication
✅ Tomcat started on port 8080
✅ Healthcheck passed
```

---

## 🐛 Si Falla el Deploy

### Error: "ConflictingBeanDefinitionException"
**Causa:** Archivo `config/SecurityConfig.java` aún existe en git

**Solución:**
```powershell
# Verificar si existe
Test-Path "src\main\java\com\chronicare\platform\config\SecurityConfig.java"

# Si existe, eliminarlo
Remove-Item "src\main\java\com\chronicare\platform\config\SecurityConfig.java" -Force

# Commit y push
git add .
git commit -m "fix: Remove duplicate SecurityConfig"
git push
```

---

### Error: "No active profile set, falling back to default"
**Causa:** Variable `SPRING_PROFILES_ACTIVE` no configurada

**Solución:**
1. Railway Dashboard → Settings → Variables
2. Agregar: `SPRING_PROFILES_ACTIVE` = `prod`
3. Redeploy automáticamente

---

### Error: "Healthcheck failed"
**Causa:** App tarda en arrancar

**Solución:** Ya está optimizado en `railway.json`:
- Healthcheck path: `/` (más rápido)
- Timeout: 180 segundos
- Reintentos: 3

**Si persiste:**
```powershell
# Ver logs en Railway para identificar el error real
# Buscar líneas con ERROR o WARN
```

---

### Error: "Cannot connect to MySQL"
**Causa:** MySQL no configurado o variables incorrectas

**Solución:**
1. Railway → New → Database → Add MySQL
2. Verificar que las variables MYSQL* existen en Variables
3. Redeploy

---

## ✅ Verificar Deploy Exitoso

### Test 1: Healthcheck
```powershell
curl https://chronicaree-backend-production.up.railway.app/
```
**Esperado:** "ChroniCare Backend is running!"

### Test 2: Actuator
```powershell
curl https://chronicaree-backend-production.up.railway.app/actuator/health
```
**Esperado:** `{"status":"UP"}`

### Test 3: API Endpoint
```powershell
curl https://chronicaree-backend-production.up.railway.app/api/v1/users
```
**Esperado:** `401 Unauthorized` (correcto, significa que JWT funciona)

---

## 📊 Checklist Pre-Deploy

Antes de hacer push, verifica:

- [ ] ✅ `config/SecurityConfig.java` eliminado
- [ ] ✅ `railway.json` actualizado
- [ ] ✅ Variables de entorno configuradas en Railway
- [ ] ✅ MySQL conectado
- [ ] ✅ JWT_SECRET generado y configurado
- [ ] ✅ CORS_ALLOWED_ORIGINS configurado con dominios reales

---

## 🎯 Comandos Rápidos

```powershell
# Ver estado de git
git status

# Agregar, commit y push en un solo flujo
git add .
git commit -m "fix: Configure Railway production deployment"
git push

# Ver logs remotos de Railway (en el navegador)
# Railway Dashboard → Deployments → Latest → View Logs
```

---

## 📞 URLs de Referencia

- **Railway Dashboard:** https://railway.app/dashboard
- **Documentación Railway:** https://docs.railway.app/
- **Spring Boot Profiles:** https://docs.spring.io/spring-boot/reference/features/profiles.html

---

**Creado:** 5 de Diciembre, 2025
**Proyecto:** ChroniCare Backend
