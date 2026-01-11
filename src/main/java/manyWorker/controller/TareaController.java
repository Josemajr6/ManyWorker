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
import manyWorker.entity.Actor;
import manyWorker.entity.Tarea;
import manyWorker.service.TareaService;

@RestController
@RequestMapping("/tareas")
@Tag(name = "Tareas", description = "Controlador para la gestión de tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private manyWorker.security.JWTUtils jwtUtils;

    @GetMapping
    @Operation(summary = "Obtener todas las tareas", description = "Devuelve una lista de todas las tareas del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay tareas registradas"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findAll() {
        List<Tarea> tareas = tareaService.findAll();
        if (tareas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay tareas registradas en el sistema");
        }
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarea por ID", description = "Busca una tarea específica utilizando su ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findById(@PathVariable String id) {
    	Actor usuarioLogueado = jwtUtils.userLogin();
        
        if (usuarioLogueado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        
        Optional<Tarea> oTarea = tareaService.findById(id);
        
        if (oTarea.isPresent()) {
            Tarea tarea = oTarea.get();
            
            boolean puedeVer = false;
            
            if ("ADMINISTRADOR".equals(usuarioLogueado.getRol().name())) {
                puedeVer = true;
            } else if ("CLIENTE".equals(usuarioLogueado.getRol().name())) {
                if (tarea.getCliente() != null && tarea.getCliente().getId() == usuarioLogueado.getId()) {
                    puedeVer = true;
                }
            } else if ("TRABAJADOR".equals(usuarioLogueado.getRol().name())) {
                puedeVer = true;
            }
            
            if (puedeVer) {
                return ResponseEntity.ok(tarea);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para ver esta tarea");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea con ID '" + id + "' no encontrada");
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva tarea", description = "Registra una nueva tarea en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Tarea creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la tarea inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> save(@RequestBody Tarea tarea) {
        try {
        	manyWorker.entity.Cliente clienteLogueado = jwtUtils.userLogin();
        	if (clienteLogueado == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Debes ser un cliente para crear tareas.");
            }
        	tarea.setCliente(clienteLogueado);
            if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La descripción de la tarea es obligatoria");
            }            
            if (tarea.getCategoria() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoría de la tarea es obligatoria");
            }
            if (tarea.getPrecioMax() == null || tarea.getPrecioMax() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El precio máximo debe ser mayor a 0");
            }
            
            Tarea savedTarea = tareaService.save(tarea);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Tarea creada correctamente con ID: " + savedTarea.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la tarea: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea", description = "Actualiza la información de una tarea existente")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Tarea tarea) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tarea no puede estar vacío");
            }
            
            Tarea updatedTarea = tareaService.update(id, tarea);
            if (updatedTarea == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea con ID '" + id + "' no encontrada");
            }
            
            return ResponseEntity.ok("Tarea actualizada correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la tarea: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea", description = "Elimina una tarea existente del sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Tarea eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Tarea no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tarea no puede estar vacío");
            }
            
            if (!tareaService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea con ID '" + id + "' no encontrada");
            }
            
            tareaService.delete(id);
            return ResponseEntity.ok("Tarea eliminada correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la tarea: " + e.getMessage());
        }
    }
}