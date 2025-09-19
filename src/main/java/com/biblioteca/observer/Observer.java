package com.biblioteca.observer;

import com.biblioteca.model.ILibro;

// Patron Observer - Interfaz del observador
public interface Observer {
    void update(ILibro libro, String evento);
}


