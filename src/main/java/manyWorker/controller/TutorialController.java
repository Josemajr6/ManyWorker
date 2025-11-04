package manyWorker.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.Tutorial;
import manyWorker.service.TutorialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/tutoriales")
@Tag(name = "Tutoriales", description = "Controlador para la gestión de tutoriales")
public class TutorialController {
    
    // Creamos una instancia de TutorialService para llamar a los métodos
    @Autowired
    private TutorialService tutorialService;
    

    // Método para obtener todos los tutoriales
    @GetMapping
    @Operation(summary = "Obtener todos los tutoriales")
    public ResponseEntity<List<Tutorial>> findAll() {
        List<Tutorial> tutoriales = tutorialService.findAll();
        return ResponseEntity.ok(tutoriales);
    }

    // Método para buscar un tutorial por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar un tutorial por ID")
    public ResponseEntity<Tutorial> findById(@PathVariable int id) {
        Optional<Tutorial> oTutorial = tutorialService.findById(id);

        if (oTutorial.isPresent()) {
            return ResponseEntity.ok(oTutorial.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Método para guardar nuevos tutoriales
    @PostMapping
    @Operation(summary = "Crear un nuevo tutorial")
    public void save (@RequestBody Tutorial t, HttpServletResponse response) throws IOException  {
        tutorialService.save(t);
        response.setStatus(200);
        response.getWriter().println("Tutorial creado.");
    }

    // Método para actualizar un tutorial mediante ID
    @Operation(summary = "Actualizar tutorial por ID")
    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody Tutorial t, HttpServletResponse response) throws IOException {
        if (tutorialService.update(id, t) == null) {
            response.setStatus(400);
            response.getWriter().println("Tutorial no encontrado.");
        }
        else {
            response.setStatus(200);
            response.getWriter().println("Tutorial actualizado correctamente.");
        }
    }

    // Método para borrar tutoriales mediante ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar tutorial por ID")
    public void delete(@PathVariable int id, HttpServletResponse response) throws IOException {
        Optional<Tutorial> oTutorial = tutorialService.findById(id);
        if (oTutorial.isPresent()) {
            response.setStatus(200);
            response.getWriter().println("Tutorial eliminado correctamente.");
            tutorialService.delete(id);
        }
        else {
            response.setStatus(400);
            response.getWriter().println("Tutorial no encontrado.");
        }
    }


}
