# IAM Bounded Context - Implementation Guide

## 📋 Overview
El bounded context **IAM (Identity and Access Management)** gestiona usuarios, autenticación y autorización siguiendo el patrón **DDD + CQRS** del proyecto Learning-Center.

## 🏗️ Arquitectura

### Estructura de carpetas
```
iam/
├── domain/
│   ├── model/
│   │   ├── aggregates/
│   │   │   └── User.java                    # Aggregate root
│   │   ├── commands/
│   │   │   ├── RegisterUserCommand.java
│   │   │   └── UpdateUserCommand.java
│   │   ├── queries/
│   │   │   ├── GetAllUsersQuery.java
│   │   │   ├── GetUserByIdQuery.java
│   │   │   ├── GetUserByEmailQuery.java
│   │   │   └── GetUsersByRoleQuery.java
│   │   ├── valueobjects/
│   │   │   ├── Roles.java                   # Enum: PATIENT, DOCTOR, HOSPITAL_ADMIN
│   │   │   └── EmailAddress.java            # Value object
│   │   └── repositories/
│   │       └── UserRepository.java          # Repository interface
│   └── services/
│       ├── UserCommandService.java
│       └── UserQueryService.java
├── application/
│   └── internal/
│       ├── commandservices/
│       │   └── UserCommandServiceImpl.java
│       └── queryservices/
│           └── UserQueryServiceImpl.java
└── interfaces/
    └── rest/
        ├── resources/
        │   ├── UserResource.java
        │   ├── CreateUserResource.java
        │   └── UpdateUserResource.java
        ├── transform/
        │   ├── RegisterUserCommandFromResourceAssembler.java
        │   ├── UpdateUserCommandFromResourceAssembler.java
        │   └── UserResourceFromEntityAssembler.java
        └── UsersController.java
```

## 🎯 Características principales

### Value Objects
- **Roles**: `PATIENT`, `DOCTOR`, `HOSPITAL_ADMIN`
- **EmailAddress**: Encapsula validación de email con `@Embeddable`

### User Aggregate
Extiende `AuditableAbstractAggregateRoot<User>` (del `shared` layer) y proporciona:
- Registro de usuarios con validación de email único
- Verificación de email (`verify()`)
- Gestión de 2FA (`enableTwoFactor()`, `disableTwoFactor()`)
- Actualización de perfil
- Cambio de password (debe recibir hash)

### Comandos
- `RegisterUserCommand`: Registrar nuevo usuario
- `UpdateUserCommand`: Actualizar perfil (nombre, verificación, 2FA)

### Queries
- `GetAllUsersQuery`: Obtener todos los usuarios
- `GetUserByIdQuery`: Buscar usuario por ID
- `GetUserByEmailQuery`: Buscar usuario por email
- `GetUsersByRoleQuery`: Filtrar usuarios por rol

### Servicios
Separación CQRS:
- **UserCommandService**: Maneja comandos (create, update, verify, delete)
- **UserQueryService**: Maneja consultas (findAll, findById, findByEmail, findByRole)

## 🌐 API Endpoints

### Base URL: `/api/v1/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Crear nuevo usuario |
| GET | `/api/v1/users` | Obtener todos los usuarios |
| GET | `/api/v1/users/{userId}` | Obtener usuario por ID |
| GET | `/api/v1/users/email/{email}` | Obtener usuario por email |
| GET | `/api/v1/users/role/{role}` | Filtrar usuarios por rol |
| PUT | `/api/v1/users/{userId}` | Actualizar perfil de usuario |
| PATCH | `/api/v1/users/{userId}/verify` | Verificar usuario |
| DELETE | `/api/v1/users/{userId}` | Eliminar usuario |

## 📝 Ejemplo de uso

### Crear usuario (POST `/api/v1/users`)
```json
{
  "email": "doctor@hospital.com",
  "password": "securePassword123",
  "name": "Dr. Juan Pérez",
  "role": "doctor",
  "tenantId": 1
}
```

### Respuesta (UserResource)
```json
{
  "id": 1,
  "email": "doctor@hospital.com",
  "name": "Dr. Juan Pérez",
  "role": "doctor",
  "tenantId": 1,
  "isVerified": false,
  "twoFactorEnabled": false,
  "createdAt": "2025-11-25T10:30:00"
}
```

### Actualizar usuario (PUT `/api/v1/users/1`)
```json
{
  "name": "Dr. Juan Alberto Pérez",
  "isVerified": true,
  "twoFactorEnabled": false
}
```

## 🔐 Seguridad (Próximos pasos)

> **IMPORTANTE**: Actualmente el password se guarda sin hashear. Para producción:

1. **Hash de passwords**: Implementar BCryptPasswordEncoder en `UserCommandServiceImpl`:
```java
@Autowired
private PasswordEncoder passwordEncoder;

// En handle(RegisterUserCommand):
var hashedPassword = passwordEncoder.encode(command.password());
var user = new User(new RegisterUserCommand(
    command.email(),
    hashedPassword,  // Password hasheado
    command.name(),
    command.role(),
    command.tenantId()
));
```

2. **JWT Authentication**: Crear `AuthenticationController` con endpoints:
   - `POST /api/v1/auth/login` → retorna JWT token
   - `POST /api/v1/auth/register` → registro público
   - `POST /api/v1/auth/refresh` → refrescar token

3. **Spring Security Configuration**: Configurar filtros JWT y proteger endpoints por rol.

## 🗄️ Base de datos

### Tabla generada: `users`
Columnas principales:
- `id` (BIGINT, PK)
- `email` (VARCHAR, UNIQUE)
- `password` (VARCHAR)
- `name` (VARCHAR)
- `role` (VARCHAR/ENUM)
- `tenant_id` (BIGINT, nullable)
- `is_verified` (BOOLEAN, default false)
- `two_factor_enabled` (BOOLEAN, default false)
- `created_at` (TIMESTAMP)
- `created_date`, `last_modified_date` (heredados de AuditableModel)

## 🔄 Integración con frontend

Tu frontend usa:
```typescript
usersEndpointPath: '/users',
authEndpointPath: '/auth',
```

**Mapeo actual**:
- ✅ `/api/v1/users` → todos los endpoints CRUD funcionan
- ⚠️ `/api/v1/auth` → **pendiente de implementar** (login, register, refresh)

## ✅ Checklist de implementación

- [x] Value objects (Roles, EmailAddress)
- [x] Commands y Queries
- [x] User Aggregate extendiendo AuditableAbstractAggregateRoot
- [x] UserRepository con métodos JPA
- [x] UserCommandService + UserQueryService (interfaces + implementaciones)
- [x] Resources (DTOs) y Assemblers
- [x] UsersController con todos los endpoints CRUD
- [ ] Hash de passwords con BCrypt
- [ ] AuthenticationController (login/register/refresh)
- [ ] Spring Security + JWT configuration
- [ ] Tests unitarios e integración

## 🚀 Próximos pasos recomendados

1. **Implementar hash de passwords** (5 min)
2. **Crear AuthenticationController** para login/register (20 min)
3. **Configurar Spring Security con JWT** (30 min)
4. **Refactorizar otros bounded contexts** (patients, doctors, tenants) para usar el mismo patrón DDD

## 📚 Referencias
- Learning-Center Platform (proyecto de referencia)
- Shared layer: `com.chronicare.platform.shared`
- OpenAPI docs: http://localhost:8080/swagger-ui.html (una vez corriendo)
