package com.biblioteca.adapter;

import com.biblioteca.model.ILibro;
import com.biblioteca.model.TipoLibro;
import com.biblioteca.model.FormatoLibro;
import com.biblioteca.model.EstadoLibro;

// Patrón Adapter - Adapta la clase LegacyLibro para implementar ILibro
public class LegacyLibroAdapter implements ILibro {
    
    // Clase legacy con métodos incompatibles (clase interna estática)
    public static class LegacyLibro {
        private String bookTitle;
        private String bookAuthor;
        private String bookType;
        private boolean isAvailable;

        public LegacyLibro(String bookTitle, String bookAuthor, String bookType, boolean isAvailable) {
            this.bookTitle = bookTitle;
            this.bookAuthor = bookAuthor;
            this.bookType = bookType;
            this.isAvailable = isAvailable;
        }

        public static LegacyLibro createBook(String title, String author, String type, boolean available) {
            return new LegacyLibro(title, author, type, available);
        }

        public String getBookTitle() { return bookTitle; }
        public String getBookAuthor() { return bookAuthor; }
        public String getBookType() { return bookType; }
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { 
            this.isAvailable = available; 
        }
    }
    private final LegacyLibro legacyLibro;
    private Long id;

    public LegacyLibroAdapter(LegacyLibro legacyLibro) {
        this.legacyLibro = legacyLibro;
    }

    public LegacyLibroAdapter(LegacyLibro legacyLibro, Long id) {
        this.legacyLibro = legacyLibro;
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitulo() {
        return legacyLibro.getBookTitle();
    }

    @Override
    public String getAutor() {
        return legacyLibro.getBookAuthor();
    }

    @Override
    public TipoLibro getTipo() {
        return legacyLibro.getBookType().equalsIgnoreCase("fiction")
            ? TipoLibro.FICCION : TipoLibro.NO_FICCION;
    }

    @Override
    public FormatoLibro getFormato() {
        // Asumimos que los libros legacy son físicos por defecto
        return FormatoLibro.FISICO;
    }

    @Override
    public EstadoLibro getEstado() {
        return legacyLibro.isAvailable() ? EstadoLibro.DISPONIBLE : EstadoLibro.PRESTADO;
    }

    @Override
    public void setEstado(EstadoLibro estado) {
        legacyLibro.setAvailable(estado == EstadoLibro.DISPONIBLE);
    }

    @Override
    public String getDescripcion() {
        return String.format("[LEGACY] %s - %s (%s, %s) - Estado: %s",
            getTitulo(), getAutor(), getTipo(), getFormato(), getEstado());
    }
}
