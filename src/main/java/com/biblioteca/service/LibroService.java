package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.factory.LibroFactory;
import com.biblioteca.factory.AbstractLibroFactory;
import com.biblioteca.factory.LibroFisicoFactory;
import com.biblioteca.factory.LibroDigitalFactory;
import com.biblioteca.builder.LibroBuilder;
import com.biblioteca.strategy.SearchStrategy;
import com.biblioteca.strategy.SearchByTitle;
import com.biblioteca.strategy.SearchByAuthor;
import com.biblioteca.strategy.SearchByTipo;
import com.biblioteca.validator.Validador;
import com.biblioteca.validator.ValidadorTitulo;
import com.biblioteca.validator.ValidadorAutor;
import com.biblioteca.validator.ValidadorCamposObligatorios;
import com.biblioteca.validator.ValidationException;
import com.biblioteca.decorator.PrestamoDecorator;
import com.biblioteca.observer.PrestamoObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    private final Validador validadorChain;

    public LibroService() {
        // Configurar Chain of Responsibility
        ValidadorTitulo validadorTitulo = new ValidadorTitulo();
        ValidadorAutor validadorAutor = new ValidadorAutor();
        ValidadorCamposObligatorios validadorCampos = new ValidadorCamposObligatorios();

        validadorTitulo.setSiguiente(validadorAutor);
        validadorAutor.setSiguiente(validadorCampos);

        this.validadorChain = validadorTitulo;
    }

    // Agregar libro usando Factory Method y Builder
    public Mono<Libro> agregarLibro(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
        return Mono.fromCallable(() -> {
            // Usar Factory Method
            LibroFactory factory = LibroFactory.getFactory(tipo);
            Libro libro = factory.crearLibro(titulo, autor, formato);

            // Validar usando Chain of Responsibility
            try {
                validadorChain.validar(libro);
            } catch (ValidationException e) {
                throw new RuntimeException("Error de validación: " + e.getMessage());
            }

            return libro;
        }).flatMap(libroRepository::save);
    }

    // Agregar libro usando Abstract Factory
    public Mono<Libro> agregarLibroConAbstractFactory(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
        return Mono.fromCallable(() -> {
            AbstractLibroFactory abstractFactory = formato == FormatoLibro.FISICO
                ? new LibroFisicoFactory()
                : new LibroDigitalFactory();

            Libro libro = tipo == TipoLibro.FICCION
                ? abstractFactory.crearLibroFiccion(titulo, autor)
                : abstractFactory.crearLibroNoFiccion(titulo, autor);

            // Validar usando Chain of Responsibility
            try {
                validadorChain.validar(libro);
            } catch (ValidationException e) {
                throw new RuntimeException("Error de validación: " + e.getMessage());
            }

            return libro;
        }).flatMap(libroRepository::save);
    }

    // Agregar libro usando Builder
    public Mono<Libro> agregarLibroConBuilder(String titulo, String autor, TipoLibro tipo, FormatoLibro formato) {
        return Mono.fromCallable(() -> {
            Libro libro = new LibroBuilder()
                .withTitulo(titulo)
                .withAutor(autor)
                .withTipo(tipo)
                .withFormato(formato)
                .build();

            // Validar usando Chain of Responsibility
            try {
                validadorChain.validar(libro);
            } catch (ValidationException e) {
                throw new RuntimeException("Error de validación: " + e.getMessage());
            }

            return libro;
        }).flatMap(libroRepository::save);
    }

    // Buscar libros usando Strategy Pattern
    public Flux<Libro> buscarLibros(String criterio, String tipoBusqueda) {
        SearchStrategy strategy = switch (tipoBusqueda.toLowerCase()) {
            case "titulo" -> new SearchByTitle();
            case "autor" -> new SearchByAuthor();
            case "tipo" -> new SearchByTipo();
            default -> new SearchByTitle();
        };

        return strategy.buscar(libroRepository.findAll(), criterio);
    }

    // Listar todos los libros
    public Flux<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    // Prestar libro usando Decorator y Observer
    public Mono<String> prestarLibro(Long libroId, String prestatario) {
        return libroRepository.findById(libroId)
            .switchIfEmpty(Mono.error(new RuntimeException("Libro no encontrado")))
            .map(libro -> {
                // Usar Decorator para añadir funcionalidad de préstamo
                PrestamoDecorator decorator = new PrestamoDecorator(libro);

                // Añadir Observer para notificaciones
                decorator.addObserver(new PrestamoObserver("Sistema de Biblioteca"));
                decorator.addObserver(new PrestamoObserver("Registro de Préstamos"));

                try {
                    decorator.prestar(prestatario);
                    return "Libro prestado exitosamente a " + prestatario;
                } catch (IllegalStateException e) {
                    throw new RuntimeException(e.getMessage());
                }
            })
            .flatMap(result -> libroRepository.findById(libroId)
                .flatMap(libroRepository::save)
                .thenReturn(result));
    }

    // Devolver libro
    public Mono<String> devolverLibro(Long libroId) {
        return libroRepository.findById(libroId)
            .switchIfEmpty(Mono.error(new RuntimeException("Libro no encontrado")))
            .map(libro -> {
                PrestamoDecorator decorator = new PrestamoDecorator(libro);
                decorator.addObserver(new PrestamoObserver("Sistema de Biblioteca"));

                try {
                    decorator.devolver();
                    return "Libro devuelto exitosamente";
                } catch (IllegalStateException e) {
                    throw new RuntimeException(e.getMessage());
                }
            })
            .flatMap(result -> libroRepository.findById(libroId)
                .flatMap(libroRepository::save)
                .thenReturn(result));
    }
}

