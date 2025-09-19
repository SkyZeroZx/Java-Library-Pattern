package com.biblioteca.validator;

import com.biblioteca.model.Libro;

public class ValidadorCamposObligatorios extends Validador {
    @Override
    protected void ejecutarValidacion(Libro libro) throws ValidationException {
        if (libro.getTipo() == null) {
            throw new ValidationException("El tipo de libro es obligatorio");
        }
        if (libro.getFormato() == null) {
            throw new ValidationException("El formato del libro es obligatorio");
        }
    }
}