package com.biblioteca.validator;

import com.biblioteca.model.Libro;

public class ValidadorAutor extends Validador {
    @Override
    protected void ejecutarValidacion(Libro libro) throws ValidationException {
        if (libro.getAutor() == null || libro.getAutor().trim().isEmpty()) {
            throw new ValidationException("El autor no puede estar vacío");
        }
        if (libro.getAutor().length() < 2) {
            throw new ValidationException("El nombre del autor debe tener al menos 2 caracteres");
        }
        if (!libro.getAutor().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new ValidationException("El nombre del autor solo debe contener letras y espacios");
        }
    }
}