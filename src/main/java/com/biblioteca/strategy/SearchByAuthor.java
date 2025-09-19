package com.biblioteca.strategy;

import com.biblioteca.model.Libro;
import reactor.core.publisher.Flux;

public class SearchByAuthor implements SearchStrategy {
    @Override
    public Flux<Libro> buscar(Flux<Libro> libros, String criterio) {
        return libros.filter(libro ->
            libro.getAutor().toLowerCase().contains(criterio.toLowerCase())
        );
    }
}