# Módulo 3 - Seguridad con JWT y Roles (5 horas)

## 📋 Objetivos del Módulo
- Configurar Spring Security en Spring Boot
- Implementar autenticación y autorización
- Implementar JWT (JSON Web Token)
- Proteger endpoints y manejar roles
- Desarrollar proyecto: Login y registro para el sistema de facturación

---

## 1. Introducción a Spring Security

### ¿Qué es Spring Security?

Spring Security es un framework de seguridad que proporciona:
- **Autenticación**: Verificar quién es el usuario
- **Autorización**: Controlar qué puede hacer el usuario
- **Protección contra ataques**: CSRF, XSS, etc.

### Conceptos clave

- **Authentication**: Proceso de verificar la identidad
- **Authorization**: Proceso de verificar permisos
- **Principal**: Usuario autenticado
- **Role**: Rol del usuario (ADMIN, USER, etc.)
- **JWT**: Token que contiene información del usuario

---

## 2. Configuración inicial de Spring Security

### Paso 1: Agregar dependencias

En `pom.xml`:

```xml
<dependencies>
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Password Encoder -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-crypto</artifactId>
    </dependency>
</dependencies>
```

### Paso 2: Crear la entidad Usuario

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    @Column(nullable = false)
    private Boolean activo = true;

    // Constructores
    public Usuario() {}

    public Usuario(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
```

### Paso 3: Crear la entidad Rol

```java
package com.tuempresa.facturacion.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private ERol nombre;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios = new HashSet<>();

    // Constructores
    public Rol() {}

    public Rol(ERol nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERol getNombre() {
        return nombre;
    }

    public void setNombre(ERol nombre) {
        this.nombre = nombre;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
```

### Paso 4: Crear el enum ERol

```java
package com.tuempresa.facturacion.model;

public enum ERol {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MODERATOR
}
```

---

## 3. Repositorios

```java
package com.tuempresa.facturacion.repository;

import com.tuempresa.facturacion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
```

```java
package com.tuempresa.facturacion.repository;

import com.tuempresa.facturacion.model.ERol;
import com.tuempresa.facturacion.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(ERol nombre);
}
```

---

## 4. Implementar JWT

### Paso 1: Crear utilidad para JWT

```java
package com.tuempresa.facturacion.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generarToken(Authentication authentication) {
        String username = authentication.getName();
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getSigningKey())
                .compact();
    }

    public String obtenerUsernameDelToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string está vacío: {}", e.getMessage());
        }
        return false;
    }
}
```

### Paso 2: Configurar application.properties

```properties
# JWT Configuration
app.jwt.secret=MiClaveSecretaSuperSeguraParaJWTQueDebeSerMuyLarga123456789
app.jwt.expiration=86400000
```

**Nota**: En producción, usa una clave más segura y guárdala en variables de entorno.

---

## 5. Crear UserDetailsService

```java
package com.tuempresa.facturacion.security;

import com.tuempresa.facturacion.model.Usuario;
import com.tuempresa.facturacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(getAuthorities(usuario))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        return usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre().name()))
                .collect(Collectors.toList());
    }
}
```

---

## 6. Filtro JWT

```java
package com.tuempresa.facturacion.security.jwt;

import com.tuempresa.facturacion.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = obtenerJwtDelRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validarToken(jwt)) {
                String username = tokenProvider.obtenerUsernameDelToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("No se pudo establecer la autenticación del usuario: {}", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String obtenerJwtDelRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

---

## 7. Configuración de Security

```java
package com.tuempresa.facturacion.security;

import com.tuempresa.facturacion.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/productos/**").permitAll() // Temporal, luego proteger
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, 
                           UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

---

## 8. DTOs para autenticación

```java
package com.tuempresa.facturacion.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

```java
package com.tuempresa.facturacion.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private java.util.Collection<String> roles;

    public JwtResponse(String token, String username, String email, 
                      java.util.Collection<String> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public java.util.Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(java.util.Collection<String> roles) {
        this.roles = roles;
    }
}
```

```java
package com.tuempresa.facturacion.dto;

import jakarta.validation.constraints.*;

public class RegistroRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private java.util.Set<String> roles;

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public java.util.Set<String> getRoles() {
        return roles;
    }

    public void setRoles(java.util.Set<String> roles) {
        this.roles = roles;
    }
}
```

---

## 9. Servicio de autenticación

```java
package com.tuempresa.facturacion.service;

import com.tuempresa.facturacion.dto.LoginRequest;
import com.tuempresa.facturacion.dto.RegistroRequest;
import com.tuempresa.facturacion.model.ERol;
import com.tuempresa.facturacion.model.Rol;
import com.tuempresa.facturacion.model.Usuario;
import com.tuempresa.facturacion.repository.RolRepository;
import com.tuempresa.facturacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private com.tuempresa.facturacion.security.jwt.JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Usuario registrar(RegistroRequest registroRequest) {
        if (usuarioRepository.existsByUsername(registroRequest.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso");
        }

        Usuario usuario = new Usuario(
                registroRequest.getUsername(),
                registroRequest.getEmail(),
                passwordEncoder.encode(registroRequest.getPassword())
        );

        Set<String> strRoles = registroRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Rol rolUsuario = rolRepository.findByNombre(ERol.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));
            roles.add(rolUsuario);
        } else {
            strRoles.forEach(rol -> {
                switch (rol) {
                    case "admin":
                        Rol rolAdmin = rolRepository.findByNombre(ERol.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));
                        roles.add(rolAdmin);
                        break;
                    case "mod":
                        Rol rolMod = rolRepository.findByNombre(ERol.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));
                        roles.add(rolMod);
                        break;
                    default:
                        Rol rolUsuario = rolRepository.findByNombre(ERol.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado"));
                        roles.add(rolUsuario);
                }
            });
        }

        usuario.setRoles(roles);
        return usuarioRepository.save(usuario);
    }

    public com.tuempresa.facturacion.dto.JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generarToken(authentication);

        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        java.util.Collection<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre().name())
                .collect(Collectors.toList());

        return new com.tuempresa.facturacion.dto.JwtResponse(
                jwt,
                usuario.getUsername(),
                usuario.getEmail(),
                roles
        );
    }
}
```

---

## 10. Controlador de autenticación

```java
package com.tuempresa.facturacion.controller;

import com.tuempresa.facturacion.dto.JwtResponse;
import com.tuempresa.facturacion.dto.LoginRequest;
import com.tuempresa.facturacion.dto.RegistroRequest;
import com.tuempresa.facturacion.model.Usuario;
import com.tuempresa.facturacion.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }

        try {
            JwtResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: Credenciales inválidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegistroRequest registroRequest,
                                       BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors());
        }

        try {
            Usuario usuario = authService.registrar(registroRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario registrado exitosamente: " + usuario.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
```

---

## 11. Proteger endpoints con roles

### Actualizar SecurityConfig

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/productos/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/clientes/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/facturas/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, 
                       UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### Proteger métodos con anotaciones

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        // Solo ADMIN puede crear productos
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Solo ADMIN puede eliminar productos
    }
}
```

---

## 12. Proyecto: Login y registro para sistema de facturación

### Objetivos

1. **Sistema de registro**
   - Registro de nuevos usuarios
   - Asignación de roles (USER por defecto)
   - Validaciones

2. **Sistema de login**
   - Autenticación con username y password
   - Generación de JWT
   - Retorno de token y datos del usuario

3. **Protección de endpoints**
   - Endpoints públicos: `/api/auth/**`
   - Endpoints protegidos: Requieren autenticación
   - Endpoints de admin: Solo para ADMIN

4. **Funcionalidades adicionales**
   - Obtener perfil del usuario autenticado
   - Cambiar contraseña
   - Actualizar perfil

### Estructura del proyecto

```
src/main/java/com/tuempresa/facturacion/
├── security/
│   ├── SecurityConfig.java
│   ├── UserDetailsServiceImpl.java
│   ├── jwt/
│   │   ├── JwtTokenProvider.java
│   │   └── JwtAuthenticationFilter.java
├── controller/
│   └── AuthController.java
├── service/
│   └── AuthService.java
├── repository/
│   ├── UsuarioRepository.java
│   └── RolRepository.java
├── model/
│   ├── Usuario.java
│   ├── Rol.java
│   └── ERol.java
└── dto/
    ├── LoginRequest.java
    ├── RegistroRequest.java
    └── JwtResponse.java
```

### Criterios de evaluación

- [ ] Entidades Usuario y Rol creadas
- [ ] JWT implementado y funcionando
- [ ] Registro de usuarios funcionando
- [ ] Login funcionando y retornando JWT
- [ ] Endpoints protegidos correctamente
- [ ] Roles funcionando (USER, ADMIN)
- [ ] Validaciones implementadas
- [ ] Código limpio y documentado

---

## 13. Probar la API con Postman

### 1. Registrar un usuario

**POST** `http://localhost:8080/api/auth/registro`

Headers:
```
Content-Type: application/json
```

Body:
```json
{
  "username": "juan",
  "email": "juan@ejemplo.com",
  "password": "password123",
  "roles": ["user"]
}
```

### 2. Login

**POST** `http://localhost:8080/api/auth/login`

Body:
```json
{
  "username": "juan",
  "password": "password123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "juan",
  "email": "juan@ejemplo.com",
  "roles": ["ROLE_USER"]
}
```

### 3. Usar el token

**GET** `http://localhost:8080/api/productos`

Headers:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 14. Inicializar roles en la base de datos

Crear una clase de configuración para inicializar roles:

```java
package com.tuempresa.facturacion.config;

import com.tuempresa.facturacion.model.ERol;
import com.tuempresa.facturacion.model.Rol;
import com.tuempresa.facturacion.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) {
            rolRepository.save(new Rol(ERol.ROLE_USER));
            rolRepository.save(new Rol(ERol.ROLE_ADMIN));
            rolRepository.save(new Rol(ERol.ROLE_MODERATOR));
        }
    }
}
```

---

## 15. Buenas prácticas

### 1. Nunca expongas contraseñas
- No retornes la contraseña en las respuestas
- Usa DTOs sin el campo password

### 2. Usa HTTPS en producción
- Los tokens JWT deben transmitirse solo por HTTPS

### 3. Tokens con expiración corta
- Tokens de acceso: 15-60 minutos
- Tokens de refresh: 7-30 días

### 4. Validar tokens en cada request
- El filtro JWT valida el token en cada petición

### 5. Manejar errores de seguridad
- No reveles información sensible en errores

---

## ❓ Preguntas frecuentes

### ¿Qué es un JWT?
- JSON Web Token: Token que contiene información del usuario
- Se envía en el header Authorization
- Formato: `Bearer <token>`

### ¿Cómo funciona la autenticación?
1. Usuario hace login con username/password
2. Si es válido, se genera un JWT
3. El cliente envía el JWT en cada request
4. El servidor valida el JWT y autentica al usuario

### ¿Qué es BCrypt?
- Algoritmo de hash para contraseñas
- Es seguro y lento (dificulta ataques de fuerza bruta)

---

## 🎯 Checklist del Módulo 3

- [ ] Spring Security configurado
- [ ] Entidades Usuario y Rol creadas
- [ ] JWT implementado
- [ ] Registro de usuarios funcionando
- [ ] Login funcionando
- [ ] Endpoints protegidos
- [ ] Roles implementados
- [ ] Proyecto de login/registro completado
- [ ] API probada con Postman

---

**¡Felicidades! Has completado el Módulo 3** 🎉

**Siguiente paso**: [Módulo 4 - Despliegue y Buenas Prácticas](./05-MODULO-4-DESPLIEGUE.md)

