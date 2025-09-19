package com.biblioteca.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("libros")
public class Libro implements ILibro {
    @Id
    private Long id;
    private String titulo;
    private String autor;
    private TipoLibro tipo;
    private FormatoLibro formato;
    private EstadoLibro estado;
    private String descripcion;

    public String getDescripcion() {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            return String.format("%s - %s (%s, %s) - %s - Estado: %s",
                titulo, autor, tipo, formato, descripcion, estado);
        }
        return String.format("%s - %s (%s, %s) - Estado: %s",
            titulo, autor, tipo, formato, estado);
    }
}
