package com.biblioteca.config;

import com.biblioteca.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Patrón Singleton - Base de datos en memoria con datos pre-poblados
 * Esta clase simula una base de datos singleton que mantiene un catálogo
 * de libros pre-cargados y permite operaciones CRUD thread-safe
 */
public class LibraryDatabase {
    private static volatile LibraryDatabase instance;
    private final Map<Long, Libro> libros;
    private final AtomicLong contadorId;
    private static final Object lock = new Object();

    // Constructor privado - patrón Singleton
    private LibraryDatabase() {
        this.libros = new ConcurrentHashMap<>();
        this.contadorId = new AtomicLong(1);
        inicializarDatos();
        System.out.println("✓ LibraryDatabase Singleton inicializada con " + libros.size() + " libros");
    }

    /**
     * Implementación thread-safe del patrón Singleton usando Double-Checked Locking
     */
    public static LibraryDatabase getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LibraryDatabase();
                }
            }
        }
        return instance;
    }

    /**
     * Inicializa la base de datos con datos pre-poblados
     */
    private void inicializarDatos() {
        // Libros de Ficción
        agregarLibro(crearLibro("Cien años de soledad", "Gabriel García Márquez", 
            TipoLibro.FICCION, FormatoLibro.FISICO, "Obra maestra del realismo mágico"));
        
        agregarLibro(crearLibro("1984", "George Orwell", 
            TipoLibro.FICCION, FormatoLibro.DIGITAL, "Distopía clásica sobre totalitarismo"));
        
        agregarLibro(crearLibro("El Principito", "Antoine de Saint-Exupéry", 
            TipoLibro.FICCION, FormatoLibro.FISICO, "Fábula filosófica universal"));
        
        agregarLibro(crearLibro("Dune", "Frank Herbert", 
            TipoLibro.FICCION, FormatoLibro.DIGITAL, "Épica de ciencia ficción"));

        // Libros de No Ficción
        agregarLibro(crearLibro("Sapiens", "Yuval Noah Harari", 
            TipoLibro.NO_FICCION, FormatoLibro.FISICO, "Historia de la humanidad"));
        
        agregarLibro(crearLibro("Clean Code", "Robert C. Martin", 
            TipoLibro.NO_FICCION, FormatoLibro.DIGITAL, "Guía para escribir código limpio"));
        
        agregarLibro(crearLibro("El Arte de la Guerra", "Sun Tzu", 
            TipoLibro.NO_FICCION, FormatoLibro.FISICO, "Tratado militar clásico"));
        
        agregarLibro(crearLibro("Thinking, Fast and Slow", "Daniel Kahneman", 
            TipoLibro.NO_FICCION, FormatoLibro.DIGITAL, "Psicología del pensamiento"));
    }

    /**
     * Método auxiliar para crear libros usando el patrón Builder
     */
    private Libro crearLibro(String titulo, String autor, TipoLibro tipo, 
                            FormatoLibro formato, String descripcion) {
        return Libro.builder()
            .titulo(titulo)
            .autor(autor)
            .tipo(tipo)
            .formato(formato)
            .descripcion(descripcion)
            .estado(EstadoLibro.DISPONIBLE)
            .build();
    }

    /**
     * Agrega un libro a la base de datos
     */
    public synchronized Long agregarLibro(Libro libro) {
        Long id = contadorId.getAndIncrement();
        libros.put(id, libro);
        return id;
    }

    /**
     * Obtiene un libro por ID
     */
    public Optional<Libro> obtenerLibroPorId(Long id) {
        return Optional.ofNullable(libros.get(id));
    }

    /**
     * Obtiene todos los libros
     */
    public List<Libro> obtenerTodosLosLibros() {
        return new ArrayList<>(libros.values());
    }

    /**
     * Busca libros por título (búsqueda parcial, case-insensitive)
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        return libros.values().stream()
            .filter(libro -> libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .toList();
    }

    /**
     * Busca libros por autor (búsqueda parcial, case-insensitive)
     */
    public List<Libro> buscarPorAutor(String autor) {
        return libros.values().stream()
            .filter(libro -> libro.getAutor().toLowerCase().contains(autor.toLowerCase()))
            .toList();
    }

    /**
     * Obtiene libros por tipo
     */
    public List<Libro> obtenerLibrosPorTipo(TipoLibro tipo) {
        return libros.values().stream()
            .filter(libro -> libro.getTipo() == tipo)
            .toList();
    }

    /**
     * Obtiene libros por formato
     */
    public List<Libro> obtenerLibrosPorFormato(FormatoLibro formato) {
        return libros.values().stream()
            .filter(libro -> libro.getFormato() == formato)
            .toList();
    }

    /**
     * Obtiene estadísticas de la biblioteca
     */
    public String obtenerEstadisticas() {
        long totalLibros = libros.size();
        long librosFiccion = libros.values().stream()
            .filter(libro -> libro.getTipo() == TipoLibro.FICCION)
            .count();
        long librosNoFiccion = libros.values().stream()
            .filter(libro -> libro.getTipo() == TipoLibro.NO_FICCION)
            .count();
        long librosFisicos = libros.values().stream()
            .filter(libro -> libro.getFormato() == FormatoLibro.FISICO)
            .count();
        long librosDigitales = libros.values().stream()
            .filter(libro -> libro.getFormato() == FormatoLibro.DIGITAL)
            .count();

        return String.format(
            "📊 ESTADÍSTICAS DE LA BIBLIOTECA:\n" +
            "   Total de libros: %d\n" +
            "   Ficción: %d | No Ficción: %d\n" +
            "   Físicos: %d | Digitales: %d",
            totalLibros, librosFiccion, librosNoFiccion, librosFisicos, librosDigitales
        );
    }

    /**
     * Actualiza el estado de un libro
     */
    public synchronized boolean actualizarEstadoLibro(Long id, EstadoLibro nuevoEstado) {
        Libro libro = libros.get(id);
        if (libro != null) {
            // Como Libro es inmutable, necesitamos crear una nueva instancia
            Libro libroActualizado = Libro.builder()
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .tipo(libro.getTipo())
                .formato(libro.getFormato())
                .descripcion(libro.getDescripcion())
                .estado(nuevoEstado)
                .build();
            libros.put(id, libroActualizado);
            return true;
        }
        return false;
    }

    /**
     * Obtiene el número total de libros
     */
    public int getTotalLibros() {
        return libros.size();
    }

    /**
     * Método para demostrar que siempre es la misma instancia
     */
    public String getInstanceInfo() {
        return "LibraryDatabase Singleton - Hash: " + this.hashCode() + 
               " | Libros en memoria: " + libros.size();
    }
}