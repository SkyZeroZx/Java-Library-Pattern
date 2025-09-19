package com.biblioteca.validator;

import com.biblioteca.model.Libro;

public class ValidadorTitulo extends Validador {
    @Override
    protected void ejecutarValidacion(Libro libro) throws ValidationException {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new ValidationException("El título no puede estar vacío");
        }
        if (libro.getTitulo().length() < 2) {
            throw new ValidationException("El título debe tener al menos 2 caracteres");
        }
    }
}