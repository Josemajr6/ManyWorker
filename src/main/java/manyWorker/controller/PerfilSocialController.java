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
import manyWorker.entity.PerfilSocial;
import manyWorker.service.PerfilSocialService;

@RestController
@RequestMapping("/perfilSocial")
@Tag(name = "PerfilSocial", description = "Controlador para la gestión de perfiles sociales")
public class PerfilSocialController {

    @Autowired
    private PerfilSocialService perfilSocialService;

    @GetMapping
    @Operation(summary = "Obtener todos los perfiles sociales", description = "Devuelve una lista completa de todos los perfiles sociales registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de perfiles sociales obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay perfiles sociales registrados")
    })
    public ResponseEntity<?> findAll() {
        List<PerfilSocial> perfiles = perfilSocialService.findAll();
        if (perfiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay perfiles sociales registrados en el sistema");
        }
        return ResponseEntity.ok(perfiles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil social por ID", description = "Busca un perfil social específico utilizando su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Perfil social encontrado"),
        @ApiResponse(responseCode = "404", description = "Perfil social no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de perfil social inválido");
        }
        
        Optional<PerfilSocial> oPerfilSocial = perfilSocialService.findById(id);
        
        if (oPerfilSocial.isPresent()) {
            return ResponseEntity.ok(oPerfilSocial.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil social con ID " + id + " no encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo perfil social", description = "Registra un nuevo perfil social en la base de datos.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Perfil social creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del perfil social inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> save(@RequestBody PerfilSocial perfilSocial) {
        try {
            if (perfilSocial.getApodo() == null || perfilSocial.getApodo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El apodo del perfil social es obligatorio");
            }
            if (perfilSocial.getNombreRedSocial() == null || perfilSocial.getNombreRedSocial().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de la red social es obligatorio");
            }
            if (perfilSocial.getEnlace() == null || perfilSocial.getEnlace().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El enlace del perfil es obligatorio");
            }
            
            PerfilSocial savedPerfil = perfilSocialService.save(perfilSocial);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Perfil social creado correctamente con ID: " + savedPerfil.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el perfil social: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un perfil social", description = "Actualiza la información de un perfil social existente según su ID.")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Perfil social actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Perfil social no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody PerfilSocial perfilSocial) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de perfil social inválido");
            }
            
            PerfilSocial updatedPerfil = perfilSocialService.update(id, perfilSocial);
            if (updatedPerfil == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil social con ID " + id + " no encontrado");
            }
            
            return ResponseEntity.ok("Perfil social actualizado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el perfil social: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un perfil social", description = "Elimina un perfil social existente del sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Perfil social eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Perfil social no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de perfil social inválido");
            }
            
            if (!perfilSocialService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil social con ID " + id + " no encontrado");
            }
            
            perfilSocialService.delete(id);
            return ResponseEntity.ok("Perfil social eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el perfil social: " + e.getMessage());
        }
    }
}