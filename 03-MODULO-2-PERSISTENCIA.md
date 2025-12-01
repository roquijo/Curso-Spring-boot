# Módulo 2 - Persistencia con JPA y PostgreSQL (5 horas)

## 📋 Objetivos del Módulo
- Conectar Spring Boot a bases de datos PostgreSQL
- Aprender sobre entidades, relaciones y repositorios JPA
- Implementar CRUD profesional con JPA Repository
- Aplicar validaciones con anotaciones
- Desarrollar proyecto: sistema de facturación

---

## 1. Introducción a JPA y Hibernate

### ¿Qué es JPA?

**JPA (Java Persistence API)** es una especificación de Java para mapear objetos Java a tablas de base de datos. **Hibernate** es la implementación más popular de JPA.

### Ventajas de JPA

- ✅ **Menos código SQL**: Escribes menos consultas manuales
- ✅ **Mapeo objeto-relacional**: Los objetos Java se convierten automáticamente en tablas
- ✅ **Portabilidad**: El mismo código funciona con diferentes bases de datos
- ✅ **Type-safe**: Menos errores en tiempo de ejecución

### Conceptos clave

- **Entity**: Clase Java que representa una tabla
- **Repository**: Interfaz que maneja operaciones de base de datos
- **ORM (Object-Relational Mapping)**: Mapeo entre objetos y tablas

---

## 2. Configuración de la base de datos

### Paso 1: Agregar dependencias

En `pom.xml`, agrega estas dependencias:

```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Validación -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

### Paso 2: Configurar application.properties

Crea o edita `src/main/resources/application.properties`:

```properties
# Configuración de la base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/springboot_db
spring.datasource.username=springuser
spring.datasource.password=springpass
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Puerto del servidor
server.port=8080
```

### Explicación de propiedades

- `spring.datasource.url`: URL de conexión a PostgreSQL (puerto 5432 por defecto)
- `spring.datasource.username`: Usuario de PostgreSQL
- `spring.datasource.password`: Contraseña de PostgreSQL
- `spring.jpa.hibernate.ddl-auto`: 
  - `update`: Crea/actualiza tablas automáticamente
  - `create`: Crea tablas cada vez (¡cuidado, borra datos!)
  - `validate`: Solo valida, no modifica
  - `none`: No hace nada
- `spring.jpa.show-sql`: Muestra las consultas SQL en consola

---

### Paso 3: Crear tu primera entidad

### Ejemplo: Entidad Producto

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    // Constructores
    public Producto() {}

    public Producto(String nombre, Double precio, Integer stock, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
```

### Anotaciones JPA explicadas

| Anotación | Uso | Descripción |
|-----------|-----|-------------|
| `@Entity` | Clase | Marca la clase como entidad JPA |
| `@Table` | Clase | Especifica el nombre de la tabla |
| `@Id` | Campo | Marca la clave primaria |
| `@GeneratedValue` | Campo | Genera automáticamente el ID |
| `@Column` | Campo | Configura la columna en la BD |
| `@NotNull` | Campo | Valida que no sea null |
| `@NotBlank` | Campo | Valida que no esté vacío (String) |
| `@Size` | Campo | Valida tamaño de String |
| `@Min` / `@Max` | Campo | Valida valores numéricos |
| `@DecimalMin` / `@DecimalMax` | Campo | Valida valores decimales |

---

## 4. Crear el repositorio JPA

### Interfaz Repository

```java
package com.tuempresa.facturacion.repository;

import com.tuempresa.facturacion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Métodos personalizados (Spring los implementa automáticamente)
    
    // Buscar por nombre (case-insensitive)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos con stock mayor a un valor
    List<Producto> findByStockGreaterThan(Integer stock);
    
    // Buscar productos por rango de precio
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);
    
    // Consulta personalizada con JPQL
    @Query("SELECT p FROM Producto p WHERE p.precio > :precioMin AND p.stock > 0")
    List<Producto> encontrarProductosDisponiblesPorPrecio(@Param("precioMin") Double precioMin);
    
    // Consulta nativa SQL
    @Query(value = "SELECT * FROM productos WHERE precio > ?1 ORDER BY precio ASC", nativeQuery = true)
    List<Producto> encontrarProductosCaros(Double precioMin);
}
```

### Métodos disponibles automáticamente

Al extender `JpaRepository<Producto, Long>`, tienes acceso a:

```java
// Guardar
productoRepository.save(producto);

// Buscar por ID
Optional<Producto> producto = productoRepository.findById(1L);

// Buscar todos
List<Producto> productos = productoRepository.findAll();

// Eliminar
productoRepository.deleteById(1L);
productoRepository.delete(producto);

// Contar
long cantidad = productoRepository.count();

// Verificar existencia
boolean existe = productoRepository.existsById(1L);
```

### Nomenclatura de métodos personalizados

Spring Data JPA puede crear métodos automáticamente basándose en el nombre:

```java
// Buscar por nombre exacto
findByNombre(String nombre)

// Buscar por nombre ignorando mayúsculas/minúsculas
findByNombreIgnoreCase(String nombre)

// Buscar por nombre que contenga
findByNombreContaining(String nombre)

// Buscar por precio mayor que
findByPrecioGreaterThan(Double precio)

// Buscar por precio menor que
findByPrecioLessThan(Double precio)

// Buscar por precio entre
findByPrecioBetween(Double min, Double max)

// Combinar condiciones
findByNombreAndPrecio(String nombre, Double precio)
findByNombreOrPrecio(String nombre, Double precio)

// Ordenar
findByNombreOrderByPrecioAsc(String nombre)
findByNombreOrderByPrecioDesc(String nombre)
```

---

## 5. Actualizar el servicio

```java
package com.tuempresa.facturacion.service;

import com.tuempresa.facturacion.model.Producto;
import com.tuempresa.facturacion.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
        // Las validaciones se hacen automáticamente con @Valid
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto productoActualizado) {
        Optional<Producto> productoExistente = productoRepository.findById(id);
        
        if (productoExistente.isPresent()) {
            Producto producto = productoExistente.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            producto.setDescripcion(productoActualizado.getDescripcion());
            return productoRepository.save(producto);
        }
        
        throw new RuntimeException("Producto no encontrado con ID: " + id);
    }

    public void eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }

    // Métodos adicionales usando queries personalizadas
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarProductosConStock(Integer stockMinimo) {
        return productoRepository.findByStockGreaterThan(stockMinimo);
    }

    public List<Producto> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return productoRepository.findByPrecioBetween(precioMin, precioMax);
    }
}
```

---

## 6. Actualizar el controlador con validaciones

```java
package com.tuempresa.facturacion.controller;

import com.tuempresa.facturacion.model.Producto;
import com.tuempresa.facturacion.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Producto producto, 
                                   BindingResult result) {
        if (result.hasErrors()) {
            // Retornar errores de validación
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }
        
        try {
            Producto nuevo = productoService.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody Producto producto,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }
        
        try {
            Producto actualizado = productoService.actualizar(id, producto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints adicionales
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/stock/{minimo}")
    public List<Producto> buscarConStock(@PathVariable Integer minimo) {
        return productoService.buscarProductosConStock(minimo);
    }
}
```

---

## 7. Relaciones entre entidades

### Tipos de relaciones

1. **@OneToOne**: Uno a uno
2. **@OneToMany**: Uno a muchos
3. **@ManyToOne**: Muchos a uno
4. **@ManyToMany**: Muchos a muchos

### Ejemplo: Sistema de Facturación

#### Entidad Cliente

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false, length = 20)
    private String telefono;

    // Relación uno a muchos con Facturas
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    // Constructores, getters y setters
    public Cliente() {}

    // ... getters y setters
}
```

#### Entidad Factura

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private Double total;

    // Relación muchos a uno con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Relación uno a muchos con DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleFactura> detalles;

    // Constructores, getters y setters
    public Factura() {}

    // ... getters y setters
}
```

#### Entidad DetalleFactura

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Relación muchos a uno con Factura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    // Relación muchos a uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Constructores, getters y setters
    public DetalleFactura() {}

    // ... getters y setters
}
```

### Explicación de anotaciones de relaciones

- `@OneToMany`: Un cliente tiene muchas facturas
- `@ManyToOne`: Muchas facturas pertenecen a un cliente
- `mappedBy`: Indica el campo en la otra entidad que maneja la relación
- `cascade`: Operaciones que se propagan (ALL, PERSIST, MERGE, REMOVE)
- `fetch`: EAGER (carga inmediata) o LAZY (carga bajo demanda)

---

## 8. Proyecto: Sistema de Facturación

### Objetivos

Crear un sistema completo de facturación con:

1. **Gestión de Clientes**
   - CRUD completo
   - Validaciones

2. **Gestión de Productos**
   - CRUD completo
   - Control de stock

3. **Gestión de Facturas**
   - Crear factura con cliente
   - Agregar productos a la factura
   - Calcular totales automáticamente
   - Actualizar stock al facturar

### Estructura del proyecto

```
src/main/java/com/tuempresa/facturacion/
├── FacturacionApplication.java
├── controller/
│   ├── ClienteController.java
│   ├── ProductoController.java
│   └── FacturaController.java
├── service/
│   ├── ClienteService.java
│   ├── ProductoService.java
│   └── FacturaService.java
├── repository/
│   ├── ClienteRepository.java
│   ├── ProductoRepository.java
│   └── FacturaRepository.java
└── model/
    ├── Cliente.java
    ├── Producto.java
    ├── Factura.java
    └── DetalleFactura.java
```

### Funcionalidades requeridas

#### ClienteService
```java
- List<Cliente> obtenerTodos()
- Optional<Cliente> obtenerPorId(Long id)
- Cliente crear(Cliente cliente)
- Cliente actualizar(Long id, Cliente cliente)
- void eliminar(Long id)
- List<Cliente> buscarPorNombre(String nombre)
```

#### FacturaService
```java
- List<Factura> obtenerTodas()
- Optional<Factura> obtenerPorId(Long id)
- Factura crear(Factura factura, List<DetalleFactura> detalles)
- void eliminar(Long id)
- Double calcularTotal(List<DetalleFactura> detalles)
- void actualizarStock(List<DetalleFactura> detalles)
```

### Criterios de evaluación

- [ ] Entidades correctamente mapeadas con JPA
- [ ] Relaciones entre entidades funcionando
- [ ] Validaciones implementadas
- [ ] Repositorios con métodos personalizados
- [ ] Lógica de negocio en servicios
- [ ] API REST completa y funcional
- [ ] Manejo de errores apropiado
- [ ] Código limpio y bien documentado

---

## 9. Validaciones avanzadas

### Validaciones personalizadas

```java
package com.tuempresa.facturacion.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailUnicoValidator.class)
@Documented
public @interface EmailUnico {
    String message() default "El email ya está registrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

```java
package com.tuempresa.facturacion.validation;

import com.tuempresa.facturacion.repository.ClienteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailUnicoValidator implements ConstraintValidator<EmailUnico, String> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // @NotNull se encarga de esto
        }
        return !clienteRepository.existsByEmail(email);
    }
}
```

### Uso de la validación personalizada

```java
@Entity
public class Cliente {
    @EmailUnico
    @Email
    private String email;
}
```

---

## 10. Manejo de excepciones

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
```

### Manejador global de excepciones

```java
package com.tuempresa.facturacion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());
        error.put("codigo", "RECURSO_NO_ENCONTRADO");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> manejarStockInsuficiente(
            StockInsuficienteException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());
        error.put("codigo", "STOCK_INSUFICIENTE");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
            MethodArgumentNotValidException ex) {
        Map<String, Object> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}
```

---

## 11. Buenas prácticas

### 1. Usar DTOs (Data Transfer Objects)

En lugar de exponer entidades directamente, usa DTOs:

```java
public class ProductoDTO {
    private String nombre;
    private Double precio;
    private Integer stock;
    // ... getters y setters
}
```

### 2. Paginación

```java
@GetMapping
public ResponseEntity<Page<Producto>> obtenerTodos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Producto> productos = productoRepository.findAll(pageable);
    return ResponseEntity.ok(productos);
}
```

### 3. Transacciones

```java
@Transactional
public Factura crearFactura(Factura factura, List<DetalleFactura> detalles) {
    // Todas las operaciones se ejecutan en una transacción
    // Si algo falla, todo se revierte
}
```

---

## 12. Ejercicios prácticos

### Ejercicio 1: Sistema de Biblioteca
Crea un sistema de biblioteca con:
- Libros (título, autor, ISBN, cantidad disponible)
- Usuarios (nombre, email, teléfono)
- Préstamos (usuario, libro, fecha préstamo, fecha devolución)

### Ejercicio 2: Sistema de Reservas
Crea un sistema de reservas de hotel con:
- Habitaciones (número, tipo, precio por noche)
- Clientes (nombre, email, teléfono)
- Reservas (cliente, habitación, fecha entrada, fecha salida, total)

---

## ❓ Preguntas frecuentes

### ¿Cuál es la diferencia entre EAGER y LAZY?
- **EAGER**: Carga los datos relacionados inmediatamente
- **LAZY**: Carga los datos solo cuando se acceden (más eficiente)

### ¿Qué es cascade?
- Define qué operaciones se propagan a las entidades relacionadas
- `CascadeType.ALL`: Todas las operaciones se propagan

### ¿Cómo evito el error LazyInitializationException?
- Usa `@Transactional` en el servicio
- O usa `fetch = FetchType.EAGER` (menos eficiente)

---

## 🎯 Checklist del Módulo 2

- [ ] Base de datos PostgreSQL configurada
- [ ] Dependencias JPA agregadas
- [ ] Primera entidad creada y funcionando
- [ ] Repositorio JPA implementado
- [ ] CRUD completo con base de datos
- [ ] Validaciones implementadas
- [ ] Relaciones entre entidades creadas
- [ ] Proyecto de facturación completado
- [ ] Manejo de excepciones implementado

---

**¡Felicidades! Has completado el Módulo 2** 🎉

**Siguiente paso**: [Módulo 3 - Seguridad con JWT y Roles](./04-MODULO-3-SEGURIDAD.md)

