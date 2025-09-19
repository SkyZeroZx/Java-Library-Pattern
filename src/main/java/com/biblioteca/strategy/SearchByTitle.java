package com.biblioteca.strategy;

import com.biblioteca.model.Libro;
import reactor.core.publisher.Flux;

public class SearchByTitle implements SearchStrategy {
    @Override
    public Flux<Libro> buscar(Flux<Libro> libros, String criterio) {
        return libros.filter(libro ->
            libro.getTitulo().toLowerCase().contains(criterio.toLowerCase())
        );
    }
}