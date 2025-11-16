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
import manyWorker.entity.Mensaje;
import manyWorker.repository.MensajeRepository;
import manyWorker.service.MensajeService;

// DTO para enviar mensajes
class EnviarMensajeRequest {
    public int idRemitente;
    public int idDestinatario;
    public String asunto;
    public String cuerpo;
}

// DTO para broadcast
class BroadcastRequest {
    public int idRemitente;
    public String asunto;
    public String cuerpo;
}

@RestController
@RequestMapping("/mensajes")
@Tag(name = "Mensajes", description = "Controlador para la gestión de mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @Autowired
    private MensajeRepository mensajeRepository;

    @GetMapping
    @Operation(summary = "Obtener todos los mensajes", description = "Devuelve una lista de todos los mensajes del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay mensajes registrados")
    })
    public ResponseEntity<?> findAll() {
        List<Mensaje> mensajes = mensajeService.findAll();
        if (mensajes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay mensajes registrados en el sistema");
        }
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensaje por ID", description = "Busca un mensaje específico utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje encontrado"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de mensaje inválido");
        }
        
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        
        if (mensaje.isPresent()) {
            return ResponseEntity.ok(mensaje.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mensaje con ID " + id + " no encontrado");
        }
    }

    @PostMapping("/enviar")
    @Operation(summary = "Enviar un mensaje entre actores", description = "Envía un mensaje de un remitente a un destinatario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mensaje enviado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del mensaje inválidos"),
        @ApiResponse(responseCode = "404", description = "Remitente o destinatario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> enviarMensaje(@RequestBody EnviarMensajeRequest request) {
        try {
            if (request.idRemitente <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de remitente inválido");
            }
            if (request.idDestinatario <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de destinatario inválido");
            }
            if (request.asunto == null || request.asunto.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El asunto del mensaje es obligatorio");
            }
            if (request.cuerpo == null || request.cuerpo.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El cuerpo del mensaje es obligatorio");
            }
            
            Mensaje nuevo = mensajeService.enviarMensaje(
                    request.idRemitente, request.idDestinatario, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    @PostMapping("/broadcast")
    @Operation(summary = "Enviar mensaje broadcast", description = "Envía un mensaje a todos los usuarios del sistema (excepto al remitente)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mensajes broadcast enviados correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del mensaje inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para enviar broadcast"),
        @ApiResponse(responseCode = "404", description = "Remitente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> enviarBroadcast(@RequestBody BroadcastRequest request) {
        try {
            if (request.idRemitente <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de remitente inválido");
            }
            if (request.asunto == null || request.asunto.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El asunto del mensaje es obligatorio");
            }
            if (request.cuerpo == null || request.cuerpo.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El cuerpo del mensaje es obligatorio");
            }
            
            List<Mensaje> enviados = mensajeService.enviarBroadcast(
                    request.idRemitente, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(enviados);
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar broadcast: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mensaje", description = "Elimina un mensaje existente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de mensaje inválido");
            }
            
            if (!mensajeService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mensaje con ID " + id + " no encontrado");
            }
            
            mensajeService.delete(id);
            return ResponseEntity.ok("Mensaje eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el mensaje: " + e.getMessage());
        }
    }

    @GetMapping("/remitente/{remitenteId}")
    @Operation(summary = "Buscar mensajes por remitente", description = "Busca todos los mensajes enviados por un remitente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "El remitente no tiene mensajes enviados"),
        @ApiResponse(responseCode = "400", description = "ID de remitente inválido")
    })
    public ResponseEntity<?> findByRemitenteId(@PathVariable int remitenteId) {
        if (remitenteId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de remitente inválido");
        }
        
        List<Mensaje> mensajes = mensajeRepository.findByRemitenteId(remitenteId);
        if (mensajes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El remitente con ID " + remitenteId + " no tiene mensajes enviados");
        }
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/destinatario/{destinatarioId}")
    @Operation(summary = "Buscar mensajes por destinatario", description = "Busca todos los mensajes recibidos por un destinatario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mensajes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "El destinatario no tiene mensajes recibidos"),
        @ApiResponse(responseCode = "400", description = "ID de destinatario inválido")
    })
    public ResponseEntity<?> findByDestinatarioId(@PathVariable int destinatarioId) {
        if (destinatarioId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de destinatario inválido");
        }
        
        List<Mensaje> mensajes = mensajeRepository.findByDestinatarioId(destinatarioId);
        if (mensajes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El destinatario con ID " + destinatarioId + " no tiene mensajes recibidos");
        }
        return ResponseEntity.ok(mensajes);
    }
}