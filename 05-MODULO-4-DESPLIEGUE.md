# Módulo 4 - Despliegue y Buenas Prácticas (5 horas)

## 📋 Objetivos del Módulo
- Documentar API con Swagger/OpenAPI
- Implementar manejo de excepciones y logs
- Aprender sobre Docker y contenedores
- Desplegar aplicación en Render, Railway o AWS
- Completar proyecto final: API Sistema de facturación con seguridad

---

## 1. Documentación con Swagger

### ¿Qué es Swagger?

Swagger (ahora OpenAPI) es una herramienta que genera documentación interactiva de tu API REST automáticamente.

### Ventajas

- ✅ Documentación automática y siempre actualizada
- ✅ Interfaz interactiva para probar la API
- ✅ Especificación estándar (OpenAPI)
- ✅ Fácil de compartir con el equipo

---

## 2. Configurar Swagger en Spring Boot

### Paso 1: Agregar dependencias

En `pom.xml`:

```xml
<dependencies>
    <!-- SpringDoc OpenAPI (Swagger) -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>
```

### Paso 2: Configurar Swagger

```java
package com.tuempresa.facturacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sistema de Facturación")
                        .version("1.0.0")
                        .description("API REST para gestión de facturación con Spring Boot")
                        .contact(new Contact()
                                .name("Tu Nombre")
                                .email("tu-email@ejemplo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")));
    }
}
```

### Paso 3: Anotar controladores

```java
package com.tuempresa.facturacion.controller;

import com.tuempresa.facturacion.model.Producto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
@SecurityRequirement(name = "bearer-jwt")
public class ProductoController {

    @GetMapping
    @Operation(summary = "Obtener todos los productos", 
               description = "Retorna una lista de todos los productos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public List<Producto> obtenerTodos() {
        // ...
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        // ...
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> crear(
            @Parameter(description = "Datos del producto", required = true)
            @RequestBody Producto producto) {
        // ...
    }
}
```

### Paso 4: Acceder a Swagger UI

Una vez que ejecutes la aplicación:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Configuración adicional en application.properties

```properties
# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
```

---

## 3. Manejo de excepciones

### Crear excepciones personalizadas

```java
package com.tuempresa.facturacion.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}

public class EmailYaExisteException extends RuntimeException {
    public EmailYaExisteException(String mensaje) {
        super(mensaje);
    }
}
```

### Crear respuesta de error estándar

```java
package com.tuempresa.facturacion.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
```

### Manejador global de excepciones mejorado

```java
package com.tuempresa.facturacion.exception;

import com.tuempresa.facturacion.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex, WebRequest request) {
        logger.error("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso No Encontrado",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> manejarStockInsuficiente(
            StockInsuficienteException ex, WebRequest request) {
        logger.error("Stock insuficiente: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Stock Insuficiente",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailYaExisteException.class)
    public ResponseEntity<ErrorResponse> manejarEmailYaExiste(
            EmailYaExisteException ex, WebRequest request) {
        logger.error("Email ya existe: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflicto",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex, WebRequest request) {
        logger.error("Error de validación: {}", ex.getMessage());
        
        Map<String, Object> errores = new HashMap<>();
        Map<String, String> campos = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            campos.put(nombreCampo, mensaje);
        });
        
        errores.put("timestamp", java.time.LocalDateTime.now());
        errores.put("status", HttpStatus.BAD_REQUEST.value());
        errores.put("error", "Error de Validación");
        errores.put("message", "Los datos proporcionados no son válidos");
        errores.put("errors", campos);
        errores.put("path", request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> manejarCredencialesInvalidas(
            BadCredentialsException ex, WebRequest request) {
        logger.error("Credenciales inválidas: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "No Autorizado",
                "Usuario o contraseña incorrectos",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccesoDenegado(
            AccessDeniedException ex, WebRequest request) {
        logger.error("Acceso denegado: {}", ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Acceso Denegado",
                "No tienes permisos para acceder a este recurso",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionGeneral(
            Exception ex, WebRequest request) {
        logger.error("Error inesperado: ", ex);
        
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error Interno del Servidor",
                "Ocurrió un error inesperado. Por favor, contacta al administrador.",
                request.getDescription(false).replace("uri=", "")
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

## 4. Logging

### Configurar Logback

Crea `src/main/resources/logback-spring.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    
    <property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}/}spring.log}"/>
    
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>
    
    <logger name="com.tuempresa.facturacion" level="DEBUG"/>
    <logger name="org.springframework.security" level="INFO"/>
    <logger name="org.hibernate" level="WARN"/>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

### Usar Logger en el código

```java
package com.tuempresa.facturacion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    public Producto crear(Producto producto) {
        logger.info("Creando nuevo producto: {}", producto.getNombre());
        
        try {
            Producto guardado = productoRepository.save(producto);
            logger.info("Producto creado exitosamente con ID: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            logger.error("Error al crear producto: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        logger.debug("Intentando eliminar producto con ID: {}", id);
        
        if (!productoRepository.existsById(id)) {
            logger.warn("Intento de eliminar producto inexistente con ID: {}", id);
            throw new RecursoNoEncontradoException("Producto no encontrado");
        }
        
        productoRepository.deleteById(id);
        logger.info("Producto eliminado exitosamente con ID: {}", id);
    }
}
```

### Configurar niveles de log en application.properties

```properties
# Logging Configuration
logging.level.root=INFO
logging.level.com.tuempresa.facturacion=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 5. Docker y contenedores

### ¿Qué es Docker?

Docker permite empaquetar tu aplicación y sus dependencias en un contenedor que puede ejecutarse en cualquier lugar.

### Paso 1: Crear Dockerfile

Crea `Dockerfile` en la raíz del proyecto:

```dockerfile
# Usar imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Paso 2: Crear .dockerignore

Crea `.dockerignore`:

```
target/
.git/
.gitignore
.idea/
.vscode/
*.iml
*.log
.DS_Store
```

### Paso 3: Crear docker-compose.yml

Crea `docker-compose.yml` para incluir PostgreSQL:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: facturacion-postgres
    environment:
      POSTGRES_DB: springboot_db
      POSTGRES_USER: springuser
      POSTGRES_PASSWORD: springpass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - facturacion-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U springuser"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    container_name: facturacion-app
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/springboot_db
      SPRING_DATASOURCE_USERNAME: springuser
      SPRING_DATASOURCE_PASSWORD: springpass
    ports:
      - "8080:8080"
    networks:
      - facturacion-network

volumes:
  postgres_data:

networks:
  facturacion-network:
    driver: bridge
```

### Paso 4: Construir y ejecutar con Docker

```bash
# Construir la imagen
docker build -t facturacion-app .

# Ejecutar con docker-compose
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener
docker-compose down
```

---

## 6. Desplegar en Render

### Paso 1: Preparar la aplicación

1. **Actualizar application.properties para producción**

Crea `application-prod.properties`:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=86400000

server.port=${PORT:8080}
```

2. **Crear script de build**

Crea `build.sh`:

```bash
#!/bin/bash
./mvnw clean package -DskipTests
```

### Paso 2: Crear cuenta en Render

1. Ve a https://render.com/
2. Crea una cuenta (puedes usar GitHub)
3. Conecta tu repositorio de GitHub

### Paso 3: Crear base de datos PostgreSQL

1. En Render Dashboard, haz clic en "New +"
2. Selecciona "PostgreSQL"
3. Configura:
   - Name: `facturacion-db`
   - Database: `springboot_db`
   - User: `springuser`
   - Password: (genera una segura)
4. Anota las credenciales (Render te mostrará la URL de conexión completa)

### Paso 4: Desplegar la aplicación

1. En Render Dashboard, haz clic en "New +"
2. Selecciona "Web Service"
3. Conecta tu repositorio de GitHub
4. Configura:
   - **Name**: `facturacion-api`
   - **Environment**: `Java`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/*.jar --spring.profiles.active=prod`
   - **Instance Type**: Free (o pago según necesites)

5. **Agregar variables de entorno**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:postgresql://tu-host:5432/springboot_db
   DATABASE_USERNAME=springuser
   DATABASE_PASSWORD=tu-password
   JWT_SECRET=tu-clave-secreta-muy-larga-y-segura
   PORT=8080
   ```
   
   **Nota**: Render proporciona una variable `DATABASE_URL` completa. Puedes usarla directamente o extraer los componentes individuales.

6. Haz clic en "Create Web Service"
7. Espera a que se despliegue (5-10 minutos)

---

## 7. Desplegar en Railway

### Paso 1: Crear cuenta

1. Ve a https://railway.app/
2. Crea una cuenta con GitHub

### Paso 2: Crear nuevo proyecto

1. Haz clic en "New Project"
2. Selecciona "Deploy from GitHub repo"
3. Conecta tu repositorio

### Paso 3: Agregar base de datos

1. Haz clic en "New"
2. Selecciona "Database" → "PostgreSQL"
3. Railway creará automáticamente la base de datos PostgreSQL

### Paso 4: Configurar variables de entorno

Railway detecta automáticamente las variables de la base de datos. Agrega:

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=tu-clave-secreta
```

### Paso 5: Desplegar

Railway detecta automáticamente que es una aplicación Java y la despliega.

---

## 8. Desplegar en AWS (básico)

### Opción A: AWS Elastic Beanstalk

1. **Instalar EB CLI**
   ```bash
   pip install awsebcli
   ```

2. **Inicializar EB**
   ```bash
   eb init
   ```

3. **Crear entorno**
   ```bash
   eb create facturacion-env
   ```

4. **Desplegar**
   ```bash
   eb deploy
   ```

### Opción B: AWS EC2

1. **Crear instancia EC2**
2. **Instalar Java, Maven y PostgreSQL**
3. **Subir el JAR**
4. **Ejecutar la aplicación**

---

## 9. Proyecto Final: API Sistema de Facturación Completo

### Objetivos del proyecto

Crear un sistema completo de facturación con todas las funcionalidades aprendidas:

1. **Gestión de Usuarios**
   - Registro y login con JWT
   - Roles (USER, ADMIN)
   - Perfil de usuario

2. **Gestión de Clientes**
   - CRUD completo
   - Validaciones
   - Endpoints protegidos

3. **Gestión de Productos**
   - CRUD completo
   - Control de stock
   - Búsqueda y filtros

4. **Gestión de Facturas**
   - Crear facturas con detalles
   - Calcular totales
   - Actualizar stock automáticamente
   - Listar facturas por cliente

5. **Documentación**
   - Swagger/OpenAPI completo
   - Todos los endpoints documentados

6. **Manejo de errores**
   - Excepciones personalizadas
   - Respuestas de error estándar
   - Logging apropiado

7. **Despliegue**
   - Dockerfile configurado
   - docker-compose.yml
   - Desplegado en Render/Railway

### Estructura completa del proyecto

```
src/main/java/com/tuempresa/facturacion/
├── FacturacionApplication.java
├── config/
│   ├── SwaggerConfig.java
│   └── DataInitializer.java
├── controller/
│   ├── AuthController.java
│   ├── ClienteController.java
│   ├── ProductoController.java
│   └── FacturaController.java
├── service/
│   ├── AuthService.java
│   ├── ClienteService.java
│   ├── ProductoService.java
│   └── FacturaService.java
├── repository/
│   ├── UsuarioRepository.java
│   ├── RolRepository.java
│   ├── ClienteRepository.java
│   ├── ProductoRepository.java
│   └── FacturaRepository.java
├── model/
│   ├── Usuario.java
│   ├── Rol.java
│   ├── ERol.java
│   ├── Cliente.java
│   ├── Producto.java
│   ├── Factura.java
│   └── DetalleFactura.java
├── dto/
│   ├── LoginRequest.java
│   ├── RegistroRequest.java
│   ├── JwtResponse.java
│   └── ErrorResponse.java
├── security/
│   ├── SecurityConfig.java
│   ├── UserDetailsServiceImpl.java
│   └── jwt/
│       ├── JwtTokenProvider.java
│       └── JwtAuthenticationFilter.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── RecursoNoEncontradoException.java
    ├── StockInsuficienteException.java
    └── EmailYaExisteException.java
```

### Criterios de evaluación

- [ ] Todas las funcionalidades implementadas
- [ ] Seguridad con JWT funcionando
- [ ] Roles implementados correctamente
- [ ] Swagger documentado completamente
- [ ] Manejo de excepciones implementado
- [ ] Logging configurado
- [ ] Dockerfile y docker-compose.yml creados
- [ ] Aplicación desplegada en producción
- [ ] Código limpio y bien documentado
- [ ] README completo con instrucciones

---

## 10. Crear README.md para el proyecto

```markdown
# Sistema de Facturación - API REST

API REST desarrollada con Spring Boot para gestión de facturación.

## 🚀 Características

- Autenticación y autorización con JWT
- Gestión de usuarios con roles
- CRUD completo de clientes, productos y facturas
- Documentación con Swagger
- Manejo de excepciones
- Logging
- Dockerizado
- Desplegado en producción

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 15 o superior
- Docker (opcional)

## 🛠️ Instalación

### Local

1. Clonar el repositorio
2. Configurar `application.properties` con tus credenciales de PostgreSQL
3. Ejecutar: `./mvnw spring-boot:run`

### Docker

```bash
docker-compose up -d
```

## 📚 Documentación

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

## 🔐 Endpoints principales

- `POST /api/auth/registro` - Registrar usuario
- `POST /api/auth/login` - Iniciar sesión
- `GET /api/productos` - Listar productos
- `POST /api/facturas` - Crear factura

## 🚢 Despliegue

La aplicación está desplegada en: [URL de producción]

## 👥 Autores

- Tu Nombre

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.
```

---

## 11. Buenas prácticas finales

### 1. Seguridad
- ✅ Nunca commitees credenciales
- ✅ Usa variables de entorno
- ✅ HTTPS en producción
- ✅ Tokens con expiración

### 2. Código
- ✅ Código limpio y legible
- ✅ Comentarios donde sea necesario
- ✅ Nombres descriptivos
- ✅ Separación de responsabilidades

### 3. Testing
- ✅ Pruebas unitarias
- ✅ Pruebas de integración
- ✅ Pruebas de endpoints

### 4. Documentación
- ✅ README completo
- ✅ Swagger actualizado
- ✅ Comentarios en código complejo

---

## 12. Recursos adicionales

- **Swagger**: https://swagger.io/
- **Docker**: https://www.docker.com/
- **Render**: https://render.com/
- **Railway**: https://railway.app/
- **AWS**: https://aws.amazon.com/

---

## ❓ Preguntas frecuentes

### ¿Cómo cambio el puerto en producción?
- Usa la variable de entorno `PORT`
- O configura en `application-prod.properties`

### ¿Cómo manejo las credenciales en producción?
- Usa variables de entorno
- Nunca las pongas en el código
- Usa servicios de gestión de secretos

### ¿Cómo actualizo la aplicación desplegada?
- Haz push a GitHub
- La plataforma detectará los cambios y redesplegará automáticamente

---

## 🎯 Checklist del Módulo 4

- [ ] Swagger configurado y documentado
- [ ] Manejo de excepciones implementado
- [ ] Logging configurado
- [ ] Dockerfile creado
- [ ] docker-compose.yml creado
- [ ] Aplicación desplegada en Render/Railway
- [ ] Proyecto final completado
- [ ] README creado
- [ ] Documentación completa

---

## 🎓 Conclusión del Curso

¡Felicidades! Has completado el curso completo de Spring Boot. Ahora tienes:

- ✅ Conocimiento sólido de Spring Boot
- ✅ Experiencia con JPA y PostgreSQL
- ✅ Implementación de seguridad con JWT
- ✅ Documentación de APIs
- ✅ Despliegue en producción
- ✅ Proyecto completo funcional

### Próximos pasos

1. Continúa practicando
2. Explora más funcionalidades de Spring Boot
3. Aprende sobre microservicios
4. Estudia Spring Cloud
5. Contribuye a proyectos open source

---

**¡Felicidades por completar el curso!** 🎉🚀

**Has construido un sistema completo de facturación desde cero hasta producción.**

