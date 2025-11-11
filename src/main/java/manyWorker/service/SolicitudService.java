package manyWorker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Solicitud;
import manyWorker.repository.SolicitudRepository;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    // Listar todas las solicitudes
    public List<Solicitud> listar() {
        return solicitudRepository.findAll();
    }

    // Buscar solicitud por ID
    public Optional<Solicitud> findById(int id) {
        return solicitudRepository.findById(id);
    }

    // Crear nueva solicitud
    public Solicitud crear(Solicitud solicitud) {
        if (solicitud.getPrecioOfrecido() == null || solicitud.getPrecioOfrecido() <= 0) {
            throw new IllegalArgumentException("El precio ofrecido debe ser mayor que 0");
        }
        if (solicitud.getComentario() == null || solicitud.getComentario().isBlank()) {
            throw new IllegalArgumentException("El comentario es obligatorio");
        }
        if (solicitud.getTrabajador() == null) {
            throw new IllegalArgumentException("La solicitud debe tener un trabajador asignado");
        }

        solicitud.setFechaRegistro(LocalDateTime.now());
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        return solicitudRepository.save(solicitud);
    }

    // Aceptar solicitud
    public Solicitud aceptar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.ACEPTADO);
        return solicitudRepository.save(solicitud);
    }

    // Rechazar solicitud
    public Solicitud rechazar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.RECHAZADO);
        return solicitudRepository.save(solicitud);
    }

    // Eliminar solicitud (solo pendiente)
    public void eliminar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != Solicitud.EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("No se puede eliminar una solicitud que no esté pendiente");
        }

        solicitudRepository.delete(solicitud);
    }
}

