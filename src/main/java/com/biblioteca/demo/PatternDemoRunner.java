package com.biblioteca.demo;

import com.biblioteca.model.*;
import com.biblioteca.factory.*;
import com.biblioteca.builder.LibroBuilder;
import com.biblioteca.strategy.*;
import com.biblioteca.decorator.PrestamoDecorator;
import com.biblioteca.observer.PrestamoObserver;
import com.biblioteca.validator.*;
import com.biblioteca.adapter.LegacyLibroAdapter;
import com.biblioteca.config.LibraryDatabase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.List;

@Component
public class PatternDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DEMOSTRACIÓN DE PATRONES DE DISEÑO ===\n");

        // 1. Singleton Pattern - LibraryDatabase con datos pre-poblados
        System.out.println("1. PATRÓN SINGLETON - Base de datos con datos pre-poblados");
        
        // Demostrar que siempre obtenemos la misma instancia
        LibraryDatabase db1 = LibraryDatabase.getInstance();
        LibraryDatabase db2 = LibraryDatabase.getInstance();
        LibraryDatabase db3 = LibraryDatabase.getInstance();
        
        System.out.println("✓ Verificando instancia única:");
        System.out.println("  db1 == db2: " + (db1 == db2));
        System.out.println("  db2 == db3: " + (db2 == db3));
        System.out.println("  " + db1.getInstanceInfo());
        
        // Mostrar estadísticas de los datos pre-poblados
        System.out.println("\n" + db1.obtenerEstadisticas());
        
        // Demostrar recuperación de datos pre-poblados
        System.out.println("\n📚 CATÁLOGO COMPLETO DE LIBROS PRE-POBLADOS:");
        List<Libro> catalogoCompleto = db1.obtenerTodosLosLibros();
        catalogoCompleto.forEach(libro -> 
            System.out.println("  • " + libro.getTitulo() + " - " + libro.getAutor() + 
                             " [" + libro.getTipo() + "/" + libro.getFormato() + "]")
        );
        
        // Demostrar búsquedas en datos pre-poblados
        System.out.println("\n🔍 BÚSQUEDAS EN DATOS PRE-POBLADOS:");
        
        // Búsqueda por título
        System.out.println("Libros con 'Clean' en el título:");
        db1.buscarPorTitulo("Clean").forEach(libro -> 
            System.out.println("  → " + libro.getTitulo() + " - " + libro.getAutor())
        );
        
        // Búsqueda por autor
        System.out.println("Libros de autores con 'García':");
        db1.buscarPorAutor("García").forEach(libro -> 
            System.out.println("  → " + libro.getTitulo() + " - " + libro.getAutor())
        );
        
        // Libros por tipo
        System.out.println("Libros de Ficción:");
        db1.obtenerLibrosPorTipo(TipoLibro.FICCION).forEach(libro -> 
            System.out.println("  → " + libro.getTitulo())
        );
        
        // Libros por formato
        System.out.println("Libros Digitales:");
        db1.obtenerLibrosPorFormato(FormatoLibro.DIGITAL).forEach(libro -> 
            System.out.println("  → " + libro.getTitulo())
        );
        
        System.out.println();

        // 2. Factory Method Pattern
        System.out.println("2. PATRÓN FACTORY METHOD");
        LibroFactory ficcionFactory = LibroFactory.getFactory(TipoLibro.FICCION);
        Libro libroFiccion = ficcionFactory.crearLibro("El Señor de los Anillos", "J.R.R. Tolkien", FormatoLibro.FISICO);
        System.out.println("Creado con Factory Method: " + libroFiccion.getDescripcion());

        LibroFactory noFiccionFactory = LibroFactory.getFactory(TipoLibro.NO_FICCION);
        Libro libroNoFiccion = noFiccionFactory.crearLibro("Breve Historia del Tiempo", "Stephen Hawking", FormatoLibro.DIGITAL);
        System.out.println("Creado con Factory Method: " + libroNoFiccion.getDescripcion() + "\n");

        // 3. Abstract Factory Pattern
        System.out.println("3. PATRÓN ABSTRACT FACTORY");
        AbstractLibroFactory fisicoFactory = new LibroFisicoFactory();
        Libro libroFisicoFiccion = fisicoFactory.crearLibroFiccion("Dune", "Frank Herbert");
        System.out.println("Creado con Abstract Factory (Físico): " + libroFisicoFiccion.getDescripcion());

        AbstractLibroFactory digitalFactory = new LibroDigitalFactory();
        Libro libroDigitalEducativo = digitalFactory.crearLibroNoFiccion("Clean Code", "Robert C. Martin");
        System.out.println("Creado con Abstract Factory (Digital): " + libroDigitalEducativo.getDescripcion() + "\n");

        // 4. Builder Pattern
        System.out.println("4. PATRÓN BUILDER");
        Libro libroBuilder = new LibroBuilder()
            .withTitulo("Fundación")
            .withAutor("Isaac Asimov")
            .withTipo(TipoLibro.FICCION)
            .withFormato(FormatoLibro.DIGITAL)
            .withDescripcion("Serie de ciencia ficción épica")
            .build();
        System.out.println("Creado con Builder: " + libroBuilder.getDescripcion() + "\n");

        // 5. Strategy Pattern
        System.out.println("5. PATRÓN STRATEGY");
        Flux<Libro> todosLosLibros = Flux.just(libroFiccion, libroNoFiccion, libroFisicoFiccion);

        SearchStrategy strategyTitulo = new SearchByTitle();
        SearchStrategy strategyAutor = new SearchByAuthor();

        System.out.println("Búsqueda por título 'Señor':");
        strategyTitulo.buscar(todosLosLibros, "Señor")
            .subscribe(libro -> System.out.println("- " + libro.getTitulo()));

        System.out.println("Búsqueda por autor 'Tolkien':");
        strategyAutor.buscar(todosLosLibros, "Tolkien")
            .subscribe(libro -> System.out.println("- " + libro.getAutor() + "\n"));

        // 6. Observer Pattern + Decorator Pattern
        System.out.println("6. PATRÓN OBSERVER + DECORATOR");
        PrestamoDecorator decorator = new PrestamoDecorator(libroFiccion);
        decorator.addObserver(new PrestamoObserver("Bibliotecario"));
        decorator.addObserver(new PrestamoObserver("Sistema de Notificaciones"));

        System.out.println("Prestando libro...");
        decorator.prestar("Juan Pérez");
        System.out.println("Estado después del préstamo: " + decorator.getDescripcion());

        System.out.println("\nDevolviendo libro...");
        decorator.devolver();
        System.out.println("Estado después de la devolución: " + decorator.getDescripcion() + "\n");

        // 7. Chain of Responsibility Pattern
        System.out.println("7. PATRÓN CHAIN OF RESPONSIBILITY");
        ValidadorTitulo validadorTitulo = new ValidadorTitulo();
        ValidadorAutor validadorAutor = new ValidadorAutor();
        ValidadorCamposObligatorios validadorCampos = new ValidadorCamposObligatorios();

        validadorTitulo.setSiguiente(validadorAutor);
        validadorAutor.setSiguiente(validadorCampos);

        try {
            Libro libroValido = Libro.builder()
                .titulo("Libro Válido")
                .autor("Autor Válido")
                .tipo(TipoLibro.FICCION)
                .formato(FormatoLibro.FISICO)
                .build();

            validadorTitulo.validar(libroValido);
            System.out.println("✓ Validación exitosa para: " + libroValido.getTitulo());

            Libro libroInvalido = Libro.builder()
                .titulo("") // Título vacío - debe fallar
                .autor("Autor")
                .tipo(TipoLibro.FICCION)
                .formato(FormatoLibro.FISICO)
                .build();

            validadorTitulo.validar(libroInvalido);
        } catch (ValidationException e) {
            System.out.println("✗ Error de validación: " + e.getMessage());
        }
        System.out.println();

        // 8. Adapter Pattern
        System.out.println("8. PATRÓN ADAPTER");
        // Simulando un libro del sistema legacy
        LegacyLibroAdapter.LegacyLibro legacyLibro = 
            LegacyLibroAdapter.LegacyLibro.createBook(
                "Don Quixote", "Cervantes", "fiction", true);

        LegacyLibroAdapter adapter = new LegacyLibroAdapter(legacyLibro, 999L);
        System.out.println("Libro legacy adaptado: " + adapter.getDescripcion());
        System.out.println("Tipo adaptado: " + adapter.getTipo());
        System.out.println("Estado adaptado: " + adapter.getEstado() + "\n");

        // Demostración final del Singleton - persistencia de datos
        System.out.println("🔄 DEMOSTRACIÓN FINAL DEL SINGLETON:");
        System.out.println("Verificando persistencia de datos entre múltiples accesos...");
        
        // Acceder desde diferentes puntos y verificar que los datos persisten
        LibraryDatabase dbFinal1 = LibraryDatabase.getInstance();
        System.out.println("Acceso 1 - Total libros: " + dbFinal1.getTotalLibros());
        
        LibraryDatabase dbFinal2 = LibraryDatabase.getInstance();
        System.out.println("Acceso 2 - Total libros: " + dbFinal2.getTotalLibros());
        
        // Agregar un nuevo libro y verificar que se mantiene en todas las instancias
        Libro nuevoLibro = Libro.builder()
            .titulo("Nuevo Libro de Demo")
            .autor("Autor Demo")
            .tipo(TipoLibro.FICCION)
            .formato(FormatoLibro.DIGITAL)
            .descripcion("Libro agregado durante la demo")
            .estado(EstadoLibro.DISPONIBLE)
            .build();
            
        Long idNuevo = dbFinal1.agregarLibro(nuevoLibro);
        System.out.println("Libro agregado con ID: " + idNuevo);
        
        LibraryDatabase dbFinal3 = LibraryDatabase.getInstance();
        System.out.println("Acceso 3 - Total libros: " + dbFinal3.getTotalLibros());
        System.out.println("Libro recién agregado: " + 
            dbFinal3.obtenerLibroPorId(idNuevo).map(Libro::getTitulo).orElse("No encontrado"));

        System.out.println("\n=== DEMOSTRACIÓN COMPLETA ===");
        System.out.println("Todos los patrones de diseño han sido implementados y demostrados:");
        System.out.println("✓ Singleton - Base de datos en memoria con datos pre-poblados");
        System.out.println("✓ Factory Method - Creación de libros por tipo");
        System.out.println("✓ Abstract Factory - Familias de productos (Físico/Digital)");
        System.out.println("✓ Builder - Construcción flexible de objetos Libro");
        System.out.println("✓ Strategy - Diferentes estrategias de búsqueda");
        System.out.println("✓ Observer - Notificaciones de cambios de estado");
        System.out.println("✓ Decorator - Funcionalidad de préstamo añadida");
        System.out.println("✓ Chain of Responsibility - Validación en cadena");
        System.out.println("✓ Adapter - Integración de sistema legacy");
    }
}

