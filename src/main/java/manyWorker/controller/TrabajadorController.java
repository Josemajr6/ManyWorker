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
import manyWorker.entity.Trabajador;
import manyWorker.service.TrabajadorService;

@RestController
@RequestMapping("/trabajador")
@Tag(name = "Trabajadores", description = "Controlador para la gestión de trabajadores")
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping
    @Operation(summary = "Obtener todos los trabajadores", description = "Devuelve una lista completa de todos los trabajadores registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores obtenida correctamente")
    })
    public ResponseEntity<List<Trabajador>> findAll() {
        return ResponseEntity.ok(trabajadorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar trabajador por ID", description = "Busca un trabajador específico utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Trabajador encontrado"),
            @ApiResponse(responseCode = "400", description = "Trabajador no encontrado")
    })
    public ResponseEntity<Trabajador> findById(@PathVariable int id) {
        Optional<Trabajador> oTrabajador = trabajadorService.findById(id);
        return oTrabajador.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo trabajador", description = "Registra un nuevo trabajador en la base de datos.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Trabajador creado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el trabajador") 
    })
    public ResponseEntity<String> save(@RequestBody Trabajador trabajador) {
    	trabajadorService.save(trabajador);
        return ResponseEntity.status(HttpStatus.OK).body("Trabajador creado correctamente");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un trabajador", description = "Actualiza la información de un trabajador existente según su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Trabajador no encontrado o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el trabajador") 
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Trabajador trabajador) {
        if (trabajadorService.update(id, trabajador) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trabajador no encontrado");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Trabajador actualizado correctamente");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un trabajador", description = "Elimina un trabajador existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Trabajador eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Trabajador no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<Trabajador> oTrabajador = trabajadorService.findById(id);
        if (oTrabajador.isPresent()) {
        	trabajadorService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Trabajador eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trabajador no encontrado");
        }
    }
}

