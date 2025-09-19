package com.biblioteca.factory;

import com.biblioteca.model.Libro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.model.EstadoLibro;

// Factory concreta para libros digitales
public class LibroDigitalFactory implements AbstractLibroFactory {
    @Override
    public Libro crearLibroFiccion(String titulo, String autor) {
        return Libro.builder()
            .titulo(titulo)
            .autor(autor)
            .tipo(TipoLibro.FICCION)
            .formato(FormatoLibro.DIGITAL)
            .estado(EstadoLibro.DISPONIBLE)
            .descripcion("Libro digital de ficción: " + titulo)
            .build();
    }

    @Override
    public Libro crearLibroNoFiccion(String titulo, String autor) {
        return Libro.builder()
            .titulo(titulo)
            .autor(autor)
            .tipo(TipoLibro.NO_FICCION)
            .formato(FormatoLibro.DIGITAL)
            .estado(EstadoLibro.DISPONIBLE)
            .descripcion("Libro digital educativo: " + titulo)
            .build();
    }
}