package com.biblioteca.builder;

import com.biblioteca.model.Libro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.model.EstadoLibro;

// Patrón Builder - Permite construir objetos Libro de manera flexible
public class LibroBuilder {
    private Long id;
    private String titulo;
    private String autor;
    private TipoLibro tipo;
    private FormatoLibro formato;
    private EstadoLibro estado;
    private String descripcion;

    public LibroBuilder() {
        this.estado = EstadoLibro.DISPONIBLE; // Estado por defecto
    }

    public LibroBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LibroBuilder withTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public LibroBuilder withAutor(String autor) {
        this.autor = autor;
        return this;
    }

    public LibroBuilder withTipo(TipoLibro tipo) {
        this.tipo = tipo;
        return this;
    }

    public LibroBuilder withFormato(FormatoLibro formato) {
        this.formato = formato;
        return this;
    }

    public LibroBuilder withEstado(EstadoLibro estado) {
        this.estado = estado;
        return this;
    }

    public LibroBuilder withDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public Libro build() {
        if (descripcion == null) {
            descripcion = String.format("%s - %s (%s, %s)", titulo, autor, tipo, formato);
        }

        return Libro.builder()
            .id(id)
            .titulo(titulo)
            .autor(autor)
            .tipo(tipo)
            .formato(formato)
            .estado(estado)
            .descripcion(descripcion)
            .build();
    }
}

