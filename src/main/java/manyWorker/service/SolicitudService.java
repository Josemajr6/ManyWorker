package manyWorker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manyWorker.entity.Solicitud;
import manyWorker.entity.Tarea;
import manyWorker.repository.SolicitudRepository;
import manyWorker.repository.TareaRepository;
import manyWorker.repository.TrabajadorRepository;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private TareaRepository tareaRepository;
    @Autowired
    private TrabajadorRepository trabajadorRepository;
    
    public List<Solicitud> findAll() {
        return solicitudRepository.findAll();
    }

    public Optional<Solicitud> findById(int id) {
        return solicitudRepository.findById(id);
    }

    public boolean existsById(int id) {
        return solicitudRepository.existsById(id);
    }

    public Solicitud save(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    // Crear nueva solicitud
    public Solicitud crear(Solicitud solicitud) {
    	solicitud.setTrabajador(trabajadorRepository.findById(solicitud.getTrabajador().getId()).orElse(null));
        solicitud.setTarea(tareaRepository.findById(solicitud.getTarea().getId()).orElse(null));
    	
    	if (solicitud.getTrabajador() == null) {
            throw new IllegalArgumentException("La solicitud debe tener un trabajador asignado");
        }

        if (solicitud.getTarea() != null && esTareaDeReparacion(solicitud.getTarea())) {
            validarSolicitudReparacion(solicitud);
        }

        if (solicitud.getPrecioOfrecido() == null || solicitud.getPrecioOfrecido() <= 0) {
            throw new IllegalArgumentException("El precio ofrecido debe ser mayor que 0");
        }
        if (solicitud.getComentario() == null || solicitud.getComentario().isBlank()) {
            throw new IllegalArgumentException("El comentario es obligatorio");
        }

        solicitud.setFechaRegistro(LocalDateTime.now());
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);
        
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        
        // Notificar al trabajador que tiene nueva solicitud
        notificarCambioEstado(savedSolicitud, "SOLICITUD_CREADA");
        
        return savedSolicitud;
    }

    // Aceptar solicitud
    public Solicitud aceptar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.ACEPTADO);
        
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        
        // Notificar a ambos del cambio de estado
        notificarCambioEstado(savedSolicitud, "SOLICITUD_ACEPTADA");
        
        return savedSolicitud;
    }

    // Rechazar solicitud
    public Solicitud rechazar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.setEstado(Solicitud.EstadoSolicitud.RECHAZADO);
        
        Solicitud savedSolicitud = solicitudRepository.save(solicitud);
        
        // Notificar a ambos del cambio de estado
        notificarCambioEstado(savedSolicitud, "SOLICITUD_RECHAZADA");
        
        return savedSolicitud;
    }

    // método para notificar cambios de estado
    private void notificarCambioEstado(Solicitud solicitud, String tipoCambio) {
        try {
            Tarea tarea = solicitud.getTarea();
            if (tarea == null || tarea.getCliente() == null || solicitud.getTrabajador() == null) {
                return;
            }

            int clienteId = tarea.getCliente().getId();
            int trabajadorId = solicitud.getTrabajador().getId();
            String nombreCliente = tarea.getCliente().getNombre() + " " + tarea.getCliente().getApellido();
            String nombreTrabajador = solicitud.getTrabajador().getNombre() + " " + solicitud.getTrabajador().getApellido();
            String descripcionTarea = tarea.getDescripcion();
            double precio = solicitud.getPrecioOfrecido();

            // Mensaje para el cliente
            String asuntoCliente = obtenerAsuntoCliente(tipoCambio);
            String cuerpoCliente = obtenerCuerpoCliente(tipoCambio, nombreTrabajador, descripcionTarea, precio);
            
            // Mensaje para el trabajador  
            String asuntoTrabajador = obtenerAsuntoTrabajador(tipoCambio);
            String cuerpoTrabajador = obtenerCuerpoTrabajador(tipoCambio, nombreCliente, descripcionTarea, precio, solicitud.getComentario());

            // Enviar mensaje al cliente (remitente: trabajador)
            mensajeService.enviarMensaje(trabajadorId, clienteId, asuntoCliente, cuerpoCliente);
            
            // Enviar mensaje al trabajador (remitente: cliente)
            mensajeService.enviarMensaje(clienteId, trabajadorId, asuntoTrabajador, cuerpoTrabajador);

        } catch (Exception e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

    // Métodos auxiliares simples para construir mensajes
    private String obtenerAsuntoCliente(String tipoCambio) {
        switch (tipoCambio) {
            case "SOLICITUD_ACEPTADA": return "Tu solicitud ha sido ACEPTADA";
            case "SOLICITUD_RECHAZADA": return "Tu solicitud ha sido RECHAZADA";
            default: return "Actualización de solicitud";
        }
    }

    private String obtenerCuerpoCliente(String tipoCambio, String nombreTrabajador, String descripcionTarea, double precio) {
        switch (tipoCambio) {
            case "SOLICITUD_ACEPTADA": 
                return "El trabajador " + nombreTrabajador + " ha aceptado tu solicitud para: '" + 
                       descripcionTarea + "'. Precio: " + precio + "€. Contacta al trabajador para coordinar.";
            case "SOLICITUD_RECHAZADA":
                return "El trabajador " + nombreTrabajador + " ha rechazado tu solicitud para: '" + 
                       descripcionTarea + "'. Puedes buscar otros trabajadores.";
            default: 
                return "Hay una actualización en tu solicitud para: " + descripcionTarea;
        }
    }

    private String obtenerAsuntoTrabajador(String tipoCambio) {
        switch (tipoCambio) {
            case "SOLICITUD_CREADA": return "Nueva solicitud recibida";
            case "SOLICITUD_ACEPTADA": return "Has aceptado una solicitud";
            case "SOLICITUD_RECHAZADA": return "Has rechazado una solicitud";
            default: return "Actualización de solicitud";
        }
    }

    private String obtenerCuerpoTrabajador(String tipoCambio, String nombreCliente, String descripcionTarea, double precio, String comentario) {
        switch (tipoCambio) {
            case "SOLICITUD_CREADA":
                return "Tienes una nueva solicitud para: '" + descripcionTarea + "'. Cliente: " + 
                       nombreCliente + ". Precio ofrecido: " + precio + "€. Comentario: " + comentario;
            case "SOLICITUD_ACEPTADA":
                return "Has aceptado la solicitud para: '" + descripcionTarea + "'. Cliente: " + 
                       nombreCliente + ". Precio: " + precio + "€. Contacta al cliente.";
            case "SOLICITUD_RECHAZADA":
                return "Has rechazado la solicitud para: '" + descripcionTarea + "'. Cliente: " + nombreCliente;
            default:
                return "Hay una actualización en la solicitud para: " + descripcionTarea;
        }
    }

    // Métodos de validación existentes
    private boolean esTareaDeReparacion(Tarea tarea) {
        if (tarea.getCategoria() == null) {
            return false;
        }
        return tarea.getCategoria().isEsReparacion();
    }

    private void validarSolicitudReparacion(Solicitud solicitud) {
        if (solicitud.getPrecioOfrecido() == null) {
            throw new IllegalArgumentException("Las solicitudes para tareas de reparación deben incluir un precio ofrecido");
        }
        
        if (solicitud.getPrecioOfrecido() <= 0) {
            throw new IllegalArgumentException("El precio ofrecido debe ser mayor a 0");
        }
        
        if (solicitud.getComentario() == null || solicitud.getComentario().trim().isEmpty()) {
            throw new IllegalArgumentException("Las solicitudes para tareas de reparación deben incluir un comentario");
        }
        
        if (solicitud.getComentario().trim().length() < 10) {
            throw new IllegalArgumentException("El comentario debe tener al menos 10 caracteres");
        }
    }

    // Eliminar solicitud
    public void eliminar(int id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != Solicitud.EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("No se puede eliminar una solicitud que no esté pendiente");
        }

        solicitudRepository.delete(solicitud);
    }
}