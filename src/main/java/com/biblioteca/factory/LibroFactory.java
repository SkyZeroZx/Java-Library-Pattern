package com.biblioteca.factory;

import com.biblioteca.model.Libro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.model.EstadoLibro;

// Patron Factory Method - Clase base abstracta
public abstract class LibroFactory {
    public abstract Libro crearLibro(String titulo, String autor, FormatoLibro formato);

    public static LibroFactory getFactory(TipoLibro tipo) {
        return switch (tipo) {
            case FICCION -> new FiccionFactory();
            case NO_FICCION -> new NoFiccionFactory();
        };
    }
}

// Factory concreta para libros de ficcion
class FiccionFactory extends LibroFactory {
    @Override
    public Libro crearLibro(String titulo, String autor, FormatoLibro formato) {
        return Libro.builder()
            .titulo(titulo)
            .autor(autor)
            .tipo(TipoLibro.FICCION)
            .formato(formato)
            .estado(EstadoLibro.DISPONIBLE)
            .descripcion("Libro de ficcion: " + titulo)
            .build();
    }
}

// Factory concreta para libros de no ficcion
class NoFiccionFactory extends LibroFactory {
    @Override
    public Libro crearLibro(String titulo, String autor, FormatoLibro formato) {
        return Libro.builder()
            .titulo(titulo)
            .autor(autor)
            .tipo(TipoLibro.NO_FICCION)
            .formato(formato)
            .estado(EstadoLibro.DISPONIBLE)
            .descripcion("Libro educativo: " + titulo)
            .build();
    }
}
