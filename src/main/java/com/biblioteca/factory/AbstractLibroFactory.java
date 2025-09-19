package com.biblioteca.factory;

import com.biblioteca.model.Libro;

// Patrón Abstract Factory - Interfaz abstracta para familias de productos
public interface AbstractLibroFactory {
    Libro crearLibroFiccion(String titulo, String autor);
    Libro crearLibroNoFiccion(String titulo, String autor);
}

