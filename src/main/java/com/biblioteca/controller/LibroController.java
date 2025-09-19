package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping("/agregar")
    public Mono<Libro> agregarLibro(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam TipoLibro tipo,
            @RequestParam FormatoLibro formato) {
        return libroService.agregarLibro(titulo, autor, tipo, formato);
    }

    @PostMapping("/agregar/abstract-factory")
    public Mono<Libro> agregarLibroConAbstractFactory(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam TipoLibro tipo,
            @RequestParam FormatoLibro formato) {
        return libroService.agregarLibroConAbstractFactory(titulo, autor, tipo, formato);
    }

    @PostMapping("/agregar/builder")
    public Mono<Libro> agregarLibroConBuilder(
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam TipoLibro tipo,
            @RequestParam FormatoLibro formato) {
        return libroService.agregarLibroConBuilder(titulo, autor, tipo, formato);
    }

    @GetMapping("/buscar")
    public Flux<Libro> buscarLibros(
            @RequestParam String criterio,
            @RequestParam(defaultValue = "titulo") String tipoBusqueda) {
        return libroService.buscarLibros(criterio, tipoBusqueda);
    }

    @GetMapping("/todos")
    public Flux<Libro> listarTodos() {
        return libroService.listarTodos();
    }

    @PostMapping("/{id}/prestar")
    public Mono<String> prestarLibro(
            @PathVariable Long id,
            @RequestParam String prestatario) {
        return libroService.prestarLibro(id, prestatario);
    }

    @PostMapping("/{id}/devolver")
    public Mono<String> devolverLibro(@PathVariable Long id) {
        return libroService.devolverLibro(id);
    }
}
