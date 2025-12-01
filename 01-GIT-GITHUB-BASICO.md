# Guía de Git y GitHub - Fundamentos

Esta guía te enseñará los conceptos básicos de Git y GitHub necesarios para el curso.

## 📋 Índice
1. [¿Qué es Git y GitHub?](#qué-es-git-y-github)
2. [Conceptos básicos](#conceptos-básicos)
3. [Comandos esenciales de Git](#comandos-esenciales-de-git)
4. [Flujo de trabajo básico](#flujo-de-trabajo-básico)
5. [Crear tu primer repositorio](#crear-tu-primer-repositorio)
6. [Clonar un repositorio](#clonar-un-repositorio)
7. [Ramas (Branches)](#ramas-branches)
8. [Buenas prácticas](#buenas-prácticas)

---

## ¿Qué es Git y GitHub?

### Git
- **Git** es un sistema de control de versiones distribuido
- Te permite guardar el historial de cambios de tus archivos
- Puedes trabajar sin conexión a internet
- Es gratuito y de código abierto

### GitHub
- **GitHub** es una plataforma en la nube que aloja repositorios Git
- Permite colaborar con otros desarrolladores
- Ofrece funcionalidades adicionales (Issues, Pull Requests, etc.)
- Es como "Google Drive" pero para código

---

## Conceptos básicos

### Repositorio (Repo)
Un repositorio es una carpeta que contiene tu proyecto y su historial de cambios.

### Commit
Un commit es una "fotografía" del estado de tu proyecto en un momento específico. Es como guardar un punto de control en un videojuego.

### Branch (Rama)
Una rama es una línea de desarrollo independiente. Por defecto, todos los proyectos tienen una rama llamada `main` o `master`.

### Remote (Remoto)
Es la versión de tu repositorio que está en GitHub (o en otro servidor).

### Clone
Copiar un repositorio completo desde GitHub a tu computadora.

### Pull
Descargar cambios desde GitHub a tu computadora.

### Push
Subir cambios desde tu computadora a GitHub.

---

## Comandos esenciales de Git

### Configuración inicial (solo una vez)

```bash
# Configurar tu nombre
git config --global user.name "Tu Nombre"

# Configurar tu email
git config --global user.email "tu-email@ejemplo.com"

# Ver tu configuración
git config --list
```

### Comandos básicos

```bash
# Inicializar un repositorio Git en una carpeta
git init

# Ver el estado de tus archivos
git status

# Agregar archivos al área de staging
git add nombre-archivo.txt
git add .                    # Agregar todos los archivos

# Crear un commit (guardar cambios)
git commit -m "Mensaje descriptivo del cambio"

# Ver el historial de commits
git log
git log --oneline           # Versión compacta

# Conectar tu repositorio local con GitHub
git remote add origin https://github.com/tu-usuario/tu-repo.git

# Subir cambios a GitHub
git push -u origin main

# Descargar cambios desde GitHub
git pull

# Clonar un repositorio
git clone https://github.com/usuario/repositorio.git
```

---

## Flujo de trabajo básico

### 1. Trabajar en tu proyecto localmente

```bash
# 1. Crear o modificar archivos en tu proyecto
# (Editas archivos normalmente)

# 2. Ver qué archivos han cambiado
git status

# 3. Agregar los archivos que quieres guardar
git add .

# 4. Crear un commit con un mensaje descriptivo
git commit -m "Agregar funcionalidad de login"

# 5. Subir los cambios a GitHub
git push
```

### Diagrama del flujo

```
┌─────────────┐     git add      ┌──────────────┐     git commit     ┌─────────────┐
│ Working     │ ───────────────> │ Staging Area │ ─────────────────> │ Repository  │
│ Directory   │                  │ (Index)      │                    │ (Local)     │
└─────────────┘                  └──────────────┘                    └─────────────┘
                                                                              │
                                                                              │ git push
                                                                              ▼
                                                                      ┌─────────────┐
                                                                      │   GitHub    │
                                                                      │  (Remote)   │
                                                                      └─────────────┘
```

---

## Crear tu primer repositorio

### Paso 1: Crear repositorio en GitHub

1. Ve a https://github.com/
2. Haz clic en el botón **"+"** (arriba a la derecha) → **"New repository"**
3. Completa:
   - **Repository name**: `mi-primer-proyecto-spring`
   - **Description**: "Mi primer proyecto Spring Boot"
   - **Visibility**: Público o Privado
   - **NO marques** "Initialize this repository with a README"
4. Haz clic en **"Create repository"**

### Paso 2: Conectar tu proyecto local con GitHub

```bash
# 1. Navega a la carpeta de tu proyecto
cd ruta/a/tu/proyecto

# 2. Inicializa Git (si no lo has hecho)
git init

# 3. Agrega todos los archivos
git add .

# 4. Crea el primer commit
git commit -m "Initial commit"

# 5. Conecta con GitHub (copia la URL de tu repositorio)
git remote add origin https://github.com/tu-usuario/mi-primer-proyecto-spring.git

# 6. Cambia el nombre de la rama a 'main' (si es necesario)
git branch -M main

# 7. Sube tu código
git push -u origin main
```

### Paso 3: Verificar

Ve a tu repositorio en GitHub y deberías ver todos tus archivos.

---

## Clonar un repositorio

Clonar significa copiar un repositorio completo desde GitHub a tu computadora.

### Método 1: HTTPS (más fácil)

```bash
# Clonar un repositorio
git clone https://github.com/usuario/nombre-repo.git

# Entrar a la carpeta
cd nombre-repo
```

### Método 2: SSH (más seguro, requiere configuración previa)

```bash
git clone git@github.com:usuario/nombre-repo.git
```

### Ejemplo práctico

```bash
# Clonar el repositorio de Spring Boot
git clone https://github.com/spring-projects/spring-boot.git

# Entrar al proyecto
cd spring-boot
```

---

## Ramas (Branches)

Las ramas te permiten trabajar en diferentes versiones de tu proyecto simultáneamente.

### Comandos de ramas

```bash
# Ver todas las ramas
git branch

# Crear una nueva rama
git branch nombre-rama

# Cambiar a una rama
git checkout nombre-rama

# Crear y cambiar a una rama (comando combinado)
git checkout -b nombre-rama

# En Git 2.23+ puedes usar:
git switch nombre-rama
git switch -c nombre-rama  # Crear y cambiar

# Subir una rama a GitHub
git push -u origin nombre-rama

# Eliminar una rama local
git branch -d nombre-rama

# Eliminar una rama en GitHub
git push origin --delete nombre-rama
```

### Ejemplo de uso

```bash
# Estás trabajando en la rama main
git checkout -b feature/login

# Haces cambios y commits
git add .
git commit -m "Agregar formulario de login"

# Subes la rama
git push -u origin feature/login

# Vuelves a main
git checkout main
```

---

## Buenas prácticas

### 1. Mensajes de commit descriptivos

❌ **Mal:**
```bash
git commit -m "cambios"
git commit -m "fix"
git commit -m "update"
```

✅ **Bien:**
```bash
git commit -m "Agregar autenticación JWT al sistema"
git commit -m "Corregir error al validar email del usuario"
git commit -m "Actualizar dependencias de Spring Boot a versión 3.1"
```

### 2. Hacer commits frecuentes

- Haz commits pequeños y frecuentes
- Cada commit debe representar un cambio lógico completo
- Es mejor tener muchos commits pequeños que uno grande

### 3. Usar .gitignore

Crea un archivo `.gitignore` en la raíz de tu proyecto para excluir archivos que no deben subirse:

```gitignore
# Archivos compilados
target/
*.class
*.jar
*.war

# IDEs
.idea/
.vscode/
*.iml

# Logs
*.log

# Variables de entorno
.env
application-local.properties

# Sistema operativo
.DS_Store
Thumbs.db
```

### 4. Pull antes de Push

Siempre descarga los últimos cambios antes de subir los tuyos:

```bash
git pull
git push
```

### 5. Revisar antes de commitear

```bash
# Ver qué archivos vas a commitear
git status

# Ver los cambios exactos
git diff
```

---

## Comandos útiles adicionales

```bash
# Ver diferencias entre commits
git diff

# Deshacer cambios en un archivo (antes de git add)
git checkout -- nombre-archivo.txt

# Quitar archivos del staging (después de git add, antes de commit)
git reset nombre-archivo.txt

# Modificar el último commit (si olvidaste algo)
git add archivo-olvidado.txt
git commit --amend

# Ver quién hizo qué cambios
git blame nombre-archivo.txt

# Crear una copia de seguridad de tu trabajo
git stash                    # Guardar cambios temporalmente
git stash pop                # Recuperar cambios guardados
```

---

## Flujo de trabajo recomendado para el curso

### Para cada proyecto del curso:

1. **Crear repositorio en GitHub**
   ```bash
   # Desde GitHub web interface
   ```

2. **Clonar o inicializar localmente**
   ```bash
   git clone https://github.com/tu-usuario/nombre-proyecto.git
   # O
   git init
   git remote add origin https://github.com/tu-usuario/nombre-proyecto.git
   ```

3. **Trabajar en el proyecto**
   ```bash
   # Hacer cambios...
   git add .
   git commit -m "Descripción clara del cambio"
   git push
   ```

4. **Mantener sincronizado**
   ```bash
   # Antes de empezar a trabajar cada día
   git pull
   ```

---

## Recursos adicionales

- **Documentación oficial de Git**: https://git-scm.com/doc
- **GitHub Guides**: https://guides.github.com/
- **Git Cheat Sheet**: https://education.github.com/git-cheat-sheet-education.pdf
- **Visual Git Guide**: https://learngitbranching.js.org/

---

## 🎯 Práctica recomendada

1. Crea un repositorio de prueba en GitHub
2. Clónalo en tu computadora
3. Crea un archivo `README.md` con tu nombre
4. Haz commit y push
5. Crea una rama llamada `test`
6. Modifica el README en esa rama
7. Haz commit y push de la rama
8. Vuelve a `main` y observa la diferencia

---

## ❓ Preguntas frecuentes

### ¿Qué pasa si olvidé hacer commit y cerré la computadora?
- Tus cambios están seguros mientras no elimines la carpeta
- Simplemente vuelve a abrir y haz `git status` para ver los cambios

### ¿Puedo deshacer un commit?
- Sí, con `git reset` (pero ten cuidado, esto puede ser destructivo)
- Es mejor crear un nuevo commit que corrija el error

### ¿Qué es un merge conflict?
- Ocurre cuando Git no puede combinar automáticamente los cambios
- Necesitas resolverlo manualmente editando los archivos en conflicto

### ¿Debo hacer commit de archivos de configuración?
- Depende: archivos de configuración del proyecto sí, archivos personales no
- Usa `.gitignore` para excluir archivos sensibles o personales

---

**¡Ahora estás listo para usar Git y GitHub en el curso!** 🚀

**Siguiente paso**: [Módulo 1 - Fundamentos de Spring Boot](./02-MODULO-1-FUNDAMENTOS.md)

