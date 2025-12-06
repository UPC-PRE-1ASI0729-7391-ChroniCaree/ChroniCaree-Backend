# 🚨 SOLUCIÓN: Railway "Killed" - Out of Memory

## ❌ PROBLEMA IDENTIFICADO

Tu app fue **KILLED** por Railway debido a **falta de memoria RAM**:

```
Killed
```

Esto sucede porque:
1. ⚠️ Railway plan gratuito tiene **512MB RAM**
2. ⚠️ Spring Boot + Hibernate consume mucha memoria al iniciar
3. ⚠️ El DDL validation (`ddl-auto=validate`) ejecuta queries pesadas

---

## ✅ SOLUCIONES APLICADAS

### 1. **Dockerfile Optimizado** ✅
- **Xms:** 128MB (memoria inicial)
- **Xmx:** 384MB (memoria máxima - deja margen para el sistema)
- **G1GC:** Garbage collector optimizado para bajo uso de memoria
- **MaxGCPauseMillis:** 100ms (pausas cortas)
- **UseStringDeduplication:** Reduce duplicación de strings

### 2. **application-prod.properties** ✅
- **ddl-auto:** Cambiado de `validate` a `none`
- Evita que Hibernate ejecute queries de validación al inicio
- Reduce consumo de memoria durante el startup

### 3. **railway.json** ✅
- **healthcheckPath:** `/actuator/health`
- **healthcheckTimeout:** 300 segundos (más tiempo para arrancar)

---

## 🔧 PRÓXIMOS PASOS

### **1. Commit y Push (AHORA)**

```powershell
git add .
git commit -m "fix: Optimize memory usage for Railway deployment (Xmx=384m, ddl-auto=none)"
git push
```

### **2. Verificar Variables en Railway**

Asegúrate que estas variables existen en Railway:

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=tu-secret-seguro-aqui
CORS_ALLOWED_ORIGINS=https://tu-dominio.com
```

### **3. Monitorear Logs**

En Railway, busca estas líneas:

✅ **ÉXITO:**
```
The following 1 profile is active: "prod"
HikariPool-1 - Start completed
Started ChronicareeBackendApplication in X.XXX seconds
Tomcat started on port 8080
```

❌ **FALLO (si vuelve a pasar):**
```
Killed
```

---

## 🆘 SI SIGUE FALLANDO

### Opción A: Upgrade Railway Plan
Railway Hobby Plan ($5/mes):
- **1GB RAM** (suficiente para Spring Boot)
- Más estable para producción

### Opción B: Optimizar Más

1. **Reducir repositorios JPA:**
```properties
# En application-prod.properties
spring.jpa.open-in-view=false
```

2. **Lazy loading:**
```properties
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=false
```

3. **Reducir pool de conexiones:**
```properties
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
```

### Opción C: Cambiar a Flyway

En lugar de Hibernate DDL, usa Flyway para migrations:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

```properties
# application-prod.properties
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=true
```

---

## 📊 COMPARACIÓN DE MEMORIA

### Antes (causaba Killed):
```
Xmx: ~512MB (default)
ddl-auto: validate (ejecuta queries pesadas)
GC: Default (más consumo)
```

### Después (optimizado):
```
Xms: 128MB
Xmx: 384MB (deja 128MB para OS)
ddl-auto: none (sin validación)
GC: G1GC (optimizado)
```

---

## 🎯 EXPECTED OUTCOME

Con estos cambios, el startup debería usar:
- **~250-300MB RAM** durante inicio
- **~200-250MB RAM** en estado estable
- **Tiempo de inicio:** 20-30 segundos

---

## 🔍 DEBUG MEMORY ISSUES

Si necesitas ver el uso de memoria:

```bash
# En Railway logs, buscar:
HikariPool-1 - Starting...
HikariPool-1 - Start completed.

# Si ves "Killed" antes de "Start completed", es OOM
```

Para debugging local:
```powershell
# Ver uso de memoria
java -Xms128m -Xmx384m -XX:+PrintFlagsFinal -version | Select-String -Pattern "MaxHeapSize"
```

---

## ✅ CHECKLIST FINAL

Antes de hacer push:

- [x] ✅ Dockerfile actualizado (Xms=128m, Xmx=384m)
- [x] ✅ application-prod.properties (ddl-auto=none)
- [x] ✅ railway.json (healthcheck timeout=300)
- [ ] ⏳ Variables configuradas en Railway
- [ ] ⏳ Commit y push ejecutados
- [ ] ⏳ Logs monitoreados

---

**IMPORTANTE:** Si Railway sigue matando el proceso, necesitas:
1. Upgrade a plan con más RAM, O
2. Usar base de datos externa (menos carga en la app)

---

**Creado:** 5 de Diciembre, 2025
**Problema:** Railway OOM Killed
**Solución:** Optimización JVM + DDL none
