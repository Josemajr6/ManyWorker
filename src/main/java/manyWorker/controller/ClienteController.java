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
import manyWorker.entity.Cliente;
import manyWorker.service.ClienteService;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Clientes", description = "Controlador para la gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista completa de todos los clientes registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida correctamente")
    })
    public ResponseEntity<List<Cliente>> findAll() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca un cliente específico utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> findById(@PathVariable int id) {
        Optional<Cliente> oCliente = clienteService.findById(id);
        return oCliente.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente en la base de datos.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el cliente") 
    })
    public ResponseEntity<String> save(@RequestBody Cliente cliente) {
        clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.OK).body("Cliente creado correctamente");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza la información de un cliente existente según su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al actualizar el cliente") 
    })
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Cliente cliente) {
        if (clienteService.update(id, cliente) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente no encontrado");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Cliente actualizado correctamente");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente existente de la base de datos utilizando su ID.")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Cliente no encontrado") 
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        Optional<Cliente> oCliente = clienteService.findById(id);
        if (oCliente.isPresent()) {
            clienteService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente no encontrado");
        }
    }
}
