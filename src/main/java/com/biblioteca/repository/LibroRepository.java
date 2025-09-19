package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LibroRepository extends R2dbcRepository<Libro, Long> {
    Flux<Libro> findByTituloContainingIgnoreCase(String titulo);
    Flux<Libro> findByAutorContainingIgnoreCase(String autor);
}
