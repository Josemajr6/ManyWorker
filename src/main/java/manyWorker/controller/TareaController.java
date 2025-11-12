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
import manyWorker.entity.Tarea;
import manyWorker.service.TareaService;

@RestController
@RequestMapping("/tareas")
@Tag(name = "Tareas", description = "Controlador para la gestión de tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping
    @Operation(summary = "Obtener todas las tareas", description = "Devuelve una lista completa de todas las tareas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tareas obtenida correctamente")
    })
    public ResponseEntity<List<Tarea>> findAll() {
        return ResponseEntity.ok(tareaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarea por ID", description = "Busca una tarea específica utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "400", description = "Tarea no encontrada")
    })
    public ResponseEntity<Tarea> findById(@PathVariable String id) {
        Optional<Tarea> oTarea = tareaService.findById(id);
        return oTarea.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva tarea", description = "Registra una nueva tarea en la base de datos.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Tarea creada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear la tarea") 
    })
    public ResponseEntity<String> save(@RequestBody Tarea tarea) {
        tareaService.save(tarea);
        return ResponseEntity.status(HttpStatus.OK).body("Tarea creada correctamente");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea", description = "Actualiza la información de una tarea existente según su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Tarea no encontrada o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar la tarea") 
    })
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody Tarea tarea) {
        if (tareaService.update(id, tarea) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarea no encontrada");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Tarea actualizada correctamente");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea", description = "Elimina una tarea existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Tarea eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Tarea no encontrada") 
    })
    public ResponseEntity<String> delete(@PathVariable String id) {
        Optional<Tarea> oTarea = tareaService.findById(id);
        if (oTarea.isPresent()) {
            tareaService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Tarea eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarea no encontrada");
        }
    }
}
