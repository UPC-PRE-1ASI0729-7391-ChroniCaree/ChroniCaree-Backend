# Guía de Implementación IAM (Identity and Access Management)

Esta guía detalla los cambios realizados para implementar un sistema de autenticación y autorización robusto en el backend de ChroniCare, incluyendo Refresh Tokens, Hashing de contraseñas y seguridad basada en roles.

## 1. Resumen de Cambios

Se han implementado los siguientes componentes:

*   **Entidad `RefreshToken`**: Para almacenar tokens de refresco de larga duración en la base de datos.
*   **Repositorio `RefreshTokenRepository`**: Para interactuar con la tabla `refresh_tokens`.
*   **Servicio `RefreshTokenService`**: Lógica de negocio para crear, verificar, rotar y revocar tokens.
*   **Actualización de `UserCommandService`**: Ahora devuelve tanto el Access Token como el Refresh Token al iniciar sesión.
*   **Actualización de `AuthenticationController`**: Nuevos endpoints `/refresh` y `/logout`.
*   **Seguridad**: Habilitación de `@EnableMethodSecurity` para proteger rutas con `@PreAuthorize`.

## 2. Nuevos Endpoints

### 2.1. Refresh Token (`POST /api/v1/authentication/refresh`)

Permite obtener un nuevo Access Token cuando el actual ha expirado, sin que el usuario tenga que volver a iniciar sesión.

**Request Body:**
```json
{
  "refreshToken": "uuid-del-refresh-token"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "user@example.com",
  "token": "nuevo-jwt-access-token",
  "refreshToken": "nuevo-uuid-refresh-token"
}
```
*Nota: Se implementa rotación de tokens. El refresh token usado se revoca y se emite uno nuevo.*

### 2.2. Logout (`POST /api/v1/authentication/logout`)

Cierra la sesión del usuario revocando el refresh token.

**Request Body:**
```json
{
  "refreshToken": "uuid-del-refresh-token"
}
```

**Response (200 OK):**
```json
"Log out successful!"
```

## 3. Configuración de Seguridad

### 3.1. Hashing de Contraseñas
Se utiliza `BCryptPasswordEncoder` para hashear las contraseñas antes de guardarlas en la base de datos. Esto ya estaba configurado pero se ha verificado su uso en `UserCommandServiceImpl`.

### 3.2. Roles y Permisos
Se ha habilitado la seguridad a nivel de método. Ahora puedes proteger tus controladores así:

```java
@PreAuthorize("hasAuthority('DOCTOR')")
@GetMapping("/my-patients")
public ResponseEntity<?> getMyPatients() { ... }
```

Los roles disponibles son: `PATIENT`, `DOCTOR`, `HOSPITAL_ADMIN`.

## 4. Guía para el Frontend

Para integrar el frontend con este sistema IAM, sigue estos pasos:

1.  **Login:**
    *   Llama a `/api/v1/authentication/sign-in`.
    *   Guarda `token` (Access Token) en memoria (estado de la app).
    *   Guarda `refreshToken` en `localStorage` o `HttpOnly Cookie` (preferible).

2.  **Interceptor HTTP (Axios/Fetch):**
    *   En cada petición, añade el header `Authorization: Bearer <token>`.
    *   Si recibes un error `401 Unauthorized`:
        1.  Pausa las peticiones.
        2.  Llama a `/api/v1/authentication/refresh` enviando el `refreshToken` guardado.
        3.  Si es exitoso, actualiza el `token` y el `refreshToken` con los nuevos valores recibidos.
        4.  Reintenta la petición original con el nuevo token.
        5.  Si falla el refresh (ej. token expirado o revocado), redirige al usuario al Login.

3.  **Logout:**
    *   Llama a `/api/v1/authentication/logout` con el `refreshToken`.
    *   Borra los tokens del almacenamiento local.
    *   Redirige al Login.

## 5. Consideraciones de Producción

*   **HTTPS:** Asegúrate de desplegar el backend con SSL/TLS habilitado.
*   **Variables de Entorno:** No hardcodees secretos en `application.properties`. Usa variables de entorno para `authorization.jwt.secret`.
*   **CORS:** Configura `CorsConfigurationSource` en `SecurityConfig` para permitir peticiones solo desde tu dominio de frontend.

---
**Estado Actual:** Implementación completada y lista para pruebas.
