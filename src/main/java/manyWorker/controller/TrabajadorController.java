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
import manyWorker.security.JWTUtils;
import manyWorker.service.TrabajadorService;

@RestController
@RequestMapping("/trabajador")
@Tag(name = "Trabajadores", description = "Controlador para la gestión de trabajadores")
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;
    
    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping
    @Operation(summary = "Obtener todos los trabajadores", description = "Devuelve una lista completa de todos los trabajadores registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de trabajadores obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay trabajadores registrados"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    
    // Devuelve un response entity de cualquier tipo
    public ResponseEntity<?> findAll() {
        List<Trabajador> trabajadores = trabajadorService.findAll();
        if (trabajadores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay trabajadores registrados en el sistema");
        }
        return ResponseEntity.ok(trabajadores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar trabajador por ID", description = "Busca un trabajador específico utilizando su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Trabajador encontrado"),
        @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
    	Trabajador trabajadorLogueado = jwtUtils.userLogin();
        
        if (trabajadorLogueado == null || (trabajadorLogueado.getId() != id && !"ADMINISTRADOR".equals(trabajadorLogueado.getRol().name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para ver este perfil");
        }
    	
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de trabajador inválido");
        }
        
        Optional<Trabajador> oTrabajador = trabajadorService.findById(id);
        
        if (oTrabajador.isPresent()) {
            return ResponseEntity.ok(oTrabajador.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador con ID " + id + " no encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo trabajador", description = "Registra un nuevo trabajador en la base de datos.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Trabajador creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del trabajador inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> save(@RequestBody Trabajador trabajador) {
        try {
            if (trabajador.getNombre() == null || trabajador.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del trabajador es obligatorio");
            }
            if (trabajador.getApellido() == null || trabajador.getApellido().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El apellido del trabajador es obligatorio");
            }
            
            Trabajador savedTrabajador = trabajadorService.save(trabajador);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Trabajador creado correctamente con ID: " + savedTrabajador.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el trabajador: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un trabajador", description = "Actualiza la información de un trabajador existente según su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Trabajador actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Trabajador trabajador) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de trabajador inválido");
            }
            
            Trabajador updatedTrabajador = trabajadorService.update(id, trabajador);
            if (updatedTrabajador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador con ID " + id + " no encontrado");
            }
            
            return ResponseEntity.ok("Trabajador actualizado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el trabajador: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un trabajador", description = "Elimina un trabajador existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Trabajador eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Trabajador no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de trabajador inválido");
            }
            
            if (!trabajadorService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador con ID " + id + " no encontrado");
            }
            
            trabajadorService.delete(id);
            return ResponseEntity.ok("Trabajador eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el trabajador: " + e.getMessage());
        }
    }
}