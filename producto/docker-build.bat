@echo off
echo Construyendo imagen Docker para producto-api...
docker build -t producto-api:latest .
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Imagen construida exitosamente!
    echo.
    echo Para ejecutar el contenedor:
    echo docker run -d --name producto-api -p 8080:8080 ^
    echo   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres ^
    echo   -e SPRING_DATASOURCE_USERNAME=postgres ^
    echo   -e SPRING_DATASOURCE_PASSWORD=admin ^
    echo   -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://host.docker.internal:8081/realms/master ^
    echo   producto-api:latest
    echo.
    echo O usa docker-compose:
    echo docker-compose up -d
) else (
    echo Error al construir la imagen
    exit /b 1
)

