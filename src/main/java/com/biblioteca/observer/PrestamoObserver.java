package com.biblioteca.observer;

import com.biblioteca.model.ILibro;

public class PrestamoObserver implements Observer {
    private String nombre;

    public PrestamoObserver(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void update(ILibro libro, String evento) {
        System.out.printf("[%s] Notificacion: %s - %s%n",
            nombre, evento, libro.getDescripcion());
    }
}