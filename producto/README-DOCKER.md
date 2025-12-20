# Guía de Docker para Producto API

## Opciones de Despliegue

### Opción 1: Keycloak en el mismo docker-compose (Recomendado)

Si quieres que docker-compose gestione todo, incluyendo Keycloak:

```bash
# Levantar todo (aplicación + PostgreSQL + Keycloak)
docker-compose up -d

# Ver logs de Keycloak
docker-compose logs -f keycloak

# Acceder a Keycloak: http://localhost:8081
# Usuario: admin / Contraseña: admin
```

### Opción 2: Keycloak en otro contenedor (Ya existente)

Si ya tienes Keycloak corriendo en otro contenedor:

1. **Obtén el nombre de tu contenedor de Keycloak:**
   ```bash
   docker ps | grep keycloak
   ```

2. **Obtén el nombre de la red donde está Keycloak:**
   ```bash
   docker inspect <nombre-contenedor-keycloak> | grep NetworkMode
   ```

3. **Edita `docker-compose.yml` y actualiza las variables de entorno:**
   ```yaml
   producto-api:
     environment:
       # Reemplaza 'keycloak' con el nombre de tu contenedor
       SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://<nombre-contenedor>:8080/realms/master
       SPRING_OAUTHFLOW_TOKENURL: http://<nombre-contenedor>:8080/realms/master/protocol/openid-connect/token
     networks:
       - producto-network
       - <red-de-keycloak>  # Agrega la red donde está Keycloak
   ```

4. **O conecta el contenedor a la misma red:**
   ```bash
   docker network connect producto_producto-network <nombre-contenedor-keycloak>
   ```

### Opción 3: Keycloak en el host (localhost)

Si Keycloak está corriendo directamente en tu máquina (no en Docker):

```bash
# Edita docker-compose.yml y cambia las URLs a:
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://host.docker.internal:8081/realms/master
SPRING_OAUTHFLOW_TOKENURL: http://host.docker.internal:8081/realms/master/protocol/openid-connect/token
```

### Opción 4: Solo aplicación con PostgreSQL en localhost

Si tu PostgreSQL está corriendo en `localhost:5432`:

```bash
# Construir la imagen
docker build -t producto-api:latest .

# Ejecutar el contenedor
docker run -d \
  --name producto-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=admin \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/master \
  --network producto_producto-network \
  producto-api:latest
```

## Comandos Útiles

### Construir la imagen
```bash
docker build -t producto-api:latest .
```

### Ver logs
```bash
docker logs -f producto-api
```

### Detener y eliminar
```bash
docker-compose down
# O si usaste docker run:
docker stop producto-api
docker rm producto-api
```

### Reconstruir después de cambios
```bash
docker-compose build --no-cache
docker-compose up -d
```

## Variables de Entorno

Puedes personalizar la configuración con estas variables:

- `SPRING_DATASOURCE_URL`: URL de conexión a PostgreSQL
- `SPRING_DATASOURCE_USERNAME`: Usuario de PostgreSQL
- `SPRING_DATASOURCE_PASSWORD`: Contraseña de PostgreSQL
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI`: URI del issuer de Keycloak
- `SPRING_OAUTHFLOW_TOKENURL`: URL para obtener tokens de Keycloak

## Solución de Problemas

### La aplicación no puede conectarse a Keycloak

1. **Verifica que Keycloak esté corriendo:**
   ```bash
   docker ps | grep keycloak
   ```

2. **Verifica que estén en la misma red:**
   ```bash
   docker network inspect producto_producto-network
   ```

3. **Conecta Keycloak a la red si es necesario:**
   ```bash
   docker network connect producto_producto-network <nombre-contenedor-keycloak>
   ```

4. **Prueba la conectividad desde el contenedor:**
   ```bash
   docker exec producto-api wget -O- http://keycloak:8080/realms/master/.well-known/openid-configuration
   ```

5. **Verifica los logs:**
   ```bash
   docker logs producto-api | grep -i keycloak
   docker logs producto-api | grep -i oauth
   ```

### Keycloak está en otro puerto

Si Keycloak usa un puerto diferente (por ejemplo 8081), actualiza las variables:
```yaml
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://keycloak:8081/realms/master
SPRING_OAUTHFLOW_TOKENURL: http://keycloak:8081/realms/master/protocol/openid-connect/token
```

## Notas Importantes

1. **host.docker.internal**: En Windows y Mac, Docker puede acceder al `localhost` del host usando `host.docker.internal`. En Linux, puede que necesites usar `--network host` o la IP de tu máquina.

2. **Comunicación entre contenedores**: Los contenedores en la misma red Docker pueden comunicarse usando el nombre del servicio/contenedor como hostname.

3. **Puerto PostgreSQL**: Si tu PostgreSQL local usa un puerto diferente a 5432, ajusta la URL de conexión.

4. **Perfil Docker**: El archivo `application-docker.yaml` se puede activar con `SPRING_PROFILES_ACTIVE=docker`, pero las variables de entorno tienen prioridad.

