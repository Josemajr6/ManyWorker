package manyWorker.controller;

import java.util.List;

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
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista completa de todas las categorías registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente")
    })
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva categoría en la base de datos.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Categoría creada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear la categoría") 
    })
    public ResponseEntity<String> crear(@RequestBody Categoria categoria) {
        categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.OK).body("Categoría creada correctamente");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría", description = "Actualiza los datos de una categoría existente mediante su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Categoría no encontrada o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar la categoría") 
    })
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Categoria datos) {
        try {
            categoriaService.actualizar(id, datos);
            return ResponseEntity.status(HttpStatus.OK).body("Categoría actualizada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría existente si no tiene tareas asociadas.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Categoría no encontrada o asociada a tareas") 
    })
    public ResponseEntity<String> eliminar(@PathVariable String id) {
        try {
            categoriaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.OK).body("Categoría eliminada correctamente");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la categoría");
        }
    }
}
