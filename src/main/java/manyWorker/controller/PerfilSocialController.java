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
            @ApiResponse(responseCode = "200", description = "Lista de perfiles sociales obtenida correctamente")
    })
    public ResponseEntity<List<PerfilSocial>> findAll() {
        return ResponseEntity.ok(perfilSocialService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil social por ID", description = "Busca un perfil social específico utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Perfil social encontrado"),
            @ApiResponse(responseCode = "400", description = "Perfil social no encontrado")
    })
    public ResponseEntity<PerfilSocial> findById(@PathVariable int id) {
        Optional<PerfilSocial> oPerfilSocial = perfilSocialService.findById(id);
        return oPerfilSocial.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo perfil social", description = "Registra un nuevo perfil social en la base de datos.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Perfil social creado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el perfil social") 
    })
    public ResponseEntity<String> save(@RequestBody PerfilSocial perfilSocial) {
    	perfilSocialService.save(perfilSocial);
        return ResponseEntity.status(HttpStatus.OK).body("Perfil social creado correctamente");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un perfil social", description = "Actualiza la información de un perfil social existente según su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Perfil social actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Perfil social no encontrado o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el perfil social") 
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody PerfilSocial perfilSocial) {
        if (perfilSocialService.update(id, perfilSocial) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Perfil social no encontrado");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Perfil social actualizado correctamente");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un perfil social", description = "Elimina un perfil social existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Perfil social eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Perfil social no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<PerfilSocial> oPerfilSocial = perfilSocialService.findById(id);
        if (oPerfilSocial.isPresent()) {
        	perfilSocialService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Perfil social eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Perfil social no encontrado");
        }
    }
}