package manyWorker.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import manyWorker.entity.Cliente;
import manyWorker.security.JWTUtils;
import manyWorker.service.ClienteService;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Clientes", description = "Controlador para la gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay clientes registrados"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findAll() {
        List<Cliente> clientes = clienteService.findAll();
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay clientes registrados en el sistema");
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca un cliente específico utilizando su ID")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> findById(@PathVariable int id) {
    	Cliente clienteLogueado = jwtUtils.userLogin();
        
        if (clienteLogueado == null || (clienteLogueado.getId() != id && !"ADMINISTRADOR".equals(clienteLogueado.getRol().name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para ver este perfil");
        }
    	
    	if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de cliente inválido");
        }
        
        Optional<Cliente> oCliente = clienteService.findById(id);
        
        if (oCliente.isPresent()) {
            return ResponseEntity.ok(oCliente.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente con ID " + id + " no encontrado");
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del cliente inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> save(@RequestBody Cliente cliente) {
        try {
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del cliente es obligatorio");
            }
            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El apellido del cliente es obligatorio");
            }
            if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo del cliente es obligatorio");
            }
            
            Cliente savedCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cliente creado correctamente con ID: " + savedCliente.getId());
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el cliente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza la información de un cliente existente")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Cliente cliente) {
    	Cliente clienteLogueado = jwtUtils.userLogin();
        
        if (clienteLogueado == null || clienteLogueado.getId() != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para actualizar este perfil");
        }
    	
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de cliente inválido");
            }
            
            Cliente updatedCliente = clienteService.update(id, cliente);
            if (updatedCliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente con ID " + id + " no encontrado");
            }
            
            return ResponseEntity.ok("Cliente actualizado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente existente del sistema")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> delete(@PathVariable int id) {
    	Cliente clienteLogueado = jwtUtils.userLogin();
        
        if (clienteLogueado == null || clienteLogueado.getId() != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar este perfil");
        }
    	
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de cliente inválido");
            }
            
            if (!clienteService.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente con ID " + id + " no encontrado");
            }
            
            clienteService.delete(id);
            return ResponseEntity.ok("Cliente eliminado correctamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el cliente: " + e.getMessage());
        }
    }
    
    @GetMapping("/exportar/{id}")
    @Operation(summary = "Exportar datos del cliente", description = "Exporta todos los datos de un cliente en formato estructurado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Datos del cliente exportados correctamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
        @ApiResponse(responseCode = "401", description = "No autenticado token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "No autorizado, permisos insuficientes"),
    })
    public ResponseEntity<?> exportarDatos(@PathVariable int id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de cliente inválido");
            }
            
            Map<String, Object> datos = clienteService.exportarDatos(id);
            return ResponseEntity.ok(datos);
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al exportar datos del cliente: " + e.getMessage());
        }
    }
}