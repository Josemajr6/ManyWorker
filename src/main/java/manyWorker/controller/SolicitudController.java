package manyWorker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.Solicitud;
import manyWorker.service.SolicitudService;

@RestController
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes", description = "Controlador para la gestión de solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping
    @Operation(summary = "Obtener todas las solicitudes", description = "Devuelve una lista de todas las solicitudes del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay solicitudes registradas")
    })
    public ResponseEntity<?> findAll() {
        List<Solicitud> solicitudes = solicitudService.findAll();
        if (solicitudes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay solicitudes registradas en el sistema");
        }
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitud por ID", description = "Busca una solicitud específica utilizando su ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de solicitud inválido");
        }
        
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        
        if (solicitud.isPresent()) {
            return ResponseEntity.ok(solicitud.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud con ID " + id + " no encontrada");
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud", description = "Registra una nueva solicitud en el sistema con validaciones automáticas")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la solicitud inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> crear(@RequestBody Solicitud solicitud) {
        try {
            if (solicitud.getTrabajador() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El trabajador de la solicitud es obligatorio");
            }
            if (solicitud.getTarea() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tarea de la solicitud es obligatoria");
            }
            if (solicitud.getPrecioOfrecido() == null || solicitud.getPrecioOfrecido() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El precio ofrecido debe ser mayor a 0");
            }
            if (solicitud.getComentario() == null || solicitud.getComentario().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El comentario de la solicitud es obligatorio");
            }
            
            Solicitud savedSolicitud = solicitudService.crear(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Solicitud creada correctamente con ID: " + savedSolicitud.getId());
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la solicitud: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/aceptar")
    @Operation(summary = "Aceptar una solicitud", description = "Cambia el estado de la solicitud a ACEPTADO y envía notificaciones automáticas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud aceptada correctamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> aceptar(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de solicitud inválido");
            }
            
            solicitudService.aceptar(id);
            return ResponseEntity.ok("Solicitud aceptada correctamente. Se han enviado notificaciones al cliente y trabajador.");
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al aceptar la solicitud: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar una solicitud", description = "Cambia el estado de la solicitud a RECHAZADO y envía notificaciones automáticas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud rechazada correctamente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> rechazar(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de solicitud inválido");
            }
            
            solicitudService.rechazar(id);
            return ResponseEntity.ok("Solicitud rechazada correctamente. Se han enviado notificaciones al cliente y trabajador.");
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al rechazar la solicitud: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una solicitud", description = "Elimina una solicitud pendiente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud eliminada correctamente"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar solicitud no pendiente"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de solicitud inválido");
            }
            
            if (!solicitudService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud con ID " + id + " no encontrada");
            }
            
            solicitudService.eliminar(id);
            return ResponseEntity.ok("Solicitud eliminada correctamente");
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la solicitud: " + e.getMessage());
        }
    }
}