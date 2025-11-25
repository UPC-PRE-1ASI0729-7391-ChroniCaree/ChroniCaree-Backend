# 📚 Guía Completa de Uso de Swagger y Autenticación JWT

## 🌐 Acceso a Swagger UI

La aplicación está ejecutándose en: **http://localhost:11083**

### URLs Disponibles:
- **Swagger UI (Interfaz Visual)**: http://localhost:11083/swagger-ui.html
- **API Docs JSON**: http://localhost:11083/v3/api-docs
- **API Docs YAML**: http://localhost:11083/v3/api-docs.yaml

---

## 🔐 Sistema de Autenticación JWT

ChroniCare Backend usa **JWT (JSON Web Tokens)** para autenticación. Todos los endpoints (excepto los de autenticación) requieren un token válido.

### Configuración del Token JWT:
```properties
Secret Key: mySecretKeyForJwtTokenGenerationWhichShouldBeLongEnough
Expiración: 7 días
Tipo: Bearer Token
```

---

## 📝 Pasos para Autenticarte y Usar la API

### **PASO 1: Registrar un Usuario**

1. Abre Swagger UI: http://localhost:11083/swagger-ui.html
2. Busca la sección **"IAM"** o **"Authentication"**
3. Encuentra el endpoint: `POST /api/v1/authentication/sign-up`
4. Haz clic en **"Try it out"**
5. Completa el JSON de ejemplo:

```json
{
  "email": "admin@chronicare.com",
  "password": "Admin123!",
  "name": "Administrador ChroniCare",
  "role": "ROLE_ADMIN",
  "tenantId": 1
}
```

6. Haz clic en **"Execute"**
7. Deberías recibir una respuesta exitosa (201 Created)

---

### **PASO 2: Iniciar Sesión y Obtener el Token**

1. En Swagger, busca el endpoint: `POST /api/v1/authentication/sign-in`
2. Haz clic en **"Try it out"**
3. Ingresa las credenciales:

```json
{
  "username": "admin@chronicare.com",
  "password": "Admin123!"
}
```

4. Haz clic en **"Execute"**
5. En la respuesta verás algo como:

```json
{
  "id": 1,
  "username": "admin@chronicare.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBjaHJvbmljYXJlLmNvbSIsImlhdCI6MTYzOTU4NzYwMCwiZXhwIjoxNjQwMTkyNDAwfQ.abc123xyz..."
}
```

6. **¡COPIA EL TOKEN!** Lo necesitarás para el siguiente paso.

---

### **PASO 3: Configurar el Token en Swagger**

Ahora que tienes el token, debes configurarlo en Swagger para que todas tus peticiones lo incluyan automáticamente:

#### **Opción A: Usando el Botón "Authorize" (RECOMENDADO)**

1. En la parte superior derecha de Swagger UI, verás un botón verde llamado **"Authorize"** 🔓
2. Haz clic en él
3. Se abrirá un modal con el campo **"bearerAuth (http, Bearer)"**
4. En el campo de texto, pega SOLO el token (sin "Bearer"):
   ```
   eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBjaHJvbmljYXJlLmNvbSIsImlhdCI6MTYzOTU4NzYwMCwiZXhwIjoxNjQwMTkyNDAwfQ.abc123xyz...
   ```
5. Haz clic en **"Authorize"**
6. Luego en **"Close"**

¡Listo! Ahora todos tus endpoints usarán automáticamente este token.

#### **Opción B: Manual en cada petición**

Si prefieres configurarlo manualmente en cada petición:

1. Abre cualquier endpoint protegido (ej: `GET /api/v1/patients`)
2. Haz clic en **"Try it out"**
3. En la sección de **Headers**, agrega:
   - **Authorization**: `Bearer TU_TOKEN_AQUI`

---

## 🧪 Probando Endpoints Protegidos

Una vez autenticado, puedes probar cualquier endpoint. Ejemplo:

### **Crear un Paciente:**

1. Busca: `POST /api/v1/patients`
2. Click en **"Try it out"**
3. Completa el JSON:

```json
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "dni": "12345678",
  "birthDate": "1990-05-15",
  "gender": "M",
  "phone": "+51987654321",
  "address": "Av. Arequipa 123, Lima",
  "weight": 75.5,
  "height": 1.75
}
```

4. Click en **"Execute"**
5. Deberías ver la respuesta con el paciente creado

---

## 📋 Endpoints Principales

### **Autenticación (No requieren token):**
- `POST /api/v1/authentication/sign-up` - Registrar usuario
- `POST /api/v1/authentication/sign-in` - Iniciar sesión

### **Pacientes (Requieren token):**
- `GET /api/v1/patients` - Listar todos los pacientes
- `GET /api/v1/patients/{id}` - Obtener paciente por ID
- `POST /api/v1/patients` - Crear nuevo paciente
- `PUT /api/v1/patients/{id}` - Actualizar paciente
- `DELETE /api/v1/patients/{id}` - Eliminar paciente

### **Doctores (Requieren token):**
- `GET /api/v1/doctors` - Listar doctores
- `POST /api/v1/doctors` - Crear doctor

### **Citas (Requieren token):**
- `GET /api/v1/appointments` - Listar citas
- `POST /api/v1/appointments` - Crear cita

### **Tenants (Requieren token):**
- `GET /api/v1/tenants` - Listar organizaciones
- `POST /api/v1/tenants` - Crear organización

---

## 🔍 Verificando que Swagger Funciona

### Método 1: Navegador
Abre: http://localhost:11083/swagger-ui.html

Deberías ver:
- Logo de Swagger
- Título: "chronicare-backend"
- Descripción: "ChroniCare Healthcare Platform - Backend API..."
- Versión: 1.0.0
- Lista de todos los controladores agrupados por contexto

### Método 2: API Docs
Abre: http://localhost:11083/v3/api-docs

Deberías ver un JSON con toda la especificación OpenAPI 3.0

---

## ⚠️ Solución de Problemas

### Problema: "Failed to load remote configuration"
**Solución:** Ya fue corregido. Asegúrate de que:
- La aplicación esté ejecutándose en el puerto 11083
- El filtrado de recursos esté habilitado en `pom.xml`
- Las propiedades de documentación tengan valores por defecto

### Problema: "401 Unauthorized"
**Causa:** No tienes un token válido configurado

**Solución:**
1. Verifica que copiaste el token completo
2. Asegúrate de hacer click en "Authorize" en Swagger
3. Verifica que el token no haya expirado (duran 7 días)
4. Si expiró, vuelve a hacer sign-in

### Problema: "403 Forbidden"
**Causa:** Tu usuario no tiene permisos para ese endpoint

**Solución:**
- Verifica que tu usuario tenga el rol correcto (ROLE_ADMIN, ROLE_DOCTOR, ROLE_PATIENT)
- Algunos endpoints pueden requerir roles específicos

### Problema: No aparece el botón "Authorize"
**Causa:** La configuración de seguridad no está cargando correctamente

**Solución:**
1. Limpia el caché del navegador (Ctrl + Shift + Del)
2. Recarga la página (Ctrl + F5)
3. Verifica que `OpenApiConfiguration.java` esté correctamente configurado

---

## 💡 Consejos Útiles

1. **Mantén el token guardado:** Guarda tu token en un archivo de texto para no tener que autenticarte cada vez
2. **Usa roles apropiados:** Crea usuarios con diferentes roles para probar permisos
3. **Verifica las respuestas:** Lee los códigos de estado HTTP:
   - 200: OK
   - 201: Created
   - 400: Bad Request (datos inválidos)
   - 401: Unauthorized (sin token o token inválido)
   - 403: Forbidden (sin permisos)
   - 404: Not Found
   - 500: Internal Server Error

4. **Explora la documentación:** Swagger genera automáticamente ejemplos de request/response
5. **Prueba con Postman:** También puedes exportar la especificación OpenAPI y usarla en Postman

---

## 📱 Ejemplo Completo de Flujo

```bash
# 1. Registrar usuario
POST http://localhost:11083/api/v1/authentication/sign-up
{
  "email": "doctor@chronicare.com",
  "password": "Doctor123!",
  "name": "Dr. María García",
  "role": "ROLE_DOCTOR",
  "tenantId": 1
}

# 2. Iniciar sesión
POST http://localhost:11083/api/v1/authentication/sign-in
{
  "username": "doctor@chronicare.com",
  "password": "Doctor123!"
}

# Respuesta:
{
  "id": 2,
  "username": "doctor@chronicare.com",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

# 3. Usar el token en Swagger (botón Authorize)
# Pegar el token: eyJhbGciOiJIUzI1NiJ9...

# 4. Crear un paciente
POST http://localhost:11083/api/v1/patients
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
{
  "firstName": "Carlos",
  "lastName": "Rodríguez",
  "dni": "87654321",
  "birthDate": "1985-03-20",
  "gender": "M",
  "phone": "+51912345678",
  "address": "Jr. Lima 456, Cusco",
  "weight": 80.0,
  "height": 1.78
}
```

---

## 🎯 Checklist de Verificación

- [ ] La aplicación está corriendo en http://localhost:11083
- [ ] Swagger UI carga correctamente sin error "Failed to load"
- [ ] Puedo ver todos los endpoints agrupados por contexto
- [ ] Puedo registrar un nuevo usuario exitosamente
- [ ] Puedo iniciar sesión y obtener un token JWT
- [ ] Puedo configurar el token usando el botón "Authorize"
- [ ] Puedo crear un paciente con el token configurado
- [ ] Las respuestas incluyen los datos correctos

---

## 📞 Soporte

Si tienes problemas:
1. Verifica los logs de la aplicación en la terminal
2. Revisa que MySQL esté corriendo
3. Asegúrate de que la base de datos `dbchronicaree_backend` existe
4. Verifica que el puerto 11083 esté disponible

---

**¡Listo! Ahora tienes todo configurado para usar Swagger con autenticación JWT en ChroniCare Backend!** 🎉
