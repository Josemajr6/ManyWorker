package manyWorker.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tutorial extends DomainEntity {

    @NotBlank
    private String titulo;
    
    @CreationTimestamp // La fecha de creación se guarda con este @
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp // La fecha de actualización se guarda con este @
    private LocalDateTime fechaActualizacion;
    
    @NotBlank
    private String resumen;
    
    @ElementCollection
    private List<String> imagenes;
    
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String texto;

    @ManyToOne
    @NotNull
    private Trabajador autor;

    public Tutorial() {
        super();
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Trabajador getAutor() {
        return autor;
    }

    public void setAutor(Trabajador autor) {
        this.autor = autor;
    }
}