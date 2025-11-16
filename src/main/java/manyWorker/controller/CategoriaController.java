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
import manyWorker.entity.Categoria;
import manyWorker.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "Controlador para la gestión de categorías")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay categorías registradas")
    })
    public ResponseEntity<?> findAll() {
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay categorías registradas en el sistema");
        }
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoría por ID", description = "Busca una categoría específica utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ResponseEntity<?> findById(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de categoría no puede estar vacío");
        }
        
        Optional<Categoria> oCategoria = categoriaService.findById(id);
        
        if (oCategoria.isPresent()) {
            return ResponseEntity.ok(oCategoria.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría con ID '" + id + "' no encontrada");
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva categoría en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la categoría inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> save(@RequestBody Categoria categoria) {
        try {
            if (categoria.getTitulo() == null || categoria.getTitulo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El título de la categoría es obligatorio");
            }
            
            Categoria savedCategoria = categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Categoría creada correctamente con ID: " + savedCategoria.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la categoría: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza la información de una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Categoria datos) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de categoría no puede estar vacío");
            }
            
            if (datos.getTitulo() == null || datos.getTitulo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El título de la categoría es obligatorio");
            }
            
            categoriaService.update(id, datos);
            return ResponseEntity.ok("Categoría actualizada correctamente");
            
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría existente si no tiene tareas asociadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar (tiene tareas asociadas)"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de categoría no puede estar vacío");
            }
            
            if (!categoriaService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría con ID '" + id + "' no encontrada");
            }
            
            categoriaService.delete(id);
            return ResponseEntity.ok("Categoría eliminada correctamente");
            
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la categoría: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al eliminar la categoría: " + e.getMessage());
        }
    }
}