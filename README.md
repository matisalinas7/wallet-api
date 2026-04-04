# 💳 Wallet API - Billetera Virtual

[![Live API / Swagger](https://img.shields.io/badge/Live_API-Swagger_UI-85EA2D?style=for-the-badge&logo=swagger)](https://wallet-api-ysax.onrender.com/swagger-ui/index.html)
[![Java 21](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)]()
[![Spring Boot 3](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)]()
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)]()
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)]()
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)]()

API RESTful robusta para la gestión de una billetera virtual. Permite el registro de usuarios, manejo de cuentas, ingresos de dinero (cash-in) y transferencias entre usuarios. 

El proyecto está diseñado siguiendo buenas prácticas de la industria, con un enfoque fuerte en **seguridad, escalabilidad y código limpio**.

---

## 🚀 Características Principales y Decisiones de Diseño

* **Arquitectura Multicapa:** Separación estricta de responsabilidades (Controllers, Services, Repositories).
* **Seguridad Stateless:** Autenticación y autorización basada en **JSON Web Tokens (JWT)**. Las contraseñas son hasheadas con **BCrypt** antes de persistirse.
* **Patrón DTO (Data Transfer Object):** Aislamiento total entre las entidades de la base de datos y la capa de presentación para evitar vulnerabilidades de *Over-Posting*.
* **Manejo Global de Excepciones:** Uso de `@RestControllerAdvice` para centralizar y estandarizar todas las respuestas de error de la API (formato unificado para el cliente).
* **Validaciones Fail-Fast:** Implementación de Jakarta Validation (`@Valid`, `@NotBlank`, `@Positive`) a nivel de Controller para rechazar peticiones inválidas antes de que consuman recursos.
* **Testing Automatizado:** Pruebas unitarias implementadas con **JUnit 5 y Mockito**, siguiendo el patrón *Arrange-Act-Assert* (AAA) para garantizar la confiabilidad de la lógica de negocio (ej. transacciones de saldo).

## 🛠️ Stack Tecnológico

* **Backend:** Java 21, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate.
* **Base de Datos:** MySQL 8.0.
* **Herramientas:** Maven, Lombok, Jakarta Validation.
* **Infraestructura:** Docker, Docker Compose, Render (Deploy).
* **Documentación:** OpenAPI (Swagger 3).

---

## ⚙️ Instalación y Ejecución Local

Gracias a Docker, levantar el proyecto es extremadamente sencillo. No es necesario instalar Java ni configurar MySQL localmente.

### Requisitos previos
* [Docker](https://www.docker.com/products/docker-desktop/) y [Docker Compose](https://docs.docker.com/compose/install/) instalados.

### Pasos
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/TU_REPOSITORIO.git](https://github.com/TU_USUARIO/TU_REPOSITORIO.git)
2. Navegar al directorio del proyecto:
   ```bash
   cd TU_REPOSITORIO
3. Levantar la infraestructura (API + Base de Datos):
   ```bash
   docker-compose up --build
4. La API estará disponible en http://localhost:8080. Podés acceder a la documentación interactiva en http://localhost:8080/swagger-ui/index.html.
---
## 📡 Endpoints Principales

La documentación completa y de prueba en vivo se encuentra en [Swagger UI](https://wallet-api-ysax.onrender.com/swagger-ui/index.html)..

**Autenticación (`/api/v1/auth`)**
* `POST` `/login`: Genera y devuelve el token JWT.

**Usuarios (`/api/v1/usuarios`)**
* `POST` `/registro`: Crea un nuevo usuario y su cuenta bancaria (Endpoint público).

**Transacciones (`/api/v1/transacciones`)** *(Requieren Token JWT)*
* `POST` `/cashin`: Ingresa dinero a una cuenta.
* `POST` `/transferir`: Realiza una transferencia de saldo entre dos cuentas.
* `GET` `/historial/{identificador}`: Devuelve el historial de movimientos de un usuario ordenado por fecha.

---

## 🧑‍💻 Autor

**Matías Salinas**
* [LinkedIn](https://www.linkedin.com/in/salinasmatias1/)
* [Portfolio](https://salinasmatias.netlify.app/)
