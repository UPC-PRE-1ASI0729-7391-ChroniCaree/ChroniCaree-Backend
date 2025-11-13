
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
  - `GET /api/tenants` — listar todos
  - `GET /api/tenants/{id}` — obtener por ID
  - `POST /api/tenants` — crear tenant
  - `PUT /api/tenants/{id}` — actualizar
  - `DELETE /api/tenants/{id}` — eliminar

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
