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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import manyWorker.entity.Mensaje;
import manyWorker.service.MensajeService;

@RestController
@RequestMapping("/mensaje")
public class MensajeController {

	@Autowired
	private MensajeService mensajeService;
	
	@GetMapping()
	public ResponseEntity<List<Mensaje>> findAll() {
		return ResponseEntity.ok(mensajeService.findAll());
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Método para buscar mensaje por id")
	public ResponseEntity<Mensaje> findById(@PathVariable int id) {
		Optional<Mensaje> oMensaje = mensajeService.findById(id);
		
		if (oMensaje.isPresent()) {
			return ResponseEntity.ok(oMensaje.get());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PostMapping
	public void save(@RequestBody Mensaje m, HttpServletResponse response) throws IOException {
		mensajeService.save(m);
		response.setStatus(400);
		response.getWriter().println("Mensaje creado.");
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id, HttpServletResponse response) throws IOException {
		Optional<Mensaje> oMensaje = mensajeService.findById(id);

		if (oMensaje.isPresent()) {
			response.setStatus(200);
			response.getWriter().println("Mensaje eliminado");
			mensajeService.delete(id);
		} else {
			response.setStatus(400);
			response.getWriter().println("Mensaje no encontrado");
		}
	}
	
	// Enviar un mensaje
    @PostMapping("/enviar")
    public Mensaje enviarMensaje(@RequestBody int idRemitente, @RequestBody int idDestinatario, @RequestBody String asunto, @RequestBody String cuerpo) {
        return mensajeService.enviarMensaje(idRemitente, idDestinatario, asunto, cuerpo);
    }

    // Ver los mensajes enviados
    @GetMapping("/enviados/{actorId}")
    public List<Mensaje> obtenerMensajesEnviados(@PathVariable int id) {
        return mensajeService.obtenerMensajesEnviados(id);
    }

    // Ver los mensajes recibidos
    @GetMapping("/recibidos/{actorId}")
    public List<Mensaje> obtenerMensajesRecibidos(@PathVariable int id) {
        return mensajeService.obtenerMensajesRecibidos(id);
    }
}
