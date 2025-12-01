# Guía de Instalación Inicial - Curso Spring Boot

Esta guía te ayudará a instalar todas las herramientas necesarias para comenzar el curso de Spring Boot.

## 📋 Índice
1. [Instalación de Java JDK](#instalación-de-java-jdk)
2. [Instalación de Git](#instalación-de-git)
3. [Creación de cuenta en GitHub](#creación-de-cuenta-en-github)
4. [Instalación de IDE (IntelliJ IDEA o VS Code)](#instalación-de-ide)
5. [Instalación de MySQL](#instalación-de-mysql)
6. [Instalación de Postman](#instalación-de-postman)
7. [Verificación de instalaciones](#verificación-de-instalaciones)

---

## 1. Instalación de Java JDK

### Windows

1. **Descargar Java JDK 17 o superior**
   - Visita: https://adoptium.net/ (recomendado) o https://www.oracle.com/java/technologies/downloads/
   - Selecciona la versión **JDK 17 LTS** o superior
   - Descarga el instalador para Windows (`.msi`)

2. **Ejecutar el instalador**
   - Ejecuta el archivo `.msi` descargado
   - Sigue el asistente de instalación
   - **IMPORTANTE**: Marca la opción "Add to PATH" si está disponible

3. **Verificar la instalación**
   - Abre la terminal (CMD o PowerShell)
   - Ejecuta:
     ```bash
     java -version
     javac -version
     ```
   - Deberías ver algo como: `openjdk version "17.0.x"` o similar

4. **Configurar JAVA_HOME (si es necesario)**
   - Busca "Variables de entorno" en Windows
   - Crea una variable de sistema llamada `JAVA_HOME`
   - Establece su valor a la ruta de instalación (ej: `C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot`)
   - Agrega `%JAVA_HOME%\bin` al PATH

### macOS

1. **Usando Homebrew (recomendado)**
   ```bash
   brew install openjdk@17
   ```

2. **O descargar manualmente**
   - Visita: https://adoptium.net/
   - Descarga el instalador `.pkg` para macOS
   - Ejecuta el instalador

3. **Verificar la instalación**
   ```bash
   java -version
   javac -version
   ```

### Linux (Ubuntu/Debian)

```bash
# Actualizar repositorios
sudo apt update

# Instalar OpenJDK 17
sudo apt install openjdk-17-jdk

# Verificar instalación
java -version
javac -version
```

---

## 2. Instalación de Git

### Windows

1. **Descargar Git**
   - Visita: https://git-scm.com/download/win
   - Descarga el instalador

2. **Ejecutar el instalador**
   - Ejecuta el archivo descargado
   - Durante la instalación:
     - Selecciona "Git from the command line and also from 3rd-party software"
     - Selecciona "Use bundled OpenSSH"
     - Selecciona "Checkout Windows-style, commit Unix-style line endings"
     - Selecciona "Use Windows' default console window"

3. **Verificar la instalación**
   ```bash
   git --version
   ```

### macOS

```bash
# Usando Homebrew
brew install git

# O descargar desde: https://git-scm.com/download/mac
```

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install git
git --version
```

---

## 3. Creación de cuenta en GitHub

### Paso 1: Crear la cuenta

1. **Visita GitHub**
   - Ve a: https://github.com/
   - Haz clic en "Sign up"

2. **Completar el registro**
   - Ingresa tu email
   - Crea una contraseña segura
   - Elige un nombre de usuario (será tu perfil público)
   - Verifica tu email

3. **Configurar tu perfil**
   - Sube una foto de perfil (opcional pero recomendado)
   - Completa tu biografía
   - Configura tu visibilidad (público/privado)

### Paso 2: Configurar Git con tu identidad

Abre tu terminal y ejecuta:

```bash
git config --global user.name "Tu Nombre Completo"
git config --global user.email "tu-email@ejemplo.com"
```

**Nota**: Usa el mismo email que registraste en GitHub.

### Paso 3: Generar SSH Key (Opcional pero recomendado)

1. **Generar la clave SSH**
   ```bash
   ssh-keygen -t ed25519 -C "tu-email@ejemplo.com"
   ```
   - Presiona Enter para aceptar la ubicación predeterminada
   - Opcionalmente, agrega una frase de contraseña

2. **Copiar la clave pública**
   ```bash
   # Windows
   type %USERPROFILE%\.ssh\id_ed25519.pub
   
   # macOS/Linux
   cat ~/.ssh/id_ed25519.pub
   ```

3. **Agregar la clave a GitHub**
   - Ve a GitHub → Settings → SSH and GPG keys
   - Haz clic en "New SSH key"
   - Pega tu clave pública
   - Guarda

4. **Probar la conexión**
   ```bash
   ssh -T git@github.com
   ```
   - Deberías ver: "Hi [username]! You've successfully authenticated..."

---

## 4. Instalación de IDE

### Opción A: IntelliJ IDEA (Recomendado para Spring Boot)

1. **Descargar IntelliJ IDEA Community Edition**
   - Visita: https://www.jetbrains.com/idea/download/
   - Descarga la versión Community (gratuita)

2. **Instalar**
   - Ejecuta el instalador
   - Sigue el asistente
   - Selecciona las opciones recomendadas

3. **Configurar para Spring Boot**
   - Abre IntelliJ IDEA
   - Ve a File → Settings → Plugins
   - Instala el plugin "Spring Boot" (si no viene incluido)
   - Instala el plugin "Spring Assistant"

### Opción B: Visual Studio Code

1. **Descargar VS Code**
   - Visita: https://code.visualstudio.com/
   - Descarga e instala

2. **Instalar extensiones necesarias**
   - Abre VS Code
   - Ve a Extensions (Ctrl+Shift+X)
   - Instala:
     - **Extension Pack for Java** (Microsoft)
     - **Spring Boot Extension Pack** (VMware)
     - **Spring Boot Tools** (VMware)
     - **Spring Initializr Java Support** (VMware)

---

## 5. Instalación de MySQL

### Windows

1. **Descargar MySQL**
   - Visita: https://dev.mysql.com/downloads/installer/
   - Descarga "MySQL Installer for Windows"

2. **Instalar**
   - Ejecuta el instalador
   - Selecciona "Developer Default"
   - Sigue el asistente
   - **IMPORTANTE**: Anota la contraseña del usuario `root` que configures

3. **Verificar instalación**
   - Abre MySQL Workbench (viene incluido)
   - O desde la terminal:
     ```bash
     mysql --version
     ```

### macOS

```bash
# Usando Homebrew
brew install mysql

# Iniciar MySQL
brew services start mysql

# Configurar contraseña root
mysql_secure_installation
```

### Linux (Ubuntu/Debian)

```bash
# Instalar MySQL
sudo apt update
sudo apt install mysql-server

# Configurar MySQL
sudo mysql_secure_installation

# Verificar instalación
sudo systemctl status mysql
```

### Crear usuario y base de datos de prueba

1. **Acceder a MySQL**
   ```bash
   mysql -u root -p
   ```

2. **Crear usuario y base de datos**
   ```sql
   CREATE DATABASE springboot_db;
   CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'springpass';
   GRANT ALL PRIVILEGES ON springboot_db.* TO 'springuser'@'localhost';
   FLUSH PRIVILEGES;
   EXIT;
   ```

---

## 6. Instalación de Postman

Postman es útil para probar las APIs REST que crearemos.

1. **Descargar Postman**
   - Visita: https://www.postman.com/downloads/
   - Descarga la versión para tu sistema operativo

2. **Instalar y crear cuenta**
   - Ejecuta el instalador
   - Crea una cuenta gratuita (opcional pero recomendado)

---

## 7. Verificación de instalaciones

Ejecuta estos comandos en tu terminal para verificar que todo está instalado correctamente:

```bash
# Verificar Java
java -version
javac -version

# Verificar Git
git --version

# Verificar MySQL
mysql --version

# Verificar configuración de Git
git config --global user.name
git config --global user.email
```

### Checklist de instalación

- [ ] Java JDK 17 o superior instalado
- [ ] Git instalado y configurado
- [ ] Cuenta de GitHub creada
- [ ] SSH Key configurada (opcional)
- [ ] IDE instalado (IntelliJ IDEA o VS Code)
- [ ] Extensiones de Spring Boot instaladas
- [ ] MySQL instalado y funcionando
- [ ] Postman instalado

---

## 🎯 Siguiente paso

Una vez completada esta instalación, procede con:
- **[Guía de Git y GitHub](./01-GIT-GITHUB-BASICO.md)** - Para aprender los comandos básicos
- **[Módulo 1 - Fundamentos de Spring Boot](./02-MODULO-1-FUNDAMENTOS.md)** - Para comenzar con el curso

---

## ❓ Solución de problemas comunes

### Java no se reconoce en la terminal
- Verifica que JAVA_HOME esté configurado
- Reinicia la terminal después de instalar Java
- En Windows, verifica que Java esté en el PATH

### Git no funciona
- Reinicia la terminal después de instalar Git
- Verifica que Git esté en el PATH del sistema

### MySQL no inicia
- En Windows: Verifica el servicio MySQL en "Servicios"
- En macOS: `brew services start mysql`
- En Linux: `sudo systemctl start mysql`

### Problemas con SSH en GitHub
- Asegúrate de haber copiado la clave pública completa
- Verifica que el archivo `~/.ssh/id_ed25519.pub` exista
- Prueba la conexión con: `ssh -T git@github.com`

---

**¡Felicitaciones!** 🎉 Ya tienes todo listo para comenzar el curso de Spring Boot.

