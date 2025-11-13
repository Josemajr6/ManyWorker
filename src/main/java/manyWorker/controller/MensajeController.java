package manyWorker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import manyWorker.entity.Mensaje;
import manyWorker.service.MensajeService;

// DTO simple para enviar mensajes
// Este DTO es necesario para que la funcion de enviar mensaje funcione correctamente
class EnviarMensajeRequest {
    public int idRemitente;
    public int idDestinatario;
    public String asunto;
    public String cuerpo;
}

// DTO para la solicitud de broadcast
class BroadcastRequest {
	public int idRemitente;
	public String asunto;
	public String cuerpo;
}

@RestController
@RequestMapping("/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    @GetMapping
    @Operation(summary = "Obtener todos los mensajes")
    public ResponseEntity<List<Mensaje>> findAll() {
        return ResponseEntity.ok(mensajeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener mensaje por ID")
    public ResponseEntity<Mensaje> findById(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        if (mensaje.isPresent()) {
            return ResponseEntity.ok(mensaje.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/enviar")
    @Operation(summary = "Enviar un mensaje entre actores")
    public ResponseEntity<?> enviarMensaje(@RequestBody EnviarMensajeRequest request) {
        try {
            Mensaje nuevo = mensajeService.enviarMensaje(
                    request.idRemitente, request.idDestinatario, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un mensaje por ID")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Mensaje> mensaje = mensajeService.findById(id);
        if (mensaje.isPresent()) {
            mensajeService.delete(id);
            return ResponseEntity.ok("Mensaje eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mensaje no encontrado");
        }
    }
    
    // Enviar mensaje broadcast (solo admin)
    @PostMapping("/broadcast")
    public ResponseEntity<?> enviarBroadcast(@RequestBody BroadcastRequest request) {
        try {
            List<Mensaje> enviados = mensajeService.enviarBroadcast(
                    request.idRemitente, request.asunto, request.cuerpo);
            return ResponseEntity.status(HttpStatus.CREATED).body(enviados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}