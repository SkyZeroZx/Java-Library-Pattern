package com.biblioteca.validator;

import com.biblioteca.model.Libro;

// Patrón Chain of Responsibility - Interfaz base para validadores
public abstract class Validador {
    protected Validador siguiente;

    public void setSiguiente(Validador validador) {
        this.siguiente = validador;
    }

    public void validar(Libro libro) throws ValidationException {
        ejecutarValidacion(libro);
        if (siguiente != null) {
            siguiente.validar(libro);
        }
    }

    protected abstract void ejecutarValidacion(Libro libro) throws ValidationException;
}


