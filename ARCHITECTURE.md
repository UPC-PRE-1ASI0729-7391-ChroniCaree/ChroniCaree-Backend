# ChroniCare Backend - Arquitectura DDD implementada

## 📐 Estructura actual del proyecto

```
com.chronicare.platform.platform/
│
├── shared/                                    ✅ IMPLEMENTADO
│   ├── domain/
│   │   └── model/
│   │       ├── aggregates/
│   │       │   └── AuditableAbstractAggregateRoot.java
│   │       └── entities/
│   │           └── AuditableModel.java
│   ├── infrastructure/
│   │   ├── documentation/openapi/
│   │   │   └── configuration/
│   │   │       └── OpenApiConfiguration.java
│   │   └── persistence/jpa/
│   │       └── configuration/strategy/
│   │           └── SnakeCaseWithPluralizedTablePhysicalNamingStrategy.java
│   └── interfaces/rest/
│       └── resources/
│           └── MessageResource.java
│
├── iam/                                       ✅ IMPLEMENTADO (COMPLETO)
│   ├── domain/
│   │   ├── model/
│   │   │   ├── aggregates/
│   │   │   │   └── User.java
│   │   │   ├── commands/
│   │   │   │   ├── RegisterUserCommand.java
│   │   │   │   └── UpdateUserCommand.java
│   │   │   ├── queries/
│   │   │   │   ├── GetAllUsersQuery.java
│   │   │   │   ├── GetUserByIdQuery.java
│   │   │   │   ├── GetUserByEmailQuery.java
│   │   │   │   └── GetUsersByRoleQuery.java
│   │   │   ├── valueobjects/
│   │   │   │   ├── Roles.java
│   │   │   │   └── EmailAddress.java
│   │   │   └── repositories/
│   │   │       └── UserRepository.java
│   │   └── services/
│   │       ├── UserCommandService.java
│   │       └── UserQueryService.java
│   ├── application/internal/
│   │   ├── commandservices/
│   │   │   └── UserCommandServiceImpl.java
│   │   └── queryservices/
│   │       └── UserQueryServiceImpl.java
│   └── interfaces/rest/
│       ├── resources/
│       │   ├── UserResource.java
│       │   ├── CreateUserResource.java
│       │   └── UpdateUserResource.java
│       ├── transform/
│       │   ├── RegisterUserCommandFromResourceAssembler.java
│       │   ├── UpdateUserCommandFromResourceAssembler.java
│       │   └── UserResourceFromEntityAssembler.java
│       └── UsersController.java
│
├── tenants/                                   ⚠️ PENDIENTE REFACTORIZAR
│   └── (usar patrón IAM como referencia)
│
├── patients/                                  ⚠️ PENDIENTE REFACTORIZAR
│   └── (usar patrón IAM como referencia)
│
├── doctors/                                   ⚠️ PENDIENTE REFACTORIZAR
│   └── (usar patrón IAM como referencia)
│
└── [otros bounded contexts...]               ⚠️ PENDIENTE REFACTORIZAR
```

## 🎯 Patrón implementado (Learning-Center)

### Capas por Bounded Context

```
┌─────────────────────────────────────────────────────┐
│              INTERFACES LAYER (REST)                │
│  - Controllers (endpoints REST)                     │
│  - Resources (DTOs)                                 │
│  - Assemblers (conversión DTO ↔ Domain)            │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│           APPLICATION LAYER (Use Cases)             │
│  - CommandService (escritura)                       │
│  - QueryService (lectura)                           │
│  - CQRS separation                                  │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│              DOMAIN LAYER (Core)                    │
│  - Aggregates (raíz de entidades)                   │
│  - Entities                                         │
│  - Value Objects                                    │
│  - Commands (escritura)                             │
│  - Queries (lectura)                                │
│  - Domain Services (interfaces)                     │
│  - Repository Interfaces                            │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│          INFRASTRUCTURE LAYER (Adapters)            │
│  - JPA Repository Implementations                   │
│  - External Service Adapters                        │
│  - Configuration                                    │
└─────────────────────────────────────────────────────┘
```

## 🔄 Flujo de una petición (ejemplo: crear usuario)

```
1. POST /api/v1/users
   ↓
2. UsersController.createUser(CreateUserResource)
   ↓
3. RegisterUserCommandFromResourceAssembler
   → convierte DTO a RegisterUserCommand
   ↓
4. UserCommandService.handle(RegisterUserCommand)
   ↓
5. UserCommandServiceImpl:
   - Valida email único
   - Crea nuevo User aggregate
   - Guarda en UserRepository
   ↓
6. UserResourceFromEntityAssembler
   → convierte User a UserResource (DTO)
   ↓
7. Response 201 Created con UserResource
```

## 📊 Comparación: Antes vs Después

### ❌ Antes (incorrecto)
```
com.example/
├── users/
│   ├── User.java                    // Entidad anémica
│   ├── UserService.java             // Lógica mezclada
│   └── UserController.java          // Retorna entidades directamente
```
**Problemas:**
- No hay separación de capas
- Entidades expuestas directamente en API
- Sin CQRS
- Sin value objects
- Sin comandos/queries explícitos

### ✅ Después (correcto - DDD + CQRS)
```
com.chronicare.platform.iam/
├── domain/
│   ├── model/
│   │   ├── aggregates/User.java       // Aggregate con lógica de negocio
│   │   ├── commands/                  // Intenciones claras
│   │   ├── queries/                   // Separación lectura
│   │   └── valueobjects/              // Encapsulación
│   └── services/                      // Interfaces de dominio
├── application/                       // Casos de uso
└── interfaces/                        // DTOs + Controllers
```
**Beneficios:**
- ✅ Separación clara de responsabilidades
- ✅ CQRS (comando/query)
- ✅ Aggregate con lógica de dominio
- ✅ Value objects con validación
- ✅ DTOs para API (no exponer entidades)
- ✅ Assemblers para transformación
- ✅ Base `shared` reutilizable

## 🗃️ Tabla de BD generada (users)

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    tenant_id BIGINT,
    is_verified BOOLEAN DEFAULT FALSE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    created_date TIMESTAMP,           -- AuditableModel
    last_modified_date TIMESTAMP      -- AuditableModel
);
```

## 🚀 Testing rápido

### 1. Verificar compilación
```powershell
mvn clean compile
```

### 2. Arrancar aplicación
```powershell
mvn spring-boot:run
```

### 3. Probar endpoint (crear usuario)
```powershell
curl -X POST http://localhost:8080/api/v1/users `
  -H "Content-Type: application/json" `
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "name": "Test User",
    "role": "patient",
    "tenantId": null
  }'
```

### 4. Ver OpenAPI docs
```
http://localhost:8080/swagger-ui.html
```

## 📋 Próximas tareas

1. **Hash passwords** (urgente para seguridad)
2. **Crear AuthenticationController** (login/register)
3. **Refactorizar `tenants` bounded context** usando patrón IAM
4. **Refactorizar `patients` bounded context** usando patrón IAM
5. **Refactorizar `doctors` bounded context** usando patrón IAM
6. **Configurar Spring Security + JWT**
7. **Agregar tests unitarios**

## 🎓 Referencias DDD aplicadas

- **Aggregate Root**: `User` extiende `AuditableAbstractAggregateRoot`
- **Value Objects**: `EmailAddress`, `Roles`
- **Ubiquitous Language**: Comandos y queries con nombres de dominio
- **CQRS**: Separación clara entre escritura (commands) y lectura (queries)
- **Repository Pattern**: Interfaz en dominio, implementación en infraestructura
- **Assemblers/Mappers**: Transformación entre capas sin acoplar
- **Bounded Context**: `iam` es autónomo y cohesivo

---

**Estado actual**: ✅ Shared layer + IAM bounded context completamente implementados siguiendo Learning-Center como guía canónica.
