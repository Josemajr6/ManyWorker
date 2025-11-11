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
    @Operation(summary = "Obtener todas las solicitudes", description = "Devuelve la lista completa de solicitudes registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente")
    })
    public ResponseEntity<List<Solicitud>> listar() {
        return ResponseEntity.ok(solicitudService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar solicitud por ID", description = "Busca una solicitud específica utilizando su ID")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<Solicitud> findById(@PathVariable int id) {
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        return solicitud.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud", description = "Registra una nueva solicitud en el sistema")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<String> crear(@RequestBody Solicitud solicitud) {
        try {
            solicitudService.crear(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitud creada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/aceptar")
    @Operation(summary = "Aceptar una solicitud", description = "Cambia el estado de la solicitud a ACEPTADO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aceptada correctamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<String> aceptar(@PathVariable int id) {
        try {
            solicitudService.aceptar(id);
            return ResponseEntity.ok("Solicitud aceptada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar una solicitud", description = "Cambia el estado de la solicitud a RECHAZADO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada correctamente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<String> rechazar(@PathVariable int id) {
        try {
            solicitudService.rechazar(id);
            return ResponseEntity.ok("Solicitud rechazada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una solicitud", description = "Elimina una solicitud pendiente de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar solicitud no pendiente"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        Optional<Solicitud> solicitud = solicitudService.findById(id);
        if (solicitud.isPresent()) {
            try {
                solicitudService.eliminar(id);
                return ResponseEntity.ok("Solicitud eliminada correctamente");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }
    }
}