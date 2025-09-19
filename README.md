# Sistema de Gestión de Biblioteca

## Descripción

Este proyecto implementa un sistema completo de gestión de biblioteca en Java 21 con Spring Boot, utilizando una base de datos en memoria H2. El sistema demuestra la implementación de todos los patrones de diseño requeridos:

### Patrones de Diseño Implementados

1. **Singleton** - `DatabaseConnection`: Garantiza una única instancia de conexión a la base de datos
2. **Factory Method** - `LibroFactory`: Crea diferentes tipos de libros según su categoría
3. **Abstract Factory** - `AbstractLibroFactory`: Crea familias de productos (Físicos/Digitales)
4. **Builder** - `LibroBuilder`: Construcción flexible y legible de objetos Libro
5. **Strategy** - `SearchStrategy`: Diferentes estrategias de búsqueda (título, autor, tipo)
6. **Observer** - `PrestamoObserver`: Notifica cambios en el estado de los libros
7. **Decorator** - `PrestamoDecorator`: Añade funcionalidad de préstamo sin modificar la clase base
8. **Chain of Responsibility** - `Validador`: Validación en cadena de datos de libros
9. **Adapter** - `LegacyLibroAdapter`: Adapta libros de sistemas legacy

## Modelo de Datos

La clase `Libro` contiene todos los atributos requeridos:
- `id`: Identificador único
- `titulo`: Título del libro (validado)
- `autor`: Autor del libro (validado)
- `tipo`: FICCION o NO_FICCION
- `formato`: FISICO o DIGITAL
- `estado`: DISPONIBLE o PRESTADO
- `descripcion`: Descripción adicional

## Funcionalidades

### API REST Endpoints

- `POST /api/libros/agregar` - Agregar libro usando Factory Method
- `POST /api/libros/agregar/abstract-factory` - Agregar libro usando Abstract Factory
- `POST /api/libros/agregar/builder` - Agregar libro usando Builder
- `GET /api/libros/buscar` - Buscar libros (Strategy Pattern)
- `GET /api/libros/todos` - Listar todos los libros
- `POST /api/libros/{id}/prestar` - Prestar libro (Observer + Decorator)
- `POST /api/libros/{id}/devolver` - Devolver libro

### Validaciones Implementadas

- Título no vacío (mínimo 2 caracteres)
- Autor válido (solo letras y espacios, mínimo 2 caracteres)
- Campos obligatorios (tipo y formato)

## Principios SOLID Aplicados

1. **Single Responsibility** - Cada clase tiene una responsabilidad específica
2. **Open/Closed** - Extensible sin modificar código existente (Factory, Strategy)
3. **Liskov Substitution** - Implementaciones intercambiables (ILibro)
4. **Interface Segregation** - Interfaces específicas y cohesivas
5. **Dependency Inversion** - Dependencias en abstracciones, no implementaciones

## Clean Code

- Nombres descriptivos y significativos
- Métodos pequeños y con una sola responsabilidad
- Comentarios explicativos en patrones complejos
- Manejo adecuado de excepciones
- Recursos gestionados automáticamente (try-with-resources mediante Spring)

## Tecnologías Utilizadas

- **Java 21** - Versión LTS más reciente
- **Spring Boot 3.1.5** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring Data R2DBC** - Acceso reactivo a base de datos
- **H2 Database** - Base de datos en memoria
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias


## Instalación y Ejecución

### Prerrequisitos
- Java 21 o superior
- Maven 3.6+

### Pasos para ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd library-spring
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar tests**
   ```bash
   mvn test
   ```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

5. **Ejecutar demo de patrones**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.main-class=com.biblioteca.demo.PatternDemoRunner
   ```

### Acceso a la Aplicación

- **API REST**: `http://localhost:8080/api/libros`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:biblioteca`
  - Username: `sa`
  - Password: (vacío)

## Ejemplos de Uso

### Agregar un libro
```bash
curl -X POST "http://localhost:8080/api/libros/agregar" \
  -d "titulo=1984" \
  -d "autor=George Orwell" \
  -d "tipo=FICCION" \
  -d "formato=DIGITAL"
```

### Buscar libros
```bash
curl "http://localhost:8080/api/libros/buscar?criterio=Orwell&tipoBusqueda=autor"
```

### Prestar un libro
```bash
curl -X POST "http://localhost:8080/api/libros/1/prestar?prestatario=Juan Perez"
```

## Demo de Patrones

El proyecto incluye una clase `PatternDemoRunner` que demuestra todos los patrones implementados:

```bash
mvn spring-boot:run -Dspring-boot.run.main-class=com.biblioteca.demo.PatternDemoRunner
```

Esta demo muestra:
- Creación de libros con diferentes patrones de factory
- Búsqueda con diferentes estrategias
- Préstamo con observadores y decoradores
- Validación en cadena
- Adaptación de sistemas legacy

## Manejo de Errores

- Validaciones automáticas con Chain of Responsibility
- Excepciones personalizadas (`ValidationException`)
- Respuestas HTTP apropiadas
- Logging detallado para debugging

## Base de Datos

La base de datos H2 se inicializa automáticamente con datos de ejemplo:
- Libros de ficción y no ficción
- Diferentes formatos (físico/digital)
- Estados variados (disponible/prestado)

```sql
-- Ver datos en H2 Console
SELECT * FROM libros;
```
