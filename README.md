
Cada módulo incluye:
- `domain/model` → Entidades JPA
- `domain/repositories` → Interfaces de acceso a datos
- `application/services` → Lógica de negocio
- `infrastructure/persistence` → Adaptadores a la capa de datos
- `interfaces/rest` → Controladores REST con Swagger

---

## ⚙️ Tecnologías

| Componente | Versión / Herramienta |
|-------------|-----------------------|
| Java | 17+ |
| Spring Boot | 3.x |
| Spring Data JPA | ✅ |
| Spring Web | ✅ |
| Spring Security (próximamente) | 🔒 |
| MariaDB | 🐬 |
| JJWT (próximamente) | 🔑 |
| Swagger / SpringDoc OpenAPI | 📘 |

---

## 🧩 Módulos Implementados

### 🏢 Tenants
Gestión de instituciones médicas o clínicas.
- **Endpoints**
  - `GET /api/v1/tenants` — listar todos
  - `GET /api/v1/tenants/{id}` — obtener por ID
  - `POST /api/v1/tenants` — crear tenant (Permite acceso público para registro)
  - `PUT /api/v1/tenants/{id}` — actualizar
  - `DELETE /api/v1/tenants/{id}` — eliminar

#### Frontend Integration (Create Tenant)
El endpoint `POST /api/v1/tenants` espera el siguiente payload JSON:
```json
{
  "adminUserId": 1,
  "name": "Hospital Central",
  "email": "contact@hospital.com",
  "address": "Av. Principal 123",
  "phone": "+51 999 999 999",
  "status": "pending_subscription",
  "subscriptionId": null,
  "registrationDate": "2025-11-27T20:38:28.256Z",
  "settings": {
    "allowIndependentDoctors": false,
    "requirePatientApproval": true,
    "maxDoctors": 5
  }
}
```
**Nota:** La fecha `registrationDate` debe enviarse en formato ISO 8601.

### 👤 Users
Usuarios del sistema (pacientes, doctores, administradores de hospital).
- **Endpoints**
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `POST /api/users`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`

### 🧑‍⚕️ Doctors
Profesionales médicos asociados a usuarios o instituciones.
- **Endpoints**
  - `GET /api/doctors`
  - `GET /api/doctors/{id}`
  - `POST /api/doctors`
  - `PUT /api/doctors/{id}`
  - `DELETE /api/doctors/{id}`

---

## 🧰 Configuración

### Archivo `application.properties`
Ejemplo de configuración para MariaDB:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/chronicare
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
