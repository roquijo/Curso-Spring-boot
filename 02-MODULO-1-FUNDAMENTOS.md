# Módulo 1 - Fundamentos de Spring Boot (5 horas)

## 📋 Objetivos del Módulo
- Comprender el ecosistema Spring
- Entender la arquitectura backend profesional
- Aprender sobre controladores, servicios y repositorios
- Crear tu primera API REST
- Desarrollar un mini proyecto: gestión de productos

---

## 1. Introducción al ecosistema Spring

### ¿Qué es Spring Framework?

Spring es un framework de Java que simplifica el desarrollo de aplicaciones empresariales. Spring Boot es una extensión de Spring que facilita aún más la creación de aplicaciones.

### Ventajas de Spring Boot

- ✅ **Configuración automática**: Menos configuración manual
- ✅ **Servidor embebido**: No necesitas instalar Tomcat
- ✅ **Producción lista**: Incluye métricas, health checks, etc.
- ✅ **Convención sobre configuración**: Funciona con valores por defecto inteligentes

### Componentes principales

1. **Spring Core**: Inyección de dependencias
2. **Spring MVC**: Para crear aplicaciones web
3. **Spring Data**: Para trabajar con bases de datos
4. **Spring Security**: Para seguridad y autenticación
5. **Spring Boot**: Todo lo anterior simplificado

---

## 2. Arquitectura backend profesional

### Patrón de arquitectura en capas

```
┌─────────────────────────────────────┐
│      Controladores (Controllers)    │  ← Reciben peticiones HTTP
│         @RestController             │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│        Servicios (Services)          │  ← Lógica de negocio
│          @Service                    │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      Repositorios (Repositories)    │  ← Acceso a datos
│         @Repository                 │
└─────────────────────────────────────┘
```

### Responsabilidades de cada capa

#### Controladores (Controllers)
- Reciben peticiones HTTP
- Validan los datos de entrada
- Llaman a los servicios
- Retornan respuestas HTTP

#### Servicios (Services)
- Contienen la lógica de negocio
- Coordinan entre repositorios
- Aplican reglas de negocio
- No conocen detalles de HTTP

#### Repositorios (Repositories)
- Acceden a la base de datos
- Realizan operaciones CRUD
- Abstraen el acceso a datos

---

## 3. Crear tu primer proyecto Spring Boot

### Opción A: Usando Spring Initializr (Web)

1. **Visita Spring Initializr**
   - Ve a: https://start.spring.io/

2. **Configura el proyecto**
   - **Project**: Maven
   - **Language**: Java
   - **Spring Boot**: 3.1.x (o la última versión estable)
   - **Group**: `com.tuempresa`
   - **Artifact**: `mi-primer-api`
   - **Name**: `mi-primer-api`
   - **Package name**: `com.tuempresa.miprimerapi`
   - **Packaging**: Jar
   - **Java**: 17

3. **Agregar dependencias**
   - **Spring Web** (para crear APIs REST)
   - **Spring Boot DevTools** (herramientas de desarrollo)

4. **Generar y descargar**
   - Haz clic en "Generate"
   - Descarga el archivo ZIP
   - Extrae y abre en tu IDE

### Opción B: Usando IntelliJ IDEA

1. **Nuevo proyecto**
   - File → New → Project
   - Selecciona "Spring Initializr"
   - Configura igual que en la opción web
   - Selecciona dependencias: Spring Web, DevTools
   - Finish

### Opción C: Usando VS Code

1. **Instalar extensión**
   - Instala "Spring Initializr Java Support"

2. **Crear proyecto**
   - Ctrl+Shift+P → "Spring Initializr: Generate a Maven Project"
   - Sigue el asistente

---

## 4. Estructura del proyecto

Después de crear el proyecto, verás esta estructura:

```
mi-primer-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── tuempresa/
│   │   │           └── miprimerapi/
│   │   │               └── MiPrimerApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml (Maven) o build.gradle (Gradle)
└── README.md
```

### Archivos importantes

- **MiPrimerApiApplication.java**: Clase principal con `@SpringBootApplication`
- **application.properties**: Configuración de la aplicación
- **pom.xml**: Dependencias del proyecto (Maven)

---

## 5. Tu primera API REST

### Paso 1: Crear un controlador simple

Crea la clase `HolaController.java`:

```java
package com.tuempresa.miprimerapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

    @GetMapping("/hola")
    public String saludar() {
        return "¡Hola desde Spring Boot!";
    }
}
```

### Paso 2: Ejecutar la aplicación

1. **Desde IntelliJ IDEA**
   - Click derecho en `MiPrimerApiApplication.java`
   - Run 'MiPrimerApiApplication'

2. **Desde terminal**
   ```bash
   ./mvnw spring-boot:run
   # O en Windows:
   mvnw.cmd spring-boot:run
   ```

3. **Probar la API**
   - Abre tu navegador en: http://localhost:8080/hola
   - O usa Postman: GET http://localhost:8080/hola

### Explicación del código

- `@RestController`: Marca la clase como controlador REST
- `@GetMapping("/hola")`: Mapea peticiones GET a `/hola`
- El método retorna un String que se convierte automáticamente en JSON

---

## 6. Controladores, Servicios y Repositorios

### Ejemplo completo: API de Productos

#### Paso 1: Crear la entidad (Modelo)

```java
package com.tuempresa.miprimerapi.model;

public class Producto {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;

    // Constructores
    public Producto() {}

    public Producto(Long id, String nombre, Double precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
```

#### Paso 2: Crear el Repositorio

```java
package com.tuempresa.miprimerapi.repository;

import com.tuempresa.miprimerapi.model.Producto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository {
    
    private List<Producto> productos = new ArrayList<>();
    private Long nextId = 1L;

    // Simular base de datos en memoria
    public ProductoRepository() {
        // Datos de ejemplo
        productos.add(new Producto(1L, "Laptop", 999.99, 10));
        productos.add(new Producto(2L, "Mouse", 29.99, 50));
    }

    public List<Producto> findAll() {
        return productos;
    }

    public Optional<Producto> findById(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(nextId++);
            productos.add(producto);
        } else {
            // Actualizar producto existente
            productos.removeIf(p -> p.getId().equals(producto.getId()));
            productos.add(producto);
        }
        return producto;
    }

    public void deleteById(Long id) {
        productos.removeIf(p -> p.getId().equals(id));
    }
}
```

#### Paso 3: Crear el Servicio

```java
package com.tuempresa.miprimerapi.service;

import com.tuempresa.miprimerapi.model.Producto;
import com.tuempresa.miprimerapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        // Validaciones de negocio
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        Optional<Producto> existente = productoRepository.findById(id);
        if (existente.isPresent()) {
            producto.setId(id);
            return productoRepository.save(producto);
        }
        throw new RuntimeException("Producto no encontrado");
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}
```

#### Paso 4: Crear el Controlador

```java
package com.tuempresa.miprimerapi.controller;

import com.tuempresa.miprimerapi.model.Producto;
import com.tuempresa.miprimerapi.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET /api/productos - Obtener todos los productos
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    // GET /api/productos/{id} - Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/productos - Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        try {
            Producto nuevo = productoService.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /api/productos/{id} - Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/productos/{id} - Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 7. Anotaciones importantes

### Anotaciones de Spring

| Anotación | Uso | Descripción |
|-----------|-----|-------------|
| `@RestController` | Clase | Marca un controlador REST |
| `@Controller` | Clase | Controlador MVC tradicional |
| `@Service` | Clase | Marca un servicio |
| `@Repository` | Clase | Marca un repositorio |
| `@Component` | Clase | Componente genérico de Spring |
| `@Autowired` | Campo/Método | Inyección de dependencias |
| `@RequestMapping` | Clase/Método | Mapea URLs |
| `@GetMapping` | Método | Mapea peticiones GET |
| `@PostMapping` | Método | Mapea peticiones POST |
| `@PutMapping` | Método | Mapea peticiones PUT |
| `@DeleteMapping` | Método | Mapea peticiones DELETE |
| `@PathVariable` | Parámetro | Extrae variable de la URL |
| `@RequestBody` | Parámetro | Convierte JSON a objeto |
| `@RequestParam` | Parámetro | Extrae parámetro de query |

---

## 8. Probar la API con Postman

### Endpoints creados

1. **GET** `http://localhost:8080/api/productos`
   - Obtiene todos los productos

2. **GET** `http://localhost:8080/api/productos/1`
   - Obtiene el producto con ID 1

3. **POST** `http://localhost:8080/api/productos`
   - Crea un nuevo producto
   - Body (JSON):
     ```json
     {
       "nombre": "Teclado",
       "precio": 49.99,
       "stock": 30
     }
     ```

4. **PUT** `http://localhost:8080/api/productos/1`
   - Actualiza el producto con ID 1
   - Body (JSON):
     ```json
     {
       "nombre": "Laptop Pro",
       "precio": 1299.99,
       "stock": 5
     }
     ```

5. **DELETE** `http://localhost:8080/api/productos/1`
   - Elimina el producto con ID 1

---

## 9. Mini Proyecto: Gestión de Productos

### Objetivos del proyecto

Crear una API REST completa para gestionar productos con las siguientes funcionalidades:

1. ✅ Listar todos los productos
2. ✅ Obtener un producto por ID
3. ✅ Crear un nuevo producto
4. ✅ Actualizar un producto existente
5. ✅ Eliminar un producto
6. ✅ Buscar productos por nombre (opcional)
7. ✅ Filtrar productos por precio mínimo (opcional)

### Estructura del proyecto

```
src/main/java/com/tuempresa/productos/
├── ProductosApplication.java
├── controller/
│   └── ProductoController.java
├── service/
│   └── ProductoService.java
├── repository/
│   └── ProductoRepository.java
└── model/
    └── Producto.java
```

### Criterios de evaluación

- [ ] Código organizado en capas (Controller, Service, Repository)
- [ ] Uso correcto de anotaciones de Spring
- [ ] Endpoints REST funcionando correctamente
- [ ] Validaciones básicas en el servicio
- [ ] Manejo de errores (404, 400)
- [ ] Código limpio y comentado

---

## 10. Configuración adicional

### Cambiar el puerto

En `application.properties`:

```properties
server.port=8081
```

### Configurar contexto de la aplicación

```properties
server.servlet.context-path=/api
```

Ahora todas las URLs serán: `http://localhost:8081/api/...`

### Logging

```properties
# Nivel de logging
logging.level.com.tuempresa.miprimerapi=DEBUG
logging.level.org.springframework.web=INFO
```

---

## 11. Buenas prácticas

### 1. Nombres descriptivos
```java
// ✅ Bien
public List<Producto> obtenerTodosLosProductos()

// ❌ Mal
public List<Producto> get()
```

### 2. Separación de responsabilidades
- Controladores: Solo manejan HTTP
- Servicios: Lógica de negocio
- Repositorios: Acceso a datos

### 3. Manejo de errores
```java
@GetMapping("/{id}")
public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
    return productoService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

### 4. Validaciones
Siempre valida los datos en el servicio antes de guardarlos.

---

## 12. Ejercicios prácticos

### Ejercicio 1: API de Usuarios
Crea una API REST para gestionar usuarios con:
- Nombre, email, edad
- CRUD completo
- Validar que el email sea único

### Ejercicio 2: API de Tareas
Crea una API REST para gestionar tareas (To-Do) con:
- Título, descripción, completada (boolean)
- CRUD completo
- Endpoint para marcar tarea como completada

---

## 13. Recursos adicionales

- **Documentación oficial**: https://spring.io/projects/spring-boot
- **Spring Boot Guides**: https://spring.io/guides
- **Spring Boot Reference**: https://docs.spring.io/spring-boot/docs/current/reference/html/

---

## ❓ Preguntas frecuentes

### ¿Por qué usar @Autowired?
- Spring inyecta automáticamente las dependencias
- No necesitas crear objetos manualmente con `new`

### ¿Cuál es la diferencia entre @Controller y @RestController?
- `@Controller` retorna vistas (HTML)
- `@RestController` retorna datos (JSON/XML)

### ¿Cómo cambio el formato de fecha en las respuestas JSON?
- Usa `@JsonFormat` en el campo de fecha
- O configura `application.properties`

---

## 🎯 Checklist del Módulo 1

- [ ] Proyecto Spring Boot creado
- [ ] Primera API REST funcionando
- [ ] Entendido el concepto de capas (Controller, Service, Repository)
- [ ] Creado el mini proyecto de gestión de productos
- [ ] API probada con Postman
- [ ] Código subido a GitHub

---

**¡Felicidades! Has completado el Módulo 1** 🎉

**Siguiente paso**: [Módulo 2 - Persistencia con JPA y PostgreSQL](./03-MODULO-2-PERSISTENCIA.md)

