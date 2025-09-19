package com.biblioteca.strategy;

import com.biblioteca.model.Libro;
import reactor.core.publisher.Flux;

// Patrón Strategy - Interfaz para diferentes estrategias de búsqueda
public interface SearchStrategy {
    Flux<Libro> buscar(Flux<Libro> libros, String criterio);
}


