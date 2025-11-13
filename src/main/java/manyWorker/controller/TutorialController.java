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
    @Operation(summary = "Obtener todos los tutoriales")
    public ResponseEntity<List<Tutorial>> findAll() {
        return ResponseEntity.ok(tutorialService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un tutorial por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial encontrado"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado")
    })
    public ResponseEntity<Tutorial> findById(@PathVariable int id) {
        Optional<Tutorial> oTutorial = tutorialService.findById(id);
        return oTutorial.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo tutorial")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tutorial creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<String> save(@RequestBody Tutorial tutorial) {
        Tutorial savedTutorial = tutorialService.save(tutorial);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tutorial creado correctamente con ID: " + savedTutorial.getId());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tutorial por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado")
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Tutorial tutorial) {
        Tutorial updatedTutorial = tutorialService.update(id, tutorial);
        if (updatedTutorial == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Tutorial actualizado correctamente");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tutorial por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tutorial eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tutorial no encontrado")
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        if (!tutorialService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tutorialService.delete(id);
        return ResponseEntity.ok("Tutorial eliminado correctamente");
    }

    @GetMapping("/autor/{autorId}")
    @Operation(summary = "Buscar tutoriales por ID de autor")
    public ResponseEntity<List<Tutorial>> findByAutorId(@PathVariable int autorId) {
        List<Tutorial> tutoriales = tutorialService.findByAutorId(autorId);
        return ResponseEntity.ok(tutoriales);
    }
}