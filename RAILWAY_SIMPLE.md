# 🚀 RAILWAY DEPLOYMENT - 5 PASOS SIMPLES

## ✅ CONFIGURACIÓN COMPLETADA

Ya está todo configurado para Railway:
- ✅ `Dockerfile` creado
- ✅ `railway.json` configurado
- ✅ `application.properties` con variables Railway
- ✅ Health checks habilitados

---

## 📋 DEPLOYMENT EN 5 PASOS

### 1️⃣ Push a GitHub (1 minuto)

```powershell
git add .
git commit -m "feat: Railway deployment ready"
git push origin develop
```

### 2️⃣ Crear Proyecto en Railway (2 minutos)

1. Ve a **https://railway.app**
2. Click **"New Project"**
3. Click **"Deploy from GitHub repo"**
4. Selecciona **`ChroniCaree-Backend`**
5. Selecciona branch **`develop`**

### 3️⃣ Agregar MySQL (1 minuto)

1. En el dashboard, click **"+ New"**
2. Click **"Database"**
3. Click **"Add MySQL"**
4. ⏳ **Espera 2 minutos** a que MySQL termine de iniciar

### 4️⃣ Configurar JWT Secret (30 segundos)

1. Click en tu servicio backend (no en MySQL)
2. Ve a la pestaña **"Variables"**
3. Agrega SOLO estas 2 variables:

```
JWT_SECRET=<COPIA_LA_CLAVE_DE_ABAJO>
JWT_EXPIRATION_DAYS=7
```

**Genera JWT_SECRET** (PowerShell):
```powershell
-join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | % {[char]$_})
```

O copia esta de ejemplo (⚠️ cámbiala en producción):
```
myVerySecureJWTSecretKeyForProductionUseMinimum256BitsLongForSecurity2024
```

### 5️⃣ Deploy (automático)

Railway deployará automáticamente. **NO necesitas configurar más variables** porque:
- ✅ `MYSQL_URL` - Railway la provee automáticamente
- ✅ `MYSQLUSER` - Railway la provee automáticamente  
- ✅ `MYSQLPASSWORD` - Railway la provee automáticamente
- ✅ `PORT` - Railway la asigna automáticamente

---

## 🎯 VERIFICACIÓN

### Ver Logs del Deployment

1. Click en **"Deployments"**
2. Click en el deployment actual
3. Click **"View Logs"**

### Busca estas líneas (todo OK):

```
✓ Building Docker image...
✓ [build] BUILD SUCCESS
✓ Starting container...
✓ Tomcat started on port(s): 8080
✓ Started ChronicareeBackendApplication
```

### Test tu API

Railway te dará una URL como:
```
https://chronicaree-backend-production.up.railway.app
```

**Test health check:**
```bash
curl https://tu-app.railway.app/actuator/health
```

Esperado: `{"status":"UP"}`

**Test API:**
```bash
curl https://tu-app.railway.app/api/v1/alerts
```

---

## ⚠️ ERRORES COMUNES

### Error: "Connection refused MySQL"
**Solución**: Espera 2-3 minutos más. MySQL tarda en iniciar.

### Error: "Builder not found"  
**Solución**: Asegúrate de hacer `git push` del `Dockerfile` y `railway.json`

### Error: "Health check timeout"
**Solución**: 
1. Ve a **Settings** → **Deploy**
2. Cambia **Health Check Timeout** a `300` segundos

---

## 🎉 ¡LISTO!

Si ves `{"status":"UP"}` en el health check, **¡tu backend está funcionando!**

### Próximo paso:

Actualiza tu frontend con la nueva URL:

```typescript
// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://tu-app.railway.app/api/v1'
};
```

---

## 📝 NOTAS IMPORTANTES

### Variables que NO necesitas configurar:

Railway provee automáticamente:
- `MYSQL_URL` - URL completa de conexión MySQL
- `MYSQLUSER` - Usuario de la base de datos
- `MYSQLPASSWORD` - Password de la base de datos
- `PORT` - Puerto donde correrá tu app

### Variables que SÍ debes configurar:

Solo estas dos:
- `JWT_SECRET` - Para firma de tokens JWT
- `JWT_EXPIRATION_DAYS` - Días de validez del token (7 por defecto)

### Configuración por defecto:

Ya está configurado en `application.properties`:
- `spring.jpa.hibernate.ddl-auto=update` ✅
- `spring.jpa.show-sql=false` ✅
- `server.port=${PORT:8080}` ✅

---

**Tiempo total**: ~5 minutos  
**Dificultad**: ⭐ Fácil  
**Costo**: 💰 Gratis (tier gratuito Railway)

---

🔗 **Railway Dashboard**: https://railway.app/dashboard
