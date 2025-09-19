package com.biblioteca.decorator;

import com.biblioteca.model.ILibro;
import com.biblioteca.model.EstadoLibro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.observer.Observer;
import java.util.ArrayList;
import java.util.List;

// Patron Decorator - Anade funcionalidad de prestamo sin modificar la clase Libro
public class PrestamoDecorator implements ILibro {
    private final ILibro libro;
    private final List<Observer> observers = new ArrayList<>();
    private String fechaPrestamo;
    private String prestatario;

    public PrestamoDecorator(ILibro libro) {
        this.libro = libro;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void prestar(String prestatario) {
        if (libro.getEstado() == EstadoLibro.DISPONIBLE) {
            this.prestatario = prestatario;
            this.fechaPrestamo = java.time.LocalDateTime.now().toString();
            libro.setEstado(EstadoLibro.PRESTADO);
            notifyObservers("Libro prestado a: " + prestatario);
        } else {
            throw new IllegalStateException("El libro no esta disponible para prestamo");
        }
    }

    public void devolver() {
        if (libro.getEstado() == EstadoLibro.PRESTADO) {
            String antiguoPrestatario = this.prestatario;
            this.prestatario = null;
            this.fechaPrestamo = null;
            libro.setEstado(EstadoLibro.DISPONIBLE);
            notifyObservers("Libro devuelto por: " + antiguoPrestatario);
        } else {
            throw new IllegalStateException("El libro no esta prestado");
        }
    }

    private void notifyObservers(String evento) {
        observers.forEach(observer -> observer.update(libro, evento));
    }

    // Delegaci�n a la clase original
    @Override
    public Long getId() { return libro.getId(); }

    @Override
    public String getTitulo() { return libro.getTitulo(); }

    @Override
    public String getAutor() { return libro.getAutor(); }

    @Override
    public TipoLibro getTipo() { return libro.getTipo(); }

    @Override
    public FormatoLibro getFormato() { return libro.getFormato(); }

    @Override
    public EstadoLibro getEstado() { return libro.getEstado(); }

    @Override
    public void setEstado(EstadoLibro estado) { libro.setEstado(estado); }

    @Override
    public String getDescripcion() {
        String desc = libro.getDescripcion();
        if (prestatario != null) {
            desc += " [Prestado a: " + prestatario + " el " + fechaPrestamo + "]";
        }
        return desc;
    }

    public String getPrestatario() { return prestatario; }
    public String getFechaPrestamo() { return fechaPrestamo; }
}
