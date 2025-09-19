package com.biblioteca.validator;

public class ValidationException extends Exception {
    public ValidationException(String mensaje) {
        super(mensaje);
    }
}