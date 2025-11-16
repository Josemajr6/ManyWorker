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
import manyWorker.entity.Tutorial;
import manyWorker.service.TutorialService;

@RestController
@RequestMapping("/tutoriales")
@Tag(name = "Tutoriales", description = "Controlador para la gestión de tutoriales")
public class TutorialController {
    
    @Autowired
    private TutorialService tutorialService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los tutoriales", description = "Devuelve una lista de todos los tutoriales del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tutoriales obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay tutoriales registrados")
    })
    public ResponseEntity<?> findAll() {
        List<Tutorial> tutoriales = tutorialService.findAll();
        if (tutoriales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay tutoriales registrados en el sistema");
        }
        return ResponseEntity.ok(tutoriales);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un tutorial por ID", description = "Busca un tutorial específico utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial encontrado"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tutorial inválido");
        }
        
        Optional<Tutorial> oTutorial = tutorialService.findById(id);

        if (oTutorial.isPresent()) {
            return ResponseEntity.ok(oTutorial.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial con ID " + id + " no encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tutorial", description = "Registra un nuevo tutorial en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tutorial creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del tutorial inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> save(@RequestBody Tutorial tutorial) {
        try {
            if (tutorial.getTitulo() == null || tutorial.getTitulo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El título del tutorial es obligatorio");
            }
            if (tutorial.getAutor() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El autor del tutorial es obligatorio");
            }
            if (tutorial.getTexto() == null || tutorial.getTexto().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El contenido del tutorial es obligatorio");
            }
            
            Tutorial savedTutorial = tutorialService.save(tutorial);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Tutorial creado correctamente con ID: " + savedTutorial.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el tutorial: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tutorial por ID", description = "Actualiza la información de un tutorial existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Tutorial tutorial) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tutorial inválido");
            }
            
            Tutorial updatedTutorial = tutorialService.update(id, tutorial);
            if (updatedTutorial == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial con ID " + id + " no encontrado");
            }
            
            return ResponseEntity.ok("Tutorial actualizado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el tutorial: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tutorial por ID", description = "Elimina un tutorial existente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de tutorial inválido");
            }
            
            if (!tutorialService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tutorial con ID " + id + " no encontrado");
            }
            
            tutorialService.delete(id);
            return ResponseEntity.ok("Tutorial eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el tutorial: " + e.getMessage());
        }
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Buscar tutoriales por ID de autor", description = "Busca todos los tutoriales de un autor específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tutoriales obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "El autor no tiene tutoriales"),
        @ApiResponse(responseCode = "400", description = "ID de autor inválido")
    })
    public ResponseEntity<?> findByAutorId(@PathVariable int autorId) {
        if (autorId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de autor inválido");
        }
        
        List<Tutorial> tutoriales = tutorialService.findByAutorId(autorId);
        if (tutoriales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("El autor con ID " + autorId + " no tiene tutoriales");
        }
        return ResponseEntity.ok(tutoriales);
    }
}