package com.biblioteca.strategy;

import com.biblioteca.model.Libro;
import reactor.core.publisher.Flux;

public class SearchByTipo implements SearchStrategy {
    @Override
    public Flux<Libro> buscar(Flux<Libro> libros, String criterio) {
        return libros.filter(libro ->
            libro.getTipo().toString().toLowerCase().contains(criterio.toLowerCase())
        );
    }
}