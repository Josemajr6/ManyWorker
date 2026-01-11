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
import manyWorker.entity.Admin;
import manyWorker.service.AdminService;

@RestController
@RequestMapping("/admin")
@Tag(name = "Administradores", description = "Controlador para la gestión de administradores")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(summary = "Obtener todos los administradores", description = "Devuelve una lista de todos los administradores del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de administradores obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay administradores registrados"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findAll() {
        List<Admin> admins = adminService.findAll();
        if (admins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay administradores registrados en el sistema");
        }
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar administrador por ID", description = "Busca un administrador específico utilizando su ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Administrador encontrado"),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de administrador inválido");
        }
        
        Optional<Admin> oAdmin = adminService.findById(id);
        
        if (oAdmin.isPresent()) {
            return ResponseEntity.ok(oAdmin.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador con ID " + id + " no encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo administrador", description = "Registra un nuevo administrador en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del administrador inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> save(@RequestBody Admin admin) {
        try {
            if (admin.getNombre() == null || admin.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del administrador es obligatorio");
            }
            if (admin.getApellido() == null || admin.getApellido().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El apellido del administrador es obligatorio");
            }
            if (admin.getCorreo() == null || admin.getCorreo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo del administrador es obligatorio");
            }
            
            Admin savedAdmin = adminService.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Administrador creado correctamente con ID: " + savedAdmin.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el administrador: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un administrador", description = "Actualiza la información de un administrador existente")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Admin admin) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de administrador inválido");
            }
            
            Admin updatedAdmin = adminService.update(id, admin);
            if (updatedAdmin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador con ID " + id + " no encontrado");
            }
            
            return ResponseEntity.ok("Administrador actualizado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el administrador: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un administrador", description = "Elimina un administrador existente del sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Administrador eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Administrador no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de administrador inválido");
            }
            
            if (!adminService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador con ID " + id + " no encontrado");
            }
            
            adminService.delete(id);
            return ResponseEntity.ok("Administrador eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el administrador: " + e.getMessage());
        }
    }
}