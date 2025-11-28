# Reporte de Incidencia: Manejo de Error 404 en Login

## Descripción del Problema
El Backend está respondiendo con un código de estado HTTP **404 Not Found** cuando las credenciales de inicio de sesión son incorrectas (usuario no encontrado o contraseña inválida).

Actualmente, el Frontend está interpretando este error 404 como un "Unknown Error" o un fallo inesperado en el flujo de registro/login, lo que interrumpe la experiencia del usuario.

## Logs Observados
```
POST http://localhost:11083/api/v1/authentication/sign-in 404 (Not Found)
HttpErrorResponse {
  status: 404, 
  statusText: 'Unknown Error', 
  ...
}
```

## Solución Recomendada para el Equipo de Frontend
Por favor, actualicen el servicio de autenticación o el componente de login (`register-hospital.ts`) para manejar explícitamente el error **404** en el endpoint `/sign-in`.

**Comportamiento esperado:**
1.  Si el Backend devuelve **404**, mostrar un mensaje al usuario: *"Correo electrónico o contraseña incorrectos."*
2.  Evitar que este error sea interceptado como un fallo crítico del sistema.
3.  Verificar que el payload enviado en el POST coincida con lo esperado (email y password correctos).

## Nota Técnica
El Backend está configurado para devolver `404` en lugar de `500` para credenciales inválidas. Esto es un comportamiento correcto y seguro. El Frontend debe adaptarse a esta respuesta.
