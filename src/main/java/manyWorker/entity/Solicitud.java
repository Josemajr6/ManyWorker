package manyWorker.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Solicitud extends DomainEntity{

    private LocalDateTime fechaRegistro;
    
    public enum EstadoSolicitud { //Solo hay tres posibles estados
        PENDIENTE,
        ACEPTADO,
        RECHAZADO
    }

    @Enumerated(EnumType.STRING) //Enumerada
    private EstadoSolicitud estado;

    private Double precioOfrecido;

    @Column(length = 1000) //Ejemplo de longitud del comentario
    private String comentario;

    @ManyToOne
    private Trabajador trabajador;

    public Solicitud() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public Double getPrecioOfrecido() {
        return precioOfrecido;
    }

    public void setPrecioOfrecido(Double precioOfrecido) {
        this.precioOfrecido = precioOfrecido;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }
}