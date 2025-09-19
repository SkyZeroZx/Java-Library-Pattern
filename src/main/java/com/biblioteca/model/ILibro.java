package com.biblioteca.model;

public interface ILibro {
    Long getId();
    String getTitulo();
    String getAutor();
    TipoLibro getTipo();
    FormatoLibro getFormato();
    EstadoLibro getEstado();
    void setEstado(EstadoLibro estado);
    String getDescripcion();
}
